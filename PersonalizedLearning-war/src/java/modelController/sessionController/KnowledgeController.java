package modelController.sessionController;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import entities.Leadpoint;
import entities.Learningresource;
import entities.Praise;
import entities.Reexamination;
import entities.Student;
import entities.Subject;
import entities.TeacherAdmin;
import entities.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import jxl.*;
import jxl.read.biff.BiffException;
import modelController.viewerController.MainXhtml;
import sessionBeans.KnowledgeFacadeLocal;
import tools.StaticFields;

@Named("knowledgeController")
@SessionScoped
public class KnowledgeController extends CommonModelController<Knowledge> implements Serializable {

    @Inject
    private SubjectController subjectController;
    @Inject
    private SchoolController schoolController;
    @Inject
    private LearningresourceController learningresourceController;
    @Inject
    private modelController.applicationController.EdgeamongknowledgeController applicationEdgeamongknowledgeController;
    @Inject
    private modelController.applicationController.ReexaminationController applicationReexaminationController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.applicationController.LearningresourceController applicationLearningresourceController;
    @Inject
    private modelController.applicationController.StatusofresourcesController applicationStatusofresourcesController;
    @Inject
    private modelController.applicationController.TeacschoolsubjectController applicationTeacschoolsubjectController;
    @Inject
    private modelController.applicationController.TeacherAdminController applicationTeacherAdminController;
    @Inject
    private modelController.applicationController.PraiseController applicationPraiseController;
    @Inject
    private modelController.applicationController.LeadpointController applicationLeadpointController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private EdgeamongknowledgeController edgeamongknowledgeController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.sessionController.PredicateController predicateController;
    @Inject
    private modelController.sessionController.ReexaminationController reexaminationController;
    @Inject
    private modelController.sessionController.MyPublishedKnowledgeController myPublishedKnowledgeController;
    @EJB
    private KnowledgeFacadeLocal knowledgeFacadeLocal;
    private boolean isChanged = false;//????????????????????????,???????????????????????????????????????
    private String searchName = "";
    private final String tableName = "knowledge",
            listpage = "knowledge/List",
            editpage = "knowledge/CreateSecondStep",
            viewpage = "knowledge/View";
    protected final String createpage = "knowledge/Create";

    public KnowledgeController() {
    }
    protected Knowledge current;

