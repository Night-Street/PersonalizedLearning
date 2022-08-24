package modelController.sessionController;

import entities.Student;
import entities.Studentdream;
import entities.Subject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import modelController.viewerController.MainXhtml;
import tools.NLP.WordsSegment;
import tools.PersonalSessionSetup;
import tools.StaticFields;

@Named("studentDreamController")
@SessionScoped
public class StudentdreamController extends CommonModelController<Studentdream> implements Serializable {

    @Inject
    modelController.applicationController.StudentdreamController applicationStudentdreamController;
    @Inject
    modelController.applicationController.SubjectController applicationSubjectController;
    @Inject
    SchoolController schoolController;
    @Inject
    RoleinfoController roleinfoController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    PersonalSessionSetup personalSessionSetup;
    private Studentdream current;
    private List<Studentdream> searchedStudentdreamsList = new LinkedList<>();
    //与梦想相关的图像
    private Part picture;
    private List<Subject> subjects2study;//要学的课程

    private final String tableName = "studentDream", editpage = "studentDream/Edit",
            viewpage = "studentDream/View", createpage = "studentDream/Create", listpage = "studentDream/List",
            list4TeacherPage = "studentDream/List4Teacher", foldName = "studentDream";

    public StudentdreamController() {
    }

    public Studentdream getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Studentdream();
        }
        return current;
    }

    public Student getCurrentStudent() {
        if (getSelected().getId() == null) {
            if (null != studentController.getLogined()) {
                return studentController.getLogined();
            } else {
                return new Student();
            }
        } else {
            return getSelected().getStuid();
        }
    }

    public void setSelected(Studentdream current) {
        this.current = current;
    }

    public void create() {
        try {
            String fileName = storePicture();
            if (null != fileName) {
                getSelected().setMedia(fileName);
            }
            getSelected().setStuid(studentController.getLogined());
            this.updateSubject();
            applicationStudentdreamController.create(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getStuid().getName() + " Dream", tableName, StaticFields.OPERATIONINSERT);
            mainXhtml.setPageName(this.viewpage);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Studentdream");
        }
    }

    private void updateSubject() {
        //更新所学课程
        if (null != subjects2study && !subjects2study.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Subject subject : subjects2study) {
                sb.append(subject.getId()).append(",");
            }
            String subjects = sb.toString();
            if (subjects.endsWith(",")) {
                subjects = subjects.substring(0, subjects.length() - 1);
            }
            getSelected().setKnowledges(subjects);
        }
    }

    public void prepareEdit() {
        setSelected((Studentdream) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.editpage);
    }

    public void update() {
        try {
            String fileName = storePicture();
            if (null != fileName) {
                getSelected().setMedia(getSelected().getMedia() + "," + fileName);
            }
            this.updateSubject();
            applicationStudentdreamController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
            this.logs(current.getStuid().getName() + " Dream", tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student 2");
        }
    }

    public List<String> getPictures() {//把图片名称封闭成list
        List<String> pictures = new LinkedList<>();
        if (getSelected().getId() != null) {
            if (getSelected().getMedia() != null) {
                if (getSelected().getMedia().trim().length() > 0) {
                    String[] tem = getSelected().getMedia().split(",");
                    for (int i = 0; i < tem.length; i++) {
                        pictures.add(tem[i]);
                    }
                }
            }
        }
        return pictures;
    }
//下面是为表格处理

    public void setDataModelList() {
        pageOperation.setDataModelList(new LinkedList(studentController.getLogined().getStudentdreamSet()));
    }

    //-------------------------------
    WordsSegment ws = new WordsSegment();

    public void prepareList() {
        if (null != studentController.getLogined()) {//如果是教师登录，则不显示
            List<Studentdream> listStudentdreams = new LinkedList(studentController.getLogined().getStudentdreamSet());
            pageOperation.refreshData(listStudentdreams);
            mainXhtml.setPageName(this.listpage);
        }
    }

    public void prepareView() {
        setSelected((Studentdream) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
    }

    public String prepareCreate() {
        setSelected(new Studentdream());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
        return null;
    }

    private String storePicture() {
        String fileName = null;
        if (picture != null) {
            InputStream in = null;
            try {
                in = picture.getInputStream();
                File file = new File(personalSessionSetup.getFilePath() + "/" + foldName);
                if (!file.exists()) {
                    file.mkdirs();
                }
                fileName = studentController.getLogined().getId() + "_" + picture.getSubmittedFileName() + "."
                        + tools.Tool.getExtension(picture.getSubmittedFileName());
                Files.copy(in, new File(personalSessionSetup.getFilePath() + "/" + foldName + "/" + fileName).toPath());
            } catch (IOException ex) {
            }
        }
        return fileName;
    }

    public void deleteFiles(String deletePicturename) {
        StringBuffer pictureNames = new StringBuffer();
        String[] pictures = getSelected().getMedia().split(",");
        for (String picture : pictures) {
            pictureNames.append(picture);
        }
        //找到前台传过来的值，从stringBuffer中删除掉,首先判断数据库中如果只有一个视频的话，则需要删除掉该条资源

        int index = pictureNames.indexOf(deletePicturename);
        pictureNames.delete(index, index + deletePicturename.length() + 1);
        if (pictureNames.charAt(pictureNames.length() - 1) == ',') {
            pictureNames.delete(pictureNames.length() - 1, pictureNames.length());
        }
        getSelected().setMedia(pictureNames.toString().trim());
        //更新当前知识点
        applicationStudentdreamController.edit(current);
        //删除服务器中的文件
        File f = new File(personalSessionSetup.getFilePath() + "/" + foldName + "/" + deletePicturename);
        if (f.exists()) {
            f.delete();
        }
        userMessagor.addMessage(commonSession.getResourceBound().getString("Delete") + " " + commonSession.getResourceBound().getString("Succeed"));
        this.logs("picture " + deletePicturename, tableName, StaticFields.OPERATIONDELETE);
    }

    //下面是为教师页面处理的方法    
    public String getStduentNames(Set<Integer> ids) {
        String result = "";
        HashSet<Student> students = new HashSet<>();//避免重复统计学生
        for (Integer id : ids) {
            students.add(applicationStudentdreamController.getStudentdream(id).getStuid());
        }
        for (Student student : students) {
            result += student.getSecondname() + student.getFirstname() + ",";
        }
        return result;
    }

    public void getStudentDream4Teacher() {
        List<Studentdream> allStudentdreams = new LinkedList<>();
        for (Student student : schoolController.getSelected().getStudentSet()) {
            allStudentdreams.addAll(student.getStudentdreamSet());
        }
        pageOperation.refreshData(allStudentdreams);
        //下面进行统计
        Map<Integer, String> idContent = new HashMap<>();
        for (Studentdream studentdream : allStudentdreams) {
            idContent.put(studentdream.getId(), //studentdream.getMedia() + "," + 
                    studentdream.getPlan() + "," + studentdream.getTitle()
                    + studentdream.getMydream());
        }
        ws.setKeyContents(idContent);
        mainXhtml.setPageName(this.list4TeacherPage);
    }

    public void prepareEdit4Teacher(Studentdream studentdream) {
        this.setSelected(studentdream);
        mainXhtml.setPageName(this.editpage);
    }

    public Map<String, Set<Integer>> getWordId() {
        return ws.getWordId();
    }

    public Map<String, Integer> getWordCount() {
        return ws.getWordCount();
    }

    String wordCloud;

    public String getWordCloud() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Integer> entry : getWordCount().entrySet()) {
            sb.append(entry.getKey())
                    .append(",")//用","分隔key与value
                    .append(entry.getValue() * 10)
                    .append("#");//用#分隔不同的词汇
        }
        String wordCloud = sb.toString();
        if (wordCloud.contains("#")) {
            wordCloud = wordCloud.substring(0, wordCloud.length() - 1);
        }
        return wordCloud;
    }

    public Part getPicture() {
        return picture;
    }

    public void setPicture(Part picture) {
        this.picture = picture;
    }

    public void fileValidate(FacesContext ctx, UIComponent comp, Object value) {
        String msgs = "";
        Part file = (Part) value;
        if (null != value) {
            if (!file.getContentType().contains("image")) {
                msgs = commonSession.getResourceBound().getString("StudentdreamController")
                        + commonSession.getResourceBound().getString("Not") + commonSession.getResourceBound().getString("Image");
                userMessagor.addMessage(msgs);
            }
            if (file.getSize() > 1024000) {
                msgs = commonSession.getResourceBound().getString("Too")
                        + commonSession.getResourceBound().getString("Big");
                userMessagor.addMessage(msgs);
            }
            if (msgs.trim().length() > 0) {
                throw new ValidatorException(new FacesMessage(msgs));
            }
        }
    }

    public List<Subject> getSubjects2study() {
//        if (getSelected().getId() != null) {
//            subjects2study = new LinkedList<Subject>();
//            if (null != getSelected().getKnowledges() && getSelected().getKnowledges().trim().length() > 0) {
//                String[] subjects = getSelected().getKnowledges().split(",");
//                for (String id : subjects) {
//                    subjects2study.add(applicationSubjectController.find(Integer.parseInt(id)));
//                }
//            }
//        }
        return subjects2study;
    }

    public void setSubjects2study(List<Object> subjects2study1) {
        //这个时候传上来的是Integer的List，需要转换
        this.subjects2study = new LinkedList<>();
        for (int i = 0; i < subjects2study1.size(); i++) {
            if (subjects2study1.get(i) instanceof Subject) {
                System.out.println("is Subject");
                subjects2study.add((Subject) subjects2study1.get(i));
            } else if (subjects2study1.get(i) instanceof Integer) {
                System.out.println("is Integer");
                subjects2study.add(applicationSubjectController.getSubject((Integer) subjects2study1.get(i)));
            }
        }
    }
}
