package modelController.sessionController;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import entities.Learningresource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import modelController.viewerController.MainXhtml;
import tools.PersonalSessionSetup;
import tools.StaticFields;

@Named("learningresourceController")
@SessionScoped
public class LearningresourceController extends CommonModelController<Learningresource> implements Serializable {

    @Inject
    PersonalSessionSetup personalSessionSetup;
    @Inject
    private KnowledgeController knowledgeController;
    @Inject
    private EdgeamongknowledgeController edgeamongknowledgeController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private SubjectController subjectController;
    @Inject
    private modelController.applicationController.LearningresourceController applicationLearningresourceController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private boolean is4knowledge = false;//由于learningResources可能属于knowledge，也可能属于edgeAmongKnowledge，所以用这个变量进行标识
    //因为上传学习资源的步骤都在第二步，所以，该值可以在knowledge或edgeAmongKnowledg的第一步中进行设置

//    @Inject
//    private SubjectController subjectController;
    private Learningresource current;
    private List<Learningresource> learningResourceList = null;
    private final String tableName = "learningresource", listpage = "knowledge/List",
            editpage = "knowledge/CreateSecondStep", viewpage = "knowledge/View",
            createimagepage = "learningResource/CreateImage", createvideopage = "learningResource/CreateVideo",
            createaudiopage = "learningResource/CreateAudio", createpdfPage = "learningResource/CreatePDF";
    //   public final static int MAXPICTURENUMBER = 5;
    private Part[] picturePart;
    private Part[] filesPart;
    Knowledge knowledge = null;
    Edgeamongknowledge edgeamongKnowledge = null;