    public Knowledge getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Knowledge();
        }
        return current;
    }
    
    //@PostConstruct
    //????????????subjectController.getSelected(),??????subjectController.getSelected()?????????knowledgeController.getSelected?????????????????????????????????
    public void setDataModelList() {
        if (null != subjectController.getSelected().getId()) {
            pageOperation.setDataModelList(applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected()));
        }
    }

    //-------------------------------For the search and viewall command button------------------
    //Have an int to check whether the subject is changed, if not, omit the execution of the SQL
    // private int selectedSubjectId = 0;
    //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    public List<Knowledge> getKnowledgeList4Subject(int type, Knowledge knowledge) {
        List<Knowledge> temKnowledges = new LinkedList<>();
        //????????????????????????????????????????????????????????????????????????????????????????????????
        if (null != knowledge && null != knowledge.getId()) {
            temKnowledges = applicationKnowledgeController.getKnowledgeList4Subject(knowledge.getSubjectId());
            if (type == 0) {//??????????????????????????????????????????????????????????????????
                temKnowledges.remove(knowledge);
            }
        } else {//????????????????????????????????????subject??????????????????
            temKnowledges = applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected());
        }
        return temKnowledges;
    }

    public void prepareMyRecorderKnowledgeList(Integer status) {
        User user = null;
        switch (commonSession.getUserType()) {
            case Student:
                user = studentController.getLogined();
                break;
            case Teacher:
            case Reexamin:
                user = teacherAdminController.getLogined();
                break;
            case Parent:
                user = parentController.getLogined();
                break;
        }
        List<Knowledge> result = getMyRecorderKnowledgeList(user, status);
        pageOperation.refreshData(result);
        if (result.size() > 0) {
            setSelected(result.get(0));
        }
    }

    private List<Knowledge> getMyRecorderKnowledgeList(User user, Integer status) {
        StringBuilder sb = new StringBuilder();
        if (null != user && null != user.getId()) {
            List<Reexamination> reexaminations = reexaminationController.getReexaminationKnowledgeListRecorderBy(user, status);
            reexaminations.forEach(re -> {
                sb.append(re.getKnowledgeid().getId()).append(",");
            });
        }
        String ids = sb.toString();
        if (ids.length() > 0) {
            ids = ids.endsWith(",") ? ids.substring(0, ids.length() - 1) : ids;
            return knowledgeFacadeLocal.getQueryResultList("select * from knowledge where id in (" + ids + ")");
        } else {
            return new LinkedList<>();
        }
    }

    public void prepareMyExaminedKnowledgeList(Integer status, Integer secondStatus) {
        TeacherAdmin user = teacherAdminController.getLogined();
        List<Knowledge> result = null;
        StringBuilder sb = new StringBuilder();
        List<Reexamination> reexaminations = reexaminationController.getReexaminationNeedToBeExamined(user, status, 0, secondStatus);
        reexaminations.forEach(re -> {
            sb.append(re.getKnowledgeid().getId()).append(",");
        });
        String ids = sb.toString();
        if (ids.length() > 0) {
            ids = ids.endsWith(",") ? ids.substring(0, ids.length() - 1) : ids;
            result = knowledgeFacadeLocal.getQueryResultList("select * from knowledge where id in (" + ids + ")");
        } else {
            result = new LinkedList<>();
        }
        pageOperation.refreshData(result);
        if (result.size() > 0) {
            setSelected(result.get(0));
        }
    }

    public void search() {
        if (searchName.trim().length() > 0) {
            List<Knowledge> searchedResult;
            if (subjectController.getSelected().getId() != null) {
                searchedResult = applicationKnowledgeController.searchByString(searchName, subjectController.getSelected().getId());
            } else {
                searchedResult = applicationKnowledgeController.searchByString(searchName, 0);
            }
            this.logs(searchName, tableName, StaticFields.OPERATIONSEARCH);
            pageOperation.refreshData(searchedResult);
            if (searchedResult.size() > 0) {
                setSelected(searchedResult.get(0));
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            } else {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Failed"));
            }
        } else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Please") + " "
                    + commonSession.getResourceBound().getString("Specify") + " "
                    + commonSession.getResourceBound().getString("Whatyouwanttosearch")
            );
        }
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName.toLowerCase();
    }

    public boolean canDelete() {
        return null == edgeamongknowledgeSet ? true : edgeamongknowledgeSet.isEmpty();
    }

    public String showedName(Knowledge knowledge) {
        StringBuilder sb = new StringBuilder();
        User tem;
        if (null != knowledge) {
            Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Knowledge(knowledge);
            if (!reexaminations.isEmpty()) {
                Reexamination reexamination = (Reexamination) reexaminations.toArray()[0];
                tem = reexamination.getTeacherid();
                if (null == tem) {//????????????
                    tem = reexamination.getStudentid();
                    if (null == tem) {//????????????
                        tem = reexamination.getParentid();
                    }
                }
                if (null != tem) {//???????????????
                    sb.append(null == tem.getSecondname() ? "" : tem.getSecondname()).append(" ")
                            .append(null == tem.getFirstname() ? "" : tem.getFirstname());
                }
            }
        }
        return sb.toString();
    }

    public void setSelected(Knowledge current) {
        this.current = current;
        //??????????????????temporaryEdgeamongknowledges???????????????????????????SPO
        if (null != temporaryEdgeamongknowledges) {
            temporaryEdgeamongknowledges.clear();
        } else {
            temporaryEdgeamongknowledges = new HashSet<>();
        }
        if (null != current && null != current.getSuccessorknowledgeSet()) {
            temporaryEdgeamongknowledges.addAll(current.getPredcessorKnowledgeSet());
        }
        //??????????????????????????????
        resetRelation();
    }

    private List<Knowledge> selectedmany;

    public List<Knowledge> getSelectedMany() {
        if (null == selectedmany) {
            selectedmany = new LinkedList<>();
        }
        return selectedmany;
    }

    public void setSelectedMany(List<Knowledge> myKnowledges) {
        selectedmany = myKnowledges;
    }
//------------------prepare View, edit, list,create------------------

    public String prepareList() {
        if (fromPublished) {
            myPublishedKnowledgeController.prepareList();
            fromPublished = false;
        } else {
            pageOperation.refreshData(new ArrayList(subjectController.getSelected().getKnowledgeSet()));
            setSelected(applicationKnowledgeController.getStartKnowledge(subjectController.getSelected()));
        }
        mainXhtml.setPageName(this.listpage);
        return null;
    }

    public void prepareView() {
        if (!fromCreate) {
            setSelected((Knowledge) (getItems().getRowData()));
            fromCreate = false;
        }
        if (!fromPublished) {
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        }
        mainXhtml.setPageName(this.viewpage);
    }

    public void prepareView(Knowledge knowledge) {
        refreshKnowledges();//For the Edit viewer, where the selected and candidated subjects
        setSelected(knowledge);
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
        setTemSelectedLeft(null);
    }

    public void prepareCreate() {
        refreshKnowledges();//????????????????????????????????????????????????????????????????????????????????????????????????????????????
        setSelected(new Knowledge());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
        isChanged = false;
    }

    public void finishAddEdge() {
        mainXhtml.setPageName(this.viewpage);
    }
    boolean fromCreate = false;

    public void create() {
        //??????????????????name???detail??????????????????
        learningresourceController.setIs4knowledge(true);//
        try {
            //1. check whether the name existed? If true, deny the operation;else continue;
            if (isChanged
                    ||//?????????????????????????????????????????????????????????????????????
                    !applicationKnowledgeController.isKnowledgeExisted(current.getName(), subjectController.getSelected())) {
                //??????esle??????????????????????????????????????????????????????????????????????????????
                refreshKnowledges();//For the Edit viewer, where the selected and candidated subjects
                current.setSubjectId(subjectController.getSelected());
                this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
                if (isChanged) {//??????????????????????????????
                    applicationKnowledgeController.edit(current);
                } else {
                    applicationKnowledgeController.create(current);
                    //current.setDetails(this.dealWithInnerKnowledgeIndex(current.getDetails()));
                    reexaminationController.createReexamination(current, null);
                    //setOldKnowledge(); 
                }
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                pageOperation.refreshData(applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected()));
                fromCreate = true;
                setTemSelectedLeft(null);
                mainXhtml.setPageName(this.editpage);
                similarKnowledges.clear();//??????????????????????????????????????????????????????????????????????????? 
                resetIndex();
            } else {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
        }

    }

    public void prepareEdit() {
        if (!fromPublished) {//????????????????????????????????????
            setSelected((Knowledge) getItems().getRowData());
            //setOldKnowledge();
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            this.refreshKnowledges();
        }
        isChanged = true;
        mainXhtml.setPageName(this.createpage);
        temSelectedLeft = null;
    }

    public void gotoView() {
        mainXhtml.setPageName(this.viewpage);
    }

    public String edit() {
        isChanged = true;
        mainXhtml.setPageName(this.createpage);
        this.refreshKnowledges();
        temSelectedLeft = null;
        resetIndex();
        return null;
    }

    public void refreshKnowledges() {
        getPageOperation().refreshData();
    }

    public void submit4Reexamination() {//???????????? 
        Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Knowledge(current);
        if (!reexaminations.isEmpty()) {
            Reexamination tem = (Reexamination) reexaminations.toArray()[0];
            tem.setStatus(applicationStatusofresourcesController.getStatusofresources(0));
            applicationReexaminationController.edit(tem);
            mainXhtml.setPageName(this.listpage);//???????????????svg???????????????????????????????????????????????????
        }
    }

    private void saveBefore() {
        if (null == current.getSubjectId() || null == current.getSubjectId().getId() //|| !(subjectController.getSelected().getId().equals(current.getSubjectId().getId()))
                ) {
            current.setSubjectId(subjectController.getSelected());
        }
        applicationKnowledgeController.edit(current);
        resetRelation();//?????????
        this.refreshKnowledges();//For the Edit viewer, where the selected and candidated subjects
    }

    private void saveAfter() {
        try {
            if (applicationRoleinfoController.isSecreatary(teacherAdminController.getLogined())) {
                //The secretary adjust the knowledgeId
                getSelected().setSubjectId(subjectController.getSelected());
                refreshKnowledges();
            }
            //applicationKnowledgeController.edit(getSelected());
            //getPageOperation().recreateModel();
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(this.viewpage);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller Knowledge 2");
            mainXhtml.setPageName(this.viewpage);
        }
    }

    public void saveNow() {//update for edgeAmongKnowledge
        saveBefore();
        Set<Reexamination> reexaminations = applicationReexaminationController.getReexamination4Knowledge(current);
        if (!reexaminations.isEmpty()) {
            Reexamination tem = (Reexamination) reexaminations.toArray()[0];
            tem.setStatus(applicationStatusofresourcesController.getStatusofresources(3));
            applicationReexaminationController.edit(tem);
        }
        saveAfter();
    }

    public void saveAnd2EdgeamongKnowledge() {
        saveNow();
        edgeamongknowledgeController.prepareCreate();
    }

    public void destroy() {
        current = (Knowledge) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        subjectController.getSelected().getKnowledgeSet().remove(current);
        setSelected(null);//????????????????????????????????????Subject?????????????????????
        mainXhtml.setPageName(this.listpage);
        setTemSelectedLeft(null);
        resetIndex();
    }
    int index = 1;

    public String getIndex() {
        return "k" + (++index);
    }

    private void resetIndex() {
        this.index = 1;//??????List.xhmtl????????????index
    }

    public String getSameIndex() {
        return "k" + index;
    }

    private void performDestroy() {
        try {
            if (current.getQuestionSet().isEmpty()) {
                removeKnowledge();
            } else {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Exist") + " "
                        + commonSession.getResourceBound().getString("Question"));
            }
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //knowledgesList.remove(current);
            getPageOperation().recreateModel();;
            pageOperation.refreshData(applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected()));
            this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller Knowledge 3");
        }
    }

    private void removeKnowledge() {
        Set<Reexamination> setOfReexaminations = current.getReexaminationSet();
        for (Reexamination reexamination : setOfReexaminations) {
            applicationReexaminationController.remove(reexamination);
        }
        Set<Learningresource> setOfLearningresources = current.getLearningresourceSet();
        for (Learningresource learningresource : setOfLearningresources) {
            applicationLearningresourceController.remove(learningresource);
        }
        Set<Edgeamongknowledge> setOfEdgeamongknowledges = current.getPredcessorKnowledgeSet();
        for (Edgeamongknowledge edgeamongknowledge : setOfEdgeamongknowledges) {
            applicationEdgeamongknowledgeController.remove(edgeamongknowledge);
        }
        setOfEdgeamongknowledges = current.getSuccessorknowledgeSet();
        for (Edgeamongknowledge edgeamongknowledge : setOfEdgeamongknowledges) {
            applicationEdgeamongknowledgeController.remove(edgeamongknowledge);
        }
        Set<Praise> setOfPraises = current.getPraiseSet();
        for (Praise praise : setOfPraises) {
            applicationPraiseController.remove(praise);
        }

        applicationKnowledgeController.remove(current);
    }

    private void updateCurrentItem() {
        int count = applicationKnowledgeController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            pageOperation.refreshData();
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((Knowledge) applicationKnowledgeController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    @Override
    public DataModel getItems() {
        if (null != subjectController.getSelected().getId() && subjectController.isRenewed()) {
            pageOperation.refreshData(applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected()));
        }
        return pageOperation.getItems();
    }

    public Knowledge getKnowledge(java.lang.Integer id) {
        return (Knowledge) applicationKnowledgeController.find(id);
    }

    //-------------------------------
    //================For the parents knowledge ===============
    //temKnowledges is those being selected in the manyListbox
    private HashSet<Knowledge> temSelectedLeft = new HashSet<>(), temSelectedRight = new HashSet<>(),
            temSelectedLeftNew = new HashSet<>();//??????????????????????????????

    public Set<Knowledge> getTemSelectedLeft() {
        temSelectedLeft = applicationKnowledgeController.getPredcessorKnowledges(getSelected());
        return temSelectedLeft;
    }

    public void setTemSelectedLeft(HashSet<Knowledge> subjectList) {
        temSelectedLeft = subjectList;
        if (null != subjectList) {
            temSelectedLeftNew.clear();
            temSelectedLeftNew.addAll(temSelectedLeft);
        }
    }

    public HashSet<Knowledge> getTemSelectedLeftNew() {
        return temSelectedLeftNew;
    }

    public Set<Knowledge> getTemSelectedRight() {
        return temSelectedRight;
    }

    public void setTemSelectedRight(HashSet<Knowledge> subjectList) {
        temSelectedRight = subjectList;
    }
//   //================For the parents knowledge ===============
//    //temKnowledges is those being selected in the manyListbox
//    private HashSet<Predicate> temSelectedLeftPredicates = new HashSet<>();//??????????????????????????????
//
//    public HashSet<Predicate> getTemSelectedLeftPredicates() {
//        return temSelectedLeftPredicates;
//    }
//
//    public void setTemSelectedLeftPredicates(HashSet<Predicate> temSelectedLeftPredicates) {
//        this.temSelectedLeftPredicates = temSelectedLeftPredicates;
//    }
    private Part knowledgeExcelFile;

    public Part getKnowledgeExcelFile() {
        return knowledgeExcelFile;
    }

    public void setKnowledgeExcelFile(Part knowledgeExcelFile) {
        this.knowledgeExcelFile = knowledgeExcelFile;
    }

    public String knowledgesUpload() {
        List<NewOldID> newoldIds = new ArrayList<>();
        int i = 2;
        try {
            if (null == subjectController.getSelected().getId()) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Select") + commonSession.getResourceBound().getString("Subject"));
                return null;
            }
            if (null == this.knowledgeExcelFile || this.getFilename(knowledgeExcelFile).trim().length() == 0) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("ExcelSelection"));
                return null;
            }
            InputStream ins = knowledgeExcelFile.getInputStream();
            Workbook book = Workbook.getWorkbook(ins);
            Sheet sheet = book.getSheet(0);
            int rownum = sheet.getRows();
            try {
                for (; i < rownum; i++) {
                    Knowledge knowledge = new Knowledge();
                    Edgeamongknowledge edgeamongknowledge = new Edgeamongknowledge();
                    //????????????????????????????????????knowledge??????
                    Knowledge createdNewId = null;
                    //???????????????????????????sheet???????????????????????????????????????????????????
                    if (applicationKnowledgeController.getQueryResultList("select * from knowledge where name='" + sheet.getCell(2, i).getContents().trim() + "' and subject_id=" + subjectController.getSelected().getId()).isEmpty()) {
                        knowledge.setSubjectId(subjectController.getSelected());
                        knowledge.setName(sheet.getCell(2, i).getContents());
                        knowledge.setDetails(sheet.getCell(3, i).getContents());
//                            knowledge.setLevelnumber();
                        applicationKnowledgeController.create(knowledge);
                        reexaminationController.createReexamination(knowledge, null);
                    }
                    List<Knowledge> knowledges = applicationKnowledgeController.getQueryResultList("select * from knowledge where name='" + sheet.getCell(2, i).getContents().trim() + "' and subject_id=" + subjectController.getSelected().getId());
                    //??????????????????????????????id???
                    createdNewId = (Knowledge) knowledges.get(0);
//                    newoldIdMap.put(createdNewId.getId(), Integer.parseInt(sheet.getCell(0, i).getContents()));
                    Integer preNode = null;
                    //???????????????????????????null????????????????????????????????????????????????
                    try {
                        preNode = Integer.parseInt(sheet.getCell(1, i).getContents());
                    } catch (Exception e) {
                        //???????????????map???????????????????????????????????????????????????
                        newoldIds.add(new NewOldID(createdNewId.getId(), Integer.parseInt(sheet.getCell(0, i).getContents())));

                    }
                    //??????????????????????????????map??????????????????????????????map??????????????????id???????????????
                    if (preNode != null) {
                        for (NewOldID newOldID : newoldIds) {
                            if (Objects.equals(newOldID.oldId, preNode)) {
                                //???????????????????????????????????????id???????????????????????????????????????id?????????????????????????????????
                                edgeamongknowledge.setSuccessornode(createdNewId);
                                edgeamongknowledge.setPredecessornode(new Knowledge(newOldID.newId));
                                //????????????????????????????????????????????????????????????????????????
                                if (applicationEdgeamongknowledgeController.getQueryResultList("select * from EDGEAMONGKNOWLEDGE where successornode=" + edgeamongknowledge.getSuccessornode().getId() + " and predecessornode=" + edgeamongknowledge.getPredecessornode().getId()).isEmpty()) {
                                    applicationEdgeamongknowledgeController.create(edgeamongknowledge);
                                }
                                break;
                            }
                        }
                        //????????????map?????????????????????id????????????id??????map??????
                        newoldIds.add(new NewOldID(createdNewId.getId(), Integer.parseInt(sheet.getCell(0, i).getContents())));
                    }

                }
            } catch (NumberFormatException e) {
                i = -1;
            } finally {
                book.close();
            }