    public Learningresource getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Learningresource();
        }
        return current;
    }

    public void setSelected(Learningresource learningresource) {
        current = learningresource;
    }

    public Part[] getFilesPart() {
        if (filesPart == null) {
            filesPart = new Part[applicationLearningresourceController.MAXPICTURENUMBER];
        }
        return filesPart;
    }

    public void setFilesPart(Part[] filesPart) {
        this.filesPart = filesPart;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    private String SearchName;

    public void init() {
        if (subjectController.getSelected().getId() != null) {
            pageOperation.setDataModelList(applicationLearningresourceController.getLearningResourceList(subjectController.getSelected()));
            if (null != knowledgeController.getSelected().getId()) {
                List<Learningresource> learningresources = applicationLearningresourceController.getQueryResultList("select * from learningresource where knowledge_id=" + knowledgeController.getSelected().getId() + " and type=0");
                setSelected(learningresources.isEmpty() ? new Learningresource() : (Learningresource) learningresources.get(0));
            }
        }
    }
    //用于edit界面异步显示各种资源
    private int fileTypeFlag = 0;

    public boolean isImageType() {
        return fileTypeFlag == StaticFields.IMAGETYPE;
    }

    public boolean isVideoType() {
        return fileTypeFlag == StaticFields.VIDEOTYPE;
    }

    public boolean isAudioType() {
        return fileTypeFlag == StaticFields.AUDIOTYPE;
    }

    public boolean isPDFType() {
        return fileTypeFlag == StaticFields.PDFTYPE;
    }

    public int getFileTypeFlag() {
        return fileTypeFlag;
    }

    public void setFileTypeFlag(int fileTypeFlag) {
        this.fileTypeFlag = fileTypeFlag;
    }

    public SelectItem[] getAllFileItems() {
        SelectItem[] selectitem = new SelectItem[3];
        selectitem[0] = new SelectItem(StaticFields.IMAGETYPE, StaticFields.IMAGE);
        selectitem[1] = new SelectItem(StaticFields.VIDEOTYPE, StaticFields.VIDEO);
        selectitem[2] = new SelectItem(StaticFields.AUDIOTYPE, StaticFields.AUDIO);
        return selectitem;
    }

    public LearningresourceController() {
    }

//    public void prepareList() {
//        //getPageOperation().refreshData(applicationLearningresourceController.getLearningResourceList(subjectController.getSelected()));
//        mainXhtml.setPageName(listpage);
//    }
    //此变量用于现实create的资源界面
    private int fileType = 0;

    private void prepareCreate() {
        selectedItemIndex = -1;
        String loadPage = null;
        switch (this.fileType) {
            case StaticFields.IMAGETYPE:
                loadPage = createimagepage;
                break;
            case StaticFields.VIDEOTYPE:
                loadPage = createvideopage;
                break;
            case StaticFields.AUDIOTYPE:
                loadPage = createaudiopage;
                break;
            case StaticFields.PDFTYPE:
                loadPage = createpdfPage;
                break;
            default:
                loadPage = createimagepage;
                break;
        }
        mainXhtml.setPageName(loadPage);
    }

    public void prepareAndUpdate(int fileType) {
        this.fileType = fileType;
        if (is4knowledge) {
            knowledgeController.saveNow();
        } else {
            edgeamongknowledgeController.saveLearningResource();
        }
        prepareCreate();
    }

    //这个方法一定是在知识点维护的第3步，即学习资源维护的步骤被调用
    public void prepareAndUpdateLearningResource(int fileType) {
        createLearningResource();//先把上一步的学习资源保存
        //再设置新的资源类型
        this.fileType = fileType;
        prepareCreate();
    }

    public void create() {
        try {
            if (null == applicationLearningresourceController.findByName(current.getValueinfo())) {
                if (studentController.getLogined() != null) {
                    current.setStudentId(studentController.getLogined());
                } else if (teacherAdminController.getLogined() != null) {
                    current.setTeacherId(teacherAdminController.getLogined());
                }
                applicationLearningresourceController.create(current);
                updateCurrentItem();
                // evictForeignKey();
                userMessagor.addMessage(commonSession.getResourceBound().getString("LearningresourceCreated"));
                this.logs(current.getValueinfo(), tableName, StaticFields.OPERATIONDELETE);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getValueinfo() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller learn resouce 1");
        }
    }

    public Map<String, String> getVideos(Knowledge knowledge) {
        this.fileType = StaticFields.VIDEOTYPE;
        Map<String, String> map = new HashMap<>();
        List<Learningresource> learningresources = knowledgeController.getLearningResource(StaticFields.VIDEOTYPE, knowledge);
        if (!learningresources.isEmpty()) {
            setSelected(learningresources.get(0));
        } else {
            setSelected(new Learningresource());
        }
        if (null != current.getId()) {
            if (current.getValueinfo().contains(",")) {
                String[] videos = current.getValueinfo().split(",");
                for (String str : videos) {
                    map.put(str.substring(0, str.indexOf("#")), str.substring(str.indexOf("#") + 1));
                }
                return map;
            } else {
                String video = current.getValueinfo();
                map.put(video.substring(0, video.indexOf("#")), video.substring(video.indexOf("#") + 1));
                return map;
            }
        } else {
            return map;
        }
    }

    public String[] getAIPfileNames(int fileType, Knowledge knowledge) {
        this.fileType = fileType;
        List<Learningresource> learningresources = knowledgeController.getLearningResource(fileType, knowledge);
        if (!learningresources.isEmpty()) {
            setSelected(learningresources.get(0));
        } else {
            setSelected(new Learningresource());
        }
        if (null != current.getId()) {
            if (current.getValueinfo().contains(",")) {
                return current.getValueinfo().split(",");
            } else {
                return new String[]{current.getValueinfo()};
            }
        } else {
            return new String[0];
        }
    }

//下面3个方法用来控制页面显示哪些添加按钮，在添加音频的页面上，不再显示添加音频，其他类似
    public boolean notAudio(Map.Entry<Integer, String> resource) {
        this.fileType = StaticFields.AUDIOTYPE;
        return resource.getKey() != StaticFields.AUDIOTYPE;
    }

    public boolean notPDF(Map.Entry<Integer, String> resource) {
        this.fileType = StaticFields.PDFTYPE;
        return resource.getKey() != StaticFields.PDFTYPE;
    }

    public boolean notImage(Map.Entry<Integer, String> resource) {
        this.fileType = StaticFields.IMAGETYPE;
        return resource.getKey() != StaticFields.IMAGETYPE;
    }

    public boolean notVideo(Map.Entry<Integer, String> resource) {
        this.fileType = StaticFields.VIDEOTYPE;
        return resource.getKey() != StaticFields.VIDEOTYPE;
    }

    public void createLearningResource() {
        if (is4knowledge) {
            knowledge = knowledgeController.getSelected();
            if (null == knowledge || knowledge.getId() == null) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Knowledge")
                        + commonSession.getResourceBound().getString("Can")
                        + commonSession.getResourceBound().getString("Not")
                        + commonSession.getResourceBound().getString("Empty"));
                mainXhtml.setPageName(listpage);
                return;
            }
        } else {
            edgeamongKnowledge = edgeamongknowledgeController.getSelected();
            if (null == edgeamongKnowledge || edgeamongKnowledge.getId() == null) {
                userMessagor.addMessage(
                        commonSession.getResourceBound().getString("Edge")
                        + commonSession.getResourceBound().getString("Can")
                        + commonSession.getResourceBound().getString("Not")
                        + commonSession.getResourceBound().getString("Be")
                        + commonSession.getResourceBound().getString("Empty"));
                mainXhtml.setPageName(listpage);
                return;
            }
        }
        for (Part p : getFilesPart()) {
            int size = 0;
            if (p != null) {
                // boolean fileExist = true;
                //首先判断当前知识点下是否有此类资源
                List<Learningresource> learningresources = null;
                if (is4knowledge) {
                    learningresources = applicationLearningresourceController.getQueryResultList("select * from learningresource where knowledge_id=" + knowledge.getId() + " and type=" + fileType);
                } else {
                    learningresources = applicationLearningresourceController.getQueryResultList("select * from learningresource where edgeAmongKnowledge_id=" + edgeamongKnowledge.getId() + " and type=" + fileType);
                }
                if (learningresources == null || learningresources.isEmpty()) {//没有此类资源
                    Learningresource ls = new Learningresource();
                    if (is4knowledge) {
                        ls.setKnowledgeId(knowledge);
                    } else {
                        ls.setEdgeamongKnowledgeId(edgeamongKnowledge);
                    }
                    ls.setType(fileType);
                    if (fileType == StaticFields.VIDEOTYPE) {
                        ls.setValueinfo(knowledge.getId() + "_" + size + "." + tools.Tool.getExtension(p.getSubmittedFileName()) + "#" + p.getContentType());
                    } else {
                        ls.setValueinfo(knowledge.getId() + "_" + size + "." + tools.Tool.getExtension(p.getSubmittedFileName()));
                    }
                    applicationLearningresourceController.create(ls);
                } else {//已经存在此类资源
                    setSelected((Learningresource) learningresources.get(0));
                    size = current.getValueinfo().split(",").length;
                    if (fileType == StaticFields.VIDEOTYPE) {
                        current.setValueinfo(current.getValueinfo() + "," + knowledge.getId() + "_" + size + "." + tools.Tool.getExtension(p.getSubmittedFileName()) + "#" + p.getContentType());
                    } else {
                        current.setValueinfo(current.getValueinfo() + "," + knowledge.getId() + "_" + size + "." + tools.Tool.getExtension(p.getSubmittedFileName()));
                    }
                    applicationLearningresourceController.edit(current);
                }

                InputStream in = null;
                try {
                    in = p.getInputStream();
                    File file = new File(personalSessionSetup.getFilePath() + "/knowledgeResources" + "/" + getFoldName(fileType));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Files.copy(in, new File(personalSessionSetup.getFilePath() + "/knowledgeResources" + "/" + getFoldName(fileType) + "/" + knowledge.getId() + "_" + size + "." + tools.Tool.getExtension(p.getSubmittedFileName())).toPath());
                } catch (IOException ex) {
                }
            }
        }
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        mainXhtml.setPageName(editpage);
    }

    private String getFoldName(int type) {
        if (type == StaticFields.IMAGETYPE) {
            return StaticFields.IMAGE;
        }
        if (type == StaticFields.VIDEOTYPE) {
            return StaticFields.VIDEO;
        }
        if (type == StaticFields.AUDIOTYPE) {
            return StaticFields.AUDIO;
        }
        if (type == StaticFields.PDFTYPE) {
            return StaticFields.PDF;
        }
        return "";
    }

    public void deleteFiles(int type, String... strs) {
        Knowledge knowledge1 = knowledgeController.getSelected();
        List<Learningresource> learningresources ;
        learningresources = applicationLearningresourceController.getQueryResultList("select * from learningresource where knowledge_id=" + knowledge1.getId() + " and type=" + type);
        StringBuffer fileName;
        current = (Learningresource) learningresources.get(0);//一定存在，因为已经在页面上显示出来了
        //取得当前资源的所有值
        String valueInfoStr = current.getValueinfo();
        StringBuffer stringBuffer;
        //保存在StringBuffer中
        stringBuffer = new StringBuffer(valueInfoStr);

        if (strs.length == 1) {
            fileName = new StringBuffer(strs[0]);
        } else {
            fileName = new StringBuffer();
            for (String s : strs) {
                fileName.append(s);
            }
        }
        //找到前台传过来的值，从stringBuffer中删除掉,首先判断数据库中如果只有一个视频的话，则需要删除掉该条资源
        if (fileName.toString().equals(stringBuffer.toString())) {
            applicationLearningresourceController.remove(current);
            setSelected(null);
        } else {
            int index = stringBuffer.indexOf(fileName.toString());
            stringBuffer.delete(index, index + fileName.length() + 1);
            if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
                stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
            }
            current.setValueinfo(stringBuffer.toString());
            //更新当前知识点
            applicationLearningresourceController.edit(current);
            setSelected(current);
        }
        if (fileName.toString().contains("#")) {
            fileName = new StringBuffer(fileName.substring(0, fileName.indexOf("#")));
        }
        //删除服务器中的文件
        File f = new File(personalSessionSetup.getFilePath() + "/knowledgeResources/" + getFoldName(type) + "/" + fileName);
        if (f.exists()) {
            f.delete();
        }
        userMessagor.addMessage(commonSession.getResourceBound().getString("Delete") + " " + commonSession.getResourceBound().getString("Succeed"));
        this.logs(current.getValueinfo(), tableName, StaticFields.OPERATIONDELETE);
    }

    private void updateCurrentItem() {
        int count = applicationLearningresourceController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((Learningresource) applicationLearningresourceController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

//For view Edit and so on==================================
    StringBuilder sb = new StringBuilder();

//    public String search() {
//        //getKnowledgeList
//        learningResourceList.clear();
//        List<Knowledge> knowledges = applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected());
//        sb.delete(0, sb.length());
//        knowledges.forEach(knowledge -> {
//            sb.append(",").append(knowledge.getId());
//        });
//        if (sb.length() > 0) {
//            sb.delete(0, 1);
//            learningResourceList.clear();
//            List<Learningresource> tem = applicationLearningresourceController.getQueryResultList("select * from LearningResources where KNOWLEDGE_ID in(" + sb.toString() + ")"
//                    + " and locate('" + this.getSearchName().toLowerCase() + "',LOWER(valueinfo))>0");
//            tem.forEach(e -> {
//                learningResourceList.add((Learningresource) e);
//            });
//        }
//        getPageOperation().refreshData(learningResourceList);
//        mainXhtml.setPageName(this.listpage);
//    }
    boolean editAndView = false;

    public void prepareEdit(String editOrView) {
        if (editOrView.equals("edit")) {
            this.editAndView = true;
        } else {//"view"
            this.editAndView = false;
        }
        mainXhtml.setPageName(editpage);
        fileNames = new HashSet<>();//For Edit
    }

    public boolean isEditAndView() {
        return editAndView;
    }

    public void destroy() {
        current = (Learningresource) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(this.editpage);
    }

    private void performDestroy() {
        try {
            applicationLearningresourceController.remove(current);
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //knowledgesList.remove(current);
            //pageOperation.refreshData(applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected()));
            this.logs(current.getValueinfo(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller learn resouce 2");
        }
    }
//=====================

//    public void evict(Learningresource learningresourceurce) {
//        getFacade().evict(learningresourceurce.getClass(), learningresourceurce.getId(), learningresourceurce);
//    }
//
//    @Override
//    void evictForeignKey() {
//        knowledgeController.evict(current.getKnowledgeId());
//    }
    public String getSearchName() {
        return SearchName;
    }

    public void setSearchName(String knowledgeSearchName) {
        this.SearchName = knowledgeSearchName;
    }

    public Part[] getPicturePart() {
        if (picturePart == null) {
            picturePart = new Part[]{};
        }
        return picturePart;
    }

    public int numberOfnewPicturePart() {
        String[] valueStrings;
        int uploadShowNumber = 0;
        Learningresource ls = new Learningresource();
        if (null != current.getValueinfo()) {
            valueStrings = current.getValueinfo().split(",");
            uploadShowNumber = applicationLearningresourceController.MAXPICTURENUMBER - valueStrings.length;
        }
        //get the number of pictures and show them on Edit page
        //and less the number of pictures to be uploaded
        picturePart = new Part[uploadShowNumber];
        return picturePart.length;
    }

    public int numberOfnewFilePart() {
        Knowledge knowledge1 = knowledgeController.getSelected();
        String[] valueStrings;
        int uploadShowNumber ;
        setSelected(new Learningresource());
        if (knowledge1 != null && knowledge1.getId() != null) {
            List<Learningresource> lrList = new ArrayList<>();
            lrList = applicationLearningresourceController.getQueryResultList("select * from learningresource where knowledge_id=" + knowledge1.getId() + " and type=" + fileType);
            if (lrList != null && !lrList.isEmpty()) {
                setSelected((Learningresource) lrList.get(0));
            }
        }
        if (null != getSelected() && getSelected().getValueinfo() != null) {
            valueStrings = current.getValueinfo().split(",");
            uploadShowNumber = modelController.applicationController.LearningresourceController.MAXPICTURENUMBER - valueStrings.length;
        } else {
            uploadShowNumber = modelController.applicationController.LearningresourceController.MAXPICTURENUMBER;
        }
        //get the number of pictures and show them on Edit page
        //and less the number of pictures to be uploaded
        filesPart = new Part[uploadShowNumber];
        if (filesPart.length - 1 <= 0) {
            return 0;
        }
        return filesPart.length - 1;
    }

    public void setPicturePart(Part[] picturePart) {
        this.picturePart = picturePart;
    }

    HashSet<String> fileNames = new HashSet<>();

    String valueInfoString = "";

    public void update() {
        int i = 0;//上传图片的个数
        for (Part part : picturePart) {
            if (null != part) {
                try (InputStream input = part.getInputStream()) {
                    String fileName = part.getSubmittedFileName();
                    File fold = new File(personalSessionSetup.getFilePath() + "/knowledgeResources" + "/" + current.getKnowledgeId().getId());
                    if (!fold.exists()) {
                        fold.mkdir();
                    }
                    Files.copy(input, new File(personalSessionSetup.getFilePath() + "/knowledgeResources" + "/" + current.getKnowledgeId().getId(), fileName).toPath());
                    valueInfoString += "," + fileName;
                    i++;
                } catch (IOException e) {
                }
            }

        }
        if (i > 0) {
            if (current.getValueinfo().trim().length() == 0) {
                valueInfoString = valueInfoString.substring(1);
            }
            current.setValueinfo(current.getValueinfo() + valueInfoString);
            valueInfoString = "";//restore the blank
            if (null != current.getId()) {//不是新的记录
                applicationLearningresourceController.edit(current);
            } else {//是新的记录
                applicationLearningresourceController.create(current);
            }
        }
        mainXhtml.setPageName(viewpage);
    }

    public void fileValidate(FacesContext ctx, UIComponent comp, Object value) {
        String msgs = "";
        Part file = (Part) value;
        boolean tooBig = false;
        if (null != value) {
            switch (this.getFileType()) {
                case StaticFields.IMAGETYPE:
                    if (!file.getContentType().contains(getFoldName(this.getFileType()))) {
                        msgs = commonSession.getResourceBound().getString("Learningresource")
                                + commonSession.getResourceBound().getString("Not") + commonSession.getResourceBound().getString("Image");
                        userMessagor.addMessage(msgs);
                        break;
                    }
                    if (file.getSize() > applicationLearningresourceController.getMaxSize()) {
                        tooBig = true;
                    }
                    break;

                case StaticFields.VIDEOTYPE:
                    if (!file.getContentType().contains(getFoldName(this.getFileType()))) {
                        msgs = commonSession.getResourceBound().getString("Not") + commonSession.getResourceBound().getString("Video");
                        userMessagor.addMessage(msgs);
                        break;
                    }
                    if (file.getSize() > applicationLearningresourceController.getMaxVideoSize()) {
                        tooBig = true;
                    }
                    break;
                case StaticFields.AUDIOTYPE:
                    if (!file.getContentType().contains(getFoldName(this.getFileType()))) {
                        msgs = commonSession.getResourceBound().getString("Not") + commonSession.getResourceBound().getString("Audio");
                        userMessagor.addMessage(msgs);
                        break;
                    }
                    if (file.getSize() > applicationLearningresourceController.getMaxAudioSize()) {
                        tooBig = true;
                    }
                    break;
                case StaticFields.PDFTYPE:
                    if (!file.getContentType().contains(getFoldName(this.getFileType()))) {
                        msgs = commonSession.getResourceBound().getString("Not") + commonSession.getResourceBound().getString("PDF");
                        userMessagor.addMessage(msgs);
                        break;
                    }
                    if (file.getSize() > applicationLearningresourceController.getMaxPDFSize()) {
                        tooBig = true;
                    }
                    break;
            }
            if (tooBig) {
                msgs = commonSession.getResourceBound().getString("Too")
                        + commonSession.getResourceBound().getString("Big");
                userMessagor.addMessage(msgs);
            }
            if (msgs.trim().length() > 0) {
                throw new ValidatorException(new FacesMessage(msgs));
            }
        }
    }

    public boolean isExistImage() {
        Knowledge knowledge1 = knowledgeController.getSelected();
        return getAIPfileNames(StaticFields.IMAGETYPE, knowledge1).length == 0 && knowledge1.getId() != null;
    }

    public boolean isExistImageFoot() {
        Knowledge knowledge1 = knowledgeController.getSelected();
        return getAIPfileNames(StaticFields.IMAGETYPE, knowledge1).length > 0 && knowledge1.getId() != null;
    }

    public boolean isExistVideo() {
        Knowledge knowledge1 = knowledgeController.getSelected();
        return getVideos(knowledge1).size() == 0 && knowledge1.getId() != null;
    }

    public boolean isExistVideoFoot() {
        Knowledge knowledge1 = knowledgeController.getSelected();
        return getVideos(knowledge1).isEmpty() && knowledge1.getId() != null;
    }

    public boolean isExistAudio() {
        Knowledge knowledge1 = knowledgeController.getSelected();
        return getAIPfileNames(StaticFields.AUDIOTYPE, knowledge1).length == 0 && knowledge1.getId() != null;
    }

    public boolean isIs4knowledge() {
        return is4knowledge;
    }

    public void setIs4knowledge(boolean is4knowledge) {
        this.is4knowledge = is4knowledge;
    }

}