//            }
        } catch (BiffException | IOException ex) {
            i = -2;
        }

        switch (i) {
            case -2:
                userMessagor.addMessage(commonSession.getResourceBound().getString("ExcelWrong3"));
            case -1:
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("ExcelWrong2"));
                break;
            case 2:
                userMessagor.addMessage(commonSession.getResourceBound().getString("Import") + commonSession.getResourceBound().getString("Empty"));
                break;
            default:
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed") + " " + i + commonSession.getResourceBound().getString("Knowledge"));
        }
        return null;
    }

    private String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }

        return null;
    }

    public String getKnowledgeRelationship4dagre(Knowledge knowledge) {
        String result = "";
        if (null == knowledge || knowledge.getId() == null) {//????????????????????????????????????
            if (subjectController.getSelected().getId() != null) {//?????????????????????????????????????????????????????????
                knowledge = applicationKnowledgeController.getStartKnowledge(subjectController.getSelected());
            }
        }
        result = applicationKnowledgeController.getKnowledgeRelationship4dagre(knowledge); // ????????????????????????
        //System.out.println(result);
        return result;
    }

    public void setKnowledgeRelationship4dagre(String virtualString) {//Just a virtual one, but not a real one
    }
     
    // ???????????????????????????????????????
    public String getAllStudentKnowledge() {
        if (null == schoolController.getSelected().getId()) {
            return "";
        } else {
            Set<Student> studentSet = new HashSet<>();
            if (null == studentController.getSelected().getId()) {
                List<Student> students = applicationStudentController.getStudentList(schoolController.getSelected());
                studentSet.addAll(students);
            } else {
                studentSet.add(studentController.getSelected());
            }
            String result = "digraph {"
                    + "node [rx=5 ry=5 labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"] "
                    + "edge [labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"]";

            return result + getKnowledgeRelationship4dagre(subjectController.getSelected(), studentSet) + "}";
        }

    }
    
    

    Set<Knowledge> getKnowledgeToRecursion(Knowledge k) {
        Set<Knowledge> knowledgeSet = new HashSet<>();
        //??????????????????
        //???????????????????????????????????????null
        if (k == null) {
            return null;
        }
        //k?????????????????????????????????
        knowledgeSet.add(k);
        if (k.getSuccessorknowledgeSet() == null) {
            //??????????????????????????????????????????
            return knowledgeSet;
        }
        for (Edgeamongknowledge eak : k.getSuccessorknowledgeSet()) {
            k = eak.getSuccessornode();
            knowledgeSet.add(k);
            knowledgeSet.addAll(getKnowledgeToRecursion(k));
        }
        return knowledgeSet;
    }

    private void renameDuplicate(String name) {
        List<Knowledge> knoweList = applicationKnowledgeController.getQueryResultList("select * from knowledge where name='" + name.trim() + "'");
        knoweList.forEach(knowledge -> {
            knowledge.setName(knowledge.getSubjectId().getName() + knowledge.getName());
            applicationKnowledgeController.edit(knowledge);
        });
    }

    public String renameStartAndEnd() {
        this.renameDuplicate("start");
        this.renameDuplicate("end");
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        return null;
    }

    //???????????????????????????????????????????????????
    //boolean learningResourceChanged = true;
    private List<Learningresource> learningresourcesList = new LinkedList<>();

    private void resourceClear(int type) {
        if (type == StaticFields.AUDIOTYPE) {
            audioLearningresources.clear();
        }
        if (type == StaticFields.VIDEOTYPE) {
            videoLearningresources.clear();
        }
        if (type == StaticFields.IMAGETYPE) {
            imageLearningresources.clear();
        }
        if (type == StaticFields.PDFTYPE) {
            pdfLearningResource.clear();
        }
    }

    public List<Learningresource> getLearningResource(int type, Knowledge knowledge) {
        String sqlString = "";
        if (knowledge.getId() == null || null == knowledge) {
            sqlString = "select * from learningresource where type=" + type;
        } else if (knowledge.getId() != null) {
            sqlString = "select * from learningresource where knowledge_id=" + knowledge.getId() + " and type=" + type;
        }
        learningresourcesList = applicationLearningresourceController.getQueryResultList(sqlString);
        resourceClear(type);
        learningresourcesList.forEach(resource -> {
            if (type == StaticFields.IMAGETYPE && resource.getType() == StaticFields.IMAGETYPE) {
                imageLearningresources.add(resource);
            }
            if (type == StaticFields.VIDEOTYPE && resource.getType() == StaticFields.VIDEOTYPE) {
                videoLearningresources.add(resource);
            }
            if (type == StaticFields.AUDIOTYPE && resource.getType() == StaticFields.AUDIOTYPE) {
                audioLearningresources.add(resource);
            }
            if (type == StaticFields.PDFTYPE && resource.getType() == StaticFields.PDFTYPE) {
                pdfLearningResource.add(resource);
            }
        });
        //learningResourceChanged = false;

        return learningresourcesList;
    }
    private List<Learningresource> imageLearningresources = new LinkedList<>(),
            videoLearningresources = new LinkedList<>(),
            audioLearningresources = new LinkedList<>(), pdfLearningResource = new LinkedList<>();

    public List<Learningresource> getImageResource(Knowledge knowledge) {
        getLearningResource(StaticFields.IMAGETYPE, knowledge);
        return imageLearningresources;
    }

    public List<Learningresource> getVedioResource(Knowledge knowledge) {
        getLearningResource(StaticFields.VIDEOTYPE, knowledge);
        return videoLearningresources;
    }

    public List<Learningresource> getAudioResource(Knowledge knowledge) {
        getLearningResource(StaticFields.AUDIOTYPE, knowledge);
        return audioLearningresources;
    }

    public List<Learningresource> getPDFResource(Knowledge knowledge) {
        getLearningResource(StaticFields.PDFTYPE, knowledge);
        return pdfLearningResource;
    }

    public List<String> getResourceString(List<Learningresource> resourceList) {
        List<String> fileNames = new LinkedList<>();
        resourceList.forEach(resource -> {
            String[] names = resource.getValueinfo().split(",");
            fileNames.addAll(Arrays.asList(names));
        });
        return fileNames;
    }

    //==================????????????===============??????========
    List<Knowledge> similarKnowledges = new LinkedList<>();

    public List<Knowledge> getSimilarKnowledges() {
        int type = 0;
        if (null != current.getName() && current.getName().trim().length() > 0) {//??????????????????????????????
            type = 1;
        }
        //?????????????????????????????????????????????????????????????????????????????????
        if (null != current.getDetails() && current.getDetails().trim().length() > 13) {//?????????????????????<p>&nbsp;</p>???????????????
            type = 2;
        }
        if (type > 0) {
            similarKnowledges = applicationKnowledgeController.getSimilarKnowledges(current.getName(), type);
        }
        return similarKnowledges;
    }

    public boolean showSimilarKnowledge() {
        return similarKnowledges.size() > 0;
    }

    //==================????????????===============??????========
    private class NewOldID {

        private Integer newId;
        private Integer oldId;

        public NewOldID() {
        }

        public NewOldID(Integer newId, Integer oldId) {
            this.newId = newId;
            this.oldId = oldId;
        }

        public Integer getNewId() {
            return newId;
        }

        public void setNewId(Integer newId) {
            this.newId = newId;
        }

        public Integer getOldId() {
            return oldId;
        }

        public void setOldId(Integer oldId) {
            this.oldId = oldId;
        }

    }

//?????????????????????????????????
    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    private Set<Edgeamongknowledge> temporaryEdgeamongknowledges;//???????????????????????????????????????????????????????????????????????????????????????
    //??????temporaryEdgeamongknowledges?????????????????????
    HashMap<Integer, Edgeamongknowledge> edgeamongknowledgeSet;

    private int id = 0;//?????????knowledg--CreateSecondStep??????????????????SPO???O?????????????????????????????????????????????????????????????????????????????????

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void resetRelation() {
        edgeamongknowledgeSet = null;
        temporaryEdgeamongknowledges = null;
        id = 0;
    }

    public HashMap<Integer, Edgeamongknowledge> getTemporaryEdgeamongknowledgesMap() {
        if (null == edgeamongknowledgeSet || edgeamongknowledgeSet.isEmpty()) {
            edgeamongknowledgeSet = new HashMap<>();
            //int id = 0;
            for (Edgeamongknowledge ek : getTemporaryEdgeamongknowledges()) {
                edgeamongknowledgeSet.put(id++, ek);
            }
        }
        return edgeamongknowledgeSet;
    }

    public Edgeamongknowledge getEdgeamongknowledge() {
        return edgeamongknowledgeSet.get(id);
    }

    private Set<Edgeamongknowledge> getTemporaryEdgeamongknowledges() {
        if (null == temporaryEdgeamongknowledges) {
            temporaryEdgeamongknowledges = new HashSet<>();
            temporaryEdgeamongknowledges.addAll(applicationEdgeamongknowledgeController.getByPredecessor(getSelected()));
        }
        return temporaryEdgeamongknowledges;
    }

    private HashMap<Integer, String> levelMap = null;

    public HashMap<Integer, String> getLevelMap() {
        if (null == levelMap) {
            levelMap = new HashMap<>();
            levelMap.put(1, commonSession.getResourceBound().getString("Knowledge"));//?????????
            levelMap.put(2, commonSession.getResourceBound().getString("Volume"));//???
            levelMap.put(3, commonSession.getResourceBound().getString("Chapter"));//???
            levelMap.put(4, commonSession.getResourceBound().getString("Section"));//???
            levelMap.put(5, commonSession.getResourceBound().getString("Subsection"));//??????
        }
        return levelMap;
    }

    private boolean fromPublished = false;
    //???prepareList????????????false

    //???myPublishedKnowledge?????????Edit???View??????
    public boolean isFromPublished() {
        return fromPublished;
    }

    public void setFromPublished(boolean fromPublished) {
        this.fromPublished = fromPublished;
    }
//        public boolean canAdd() {
//        return relatedKnolwdge == null || relatedKnolwdge.getId() == null
//                || predicateController.getSelected() == null || predicateController.getSelected().getId() == null;
//    }

    //    public void addKnowledgeRelation() {//?????????????????????
//        if (null == predicateController.getSelected() || null == relatedKnolwdge) {//????????????
//            return;
//        }
//        Edgeamongknowledge temEdgeamongknowledge = new Edgeamongknowledge();
//        temEdgeamongknowledge.setPredecessornode(relatedKnolwdge);
//        temEdgeamongknowledge.setSuccessornode(this.getSelected());
//        temEdgeamongknowledge.setPredicate(predicateController.getSelected());
//        int temId = getTemporaryEdgeamongknowledgesMap().keySet().size();
//        while (getTemporaryEdgeamongknowledgesMap().keySet().contains(temId)) {
//            temId++;
//        }
//        getTemporaryEdgeamongknowledgesMap().put(temId, temEdgeamongknowledge);
//    }
    public String getKnowledgeRelationship4dagre4OneStudent(Subject subject, Student student) {
        String result = "digraph {"
                + "node [rx=5 ry=5 labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"] "
                + "edge [labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"]";
        if (subject == null || subject.getId() == null) {
            result += "}";
        } else {
            HashSet<Student> oneStudent = new HashSet<>();
            oneStudent.add(student);
            result += getKnowledgeRelationship4dagre(subject, oneStudent) + "}";
        }
        return result;
    }

    private String getKnowledgeRelationship4dagre(Subject subject, Set<Student> studentSet) {
        List<Knowledge> leadingPoint = new LinkedList<>();
        if (studentSet.size() > 0) {
            for (Student student : studentSet) {
                //????????????????????????????????????
//                List<Leadpoint> leadpoints = applicationLeadpointController.getAllLeadpointList(student, subject);
//                for (Leadpoint leadpoint : leadpoints) {
//                    leadingPoint.addAll(getKnowledgesList4LeadingPoint(leadpoint));
//                }
                //????????????????????????????????????????????????
                List<Leadpoint> leadpoints = applicationLeadpointController.getAllLeadpointList(student, subject);
                if (!leadpoints.isEmpty()) {
                    leadingPoint.addAll(applicationKnowledgeController.getKnowledgesList4LeadingPoint(leadpoints.get(0)));
                }

            }
        }
        String resultString = "";
        // Color those knowledge on the leading point
        if (leadingPoint.size() > 0 && studentSet.size() > 1) {
            Map<Knowledge, Integer> knowledgeCount = new HashMap<Knowledge, Integer>();
            Set<Knowledge> leadingPointSet = new HashSet<>(leadingPoint);
            for (Knowledge k : leadingPointSet) {
                knowledgeCount.put(k, 1); // ??????????????????????????????
            }
            for (Knowledge current : leadingPoint) {
                for (Map.Entry<Knowledge, Integer> entry : knowledgeCount.entrySet()) {
                    if (Objects.equals(current.getId(), entry.getKey().getId())) {
                        entry.setValue(entry.getValue() + 1);// ??????????????????????????????
                        break;
                    }
                }
            }
            for (Map.Entry<Knowledge, Integer> entry : knowledgeCount.entrySet()) {
                int kCount = entry.getValue();
                //double fontSize = 32 + Math.ceil(kCount * 0.6);
                double colorRed = 1 + Math.ceil(kCount * 9);
                if (colorRed > 255) {
                    colorRed = 255;
                }
                int colorIndex = (int) Math.ceil(colorRed / 25.5) - 1;
//                String colorStr = "rgb(" + colorRed + ",0,0)";
                resultString += "\"" + entry.getKey().getName()
                        + "\" [labelType=\"html\" label=\"<span style='font-size:"
                        + applicationKnowledgeController.getFontSizeArr()[colorIndex] + "px;color:"
                        + applicationKnowledgeController.getColorStrArr()[colorIndex] + ";'>" + entry.getKey().getName()
                        + "</span>\"];";

            }
            setLeadpointInformation(commonSession.getResourceBound().getString("Leadpoint")
                    + ":" + leadingPoint.size()
            );
        } else {
            for (Knowledge current : leadingPoint) {
                resultString += "\"" + current.getName()
                        + "\" [labelType=\"html\" label=\"<span style='font-size:32px;color:red;'>" + current.getName()
                        + "</span>\"];";
            }
            setLeadpointInformation(commonSession.getResourceBound().getString("No")
                    + commonSession.getResourceBound().getString("Leadpoint")
            );
        }
        List<Edgeamongknowledge> tem = applicationEdgeamongknowledgeController.getEdgeamongknowledgeList(subject);
        for (Edgeamongknowledge edge : tem) {
            resultString += applicationEdgeamongknowledgeController.getString4Dagre(edge);
        }
        return resultString;
    }
    private String leadpointInformation = "";

    public String getLeadpointInformation() {
        return subjectController.getSubjectName() + leadpointInformation;
    }

    public void setLeadpointInformation(String leadpointInformation) {
        this.leadpointInformation = leadpointInformation;
    }
}
