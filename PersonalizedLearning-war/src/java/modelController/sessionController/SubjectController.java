package modelController.sessionController;

import entities.Edgeamongsubject;
import entities.Knowledge;
import entities.Leadpoint;
import entities.Majorsubject;
import entities.School;
import entities.Student;
import entities.Subject;
import entities.TeacherAdmin;
import entities.Teacschoolsubject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("subjectController")
@SessionScoped
public class SubjectController extends CommonModelController<Subject> implements Serializable {

    @Inject
    private MajorController majorController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private MajorsubjectController majorsubjectController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private EdgeamongSubjectController edgeamongSubjectController;
    @Inject
    private SchoolController schoolController;
    @Inject
    private modelController.applicationController.SubjectController applicationSubjectController;
    @Inject
    private modelController.applicationController.MajorController applicationMajorController;
    @Inject
    private modelController.applicationController.MajorsubjectController applicationMajorsubjectController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.TeacschoolsubjectController applicationTeacschoolsubjectController;
    @Inject
    private modelController.applicationController.LeadpointController applicationLeadpointController;
    @Inject
    private modelController.applicationController.EdgeamongSubjectController applicationEdgeamongSubjectController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.KnowledgeController knowledgeController;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    private Set<Subject> subjectForSchoolList = new HashSet<>();
    private ArrayList<Subject> selectedSubjects4Major = new ArrayList<>();
    private List<Subject> searchedSubjectList = new LinkedList<>();
    private List<Subject> selectedSubjectList = new LinkedList<>();
    private List<Subject> subjectList;
    private String searchName;
    private final String tableName = "subject", listpage = "subject/List", editpage = "subject/Edit",
            createpage = "subject/Create", viewerpage = "subject/View";
    protected Subject current;
    private Subject candidateSubject;//删除一门课时前，把所有资源移到该课程下

    public Subject getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Subject();
        }
        return current;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(getSubjectList(1));
    }

    //-------------------------------For the search and view all command button------------------
    @Inject
    SubjectController subjectController;

    public List<Subject> getSubjectList(int type) {//type==0，则去掉当前的课程
        if (null != subjectList) {
            subjectList.clear();
        } else {
            subjectList = new LinkedList<>();
        }
        //把当前课程去掉
        HashSet<Subject> subjectSet = getSubject4Login();
        if (type == 0) {
            subjectSet.remove(getSelected());
        }
        subjectList.addAll(subjectSet);
        subjectList.sort((Subject o1, Subject o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return subjectList;
    }

    private HashSet<Subject> getSubject4Login() {
        HashSet<Subject> subjectSet = new HashSet<>();//有可能有重复的课程加入，所以先用hashset保存，然后再导入list
        if (null != studentController.getLogined()) {//是学生登录
            subjectSet.addAll(getSubject4SchoolList(studentController.getLogined().getSchoolId()));
//            //检查当前时间段内是否有考试课程，如果有，则直接设置其默认值为对应的考试课程
//            Set<Testpaper> testpaperSet = studentController.getLogined().getSchoolId().getTestpaperSet();
//            Calendar today = Calendar.getInstance();
//            Calendar testDay = Calendar.getInstance();
//            for (Testpaper testpaper : testpaperSet) {
//                if (today.before(testpaper.getEndtime())) {//还没有完成考试
//                    testDay.setTime(testpaper.getStart());
//                    if (today.get(Calendar.DAY_OF_YEAR) == testDay.get(Calendar.DAY_OF_YEAR)) {//正好是这一天
//                        String[] subjects = testpaper.getSubjectids().split(",");
//                        subjectController.setSelected(applicationSubjectController.getSubject(Integer.valueOf(subjects[0])));
//                        break;
//                    }
//                }
//            }
        } else if (null != teacherAdminController.getLogined()) {//是教师登录
            applicationSubjectController.getAllList().forEach(e -> {
                subjectSet.add((Subject) e);
            });
        }
        return subjectSet;
    }

    public void search() {
        if (null != searchName && searchName.trim().length() > 0) {
            searchedSubjectList.clear();
            getSubjectList(1).forEach((Subject subject) -> {
                if (subject.getName().contains(searchName)) {
                    searchedSubjectList.add(subject);
                }
            });
            pageOperation.refreshData(searchedSubjectList);
        }
        mainXhtml.setPageName(this.listpage);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public void setSelected(Subject newSubject) {
        oldSubject = getSelected();
        if (newSubject != null || null != newSubject.getId()) {//传过来的不为空
            if (oldSubject.getId() == null) {//原来是空
                renewed = true;
            } else {//原来的不是空
                if (!oldSubject.getId().equals(newSubject.getId())) {
                    renewed = true;
                } else {
                    renewed = false;
                }
            }
        } else {//要置空
            if (oldSubject.getId() == null) {//原来也是空
                renewed = false;
            } else {//原来的不是空
                renewed = true;
            }
        }
        this.current = newSubject;
    }

    public boolean isSelectedNull() {
        return null == getSelected().getId();
    }

    public void update() {
        majorsubjectController.completeMajorSelection();
        edgeamongSubjectController.completePreSubjectSelection();
        applicationSubjectController.edit(current);
        this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
        mainXhtml.setPageName(this.viewerpage);
    }

    //------------------prepare View, edit, list,create------------------
    public void prepareList() {
        pageOperation.refreshData(getSubjectList(1));
        mainXhtml.setPageName(this.listpage);
        applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.SUBJECT);
    }

    public void prepareView() {
        applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.SUBJECT);
        setSelected((Subject) (getItems().getRowData()));
        refresh();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewerpage);
    }

    public void prepareCreate() {
        applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.SUBJECT);
        setSelected(new Subject());
        refresh();
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
    }

    public void create() {
        try {
            //1. check whether the name existed? If true, deny the operation;else continue;
            if (null == applicationSubjectController.findByName(current.getName())) {
                applicationSubjectController.create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                //current = (Subject) (getFacade().getQueryResultList("select * from Subject where  name='" + current.getName().trim() + "'").get(0));
                //getSubjectList().add(current);
                pageOperation.refreshData(getSubjectList(1));
                this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
                mainXhtml.setPageName(this.editpage);
            } else {
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
                prepareCreate();
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Subject 1");
        }
    }

    public void prepareEdit() {
        try {
            setSelected(((Subject) getItems().getRowData()));
            refresh();
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            mainXhtml.setPageName(this.editpage);
        } catch (Exception e) {//getItems().getRowData() return NoRowAvailableException
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            mainXhtml.setPageName(this.editpage);
        }
        applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.SUBJECT);
    }

    private void refresh() {
        selectedParentsSubjects = null;
        majorsubjectController.refresh();
    }
    //-------------------------------
    //================For the majorSubject ===============

    //For the major's subjects
    Calendar currentCalendar = Calendar.getInstance();

    public ArrayList<Subject> getSelectedSubjects4Major() {
//        if (studentController.getLoginStudent() != null) {//A null student must not be allowed to access major select
//            selectedSubjects4Major.clear();
//            //没有开设的课程或者不在当前学期的，都不能维护==============
//            List<Teacschoolsubject> tssList = applicationTeacschoolsubjectController.getTeacschoolsubject4School(studentController.getLoginStudent().getSchoolId());
//            tssList.forEach((teaSchSub) -> {
//                if (teaSchSub.getTotime().after(currentCalendar.getTime())) {
//                    selectedSubjects4Major.add(teaSchSub.getSubjectid());
//                }
//            });
//            //sort according to subject name
//            selectedSubjects4Major.sort(subjectComparator);
//            //===================================
//        } else {
//把上面的代码注释掉，对学生放开了权限，但需要增加审核环节
        if (null != majorController.getSelected().getId()) {
            selectedSubjects4Major.clear();//finish the initiation
            List<Majorsubject> list = applicationMajorsubjectController.getMajorsubjects4Major(majorController.getSelected());
            for (int i = 0; i < list.size(); i++) {
                selectedSubjects4Major.add(list.get(i).getSubjectid());
            }
            //sort according to subject name
            selectedSubjects4Major.sort(subjectComparator);
        }
        //}
        return selectedSubjects4Major;
    }
    
    Comparator<Subject> subjectComparator = (Subject o1, Subject o2) -> {
        if (null == o1 || null == o2 || null == o1.getName() || null == o2.getName()) {
            System.out.println("Null in subjects for " + majorController.getSelected().getName());
            return 0;
        } else {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };

    public void setSelectedSubjects4Major(ArrayList<Subject> selectedSubjects4Major) {
        this.selectedSubjects4Major = selectedSubjects4Major;
    }
    //======================

    public Set<Subject> getSubject4SchoolList(School school) {
        subjectForSchoolList.clear();
        if (null == school) {//可能是秘书登录
            if (applicationRoleinfoController.isSecreatary(teacherAdminController.getLogined())) {
                subjectForSchoolList.addAll(applicationSubjectController.getAllList());
            }
        } else {
            if (null != school.getId()) {
                List<Majorsubject> ms = applicationMajorsubjectController.getMajorsubjects4Major(school.getMajorid());
                for (int i = 0; i < ms.size(); i++) {
                    subjectForSchoolList.add(ms.get(i).getSubjectid());
                }
            } else {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Please")
                        + " " + commonSession.getResourceBound().getString("Select")
                        + " " + commonSession.getResourceBound().getString("School")
                );
            }
        }
        return subjectForSchoolList;
    }

    public Set<Subject> getSubject4TeacherOrStudent(TeacherAdmin teacher, Student student) {
        if (null != studentController.getLogined()) {//学生登录进来了
            student = studentController.getLogined();
        }
        subjectForSchoolList.clear();
        if (null != teacher) {//教师根据班级获得所讲授的课程
            Set<Teacschoolsubject> teacschoolsubjects = teacher.getTeacschoolsubjectSet();
            if (null != teacschoolsubjects) {//有课程
                if (null != schoolController.getSelected().getId()) {//只选择当前班级的且是该教师教过的课程
                    teacschoolsubjects.forEach(teacschoolsubject -> {
                        if (teacschoolsubject.getSchoolid().equals(schoolController.getSelected())) {
                            subjectForSchoolList.add(teacschoolsubject.getSubjectid());
                        }
                    });
                } else {//未选择班级，则把该教师所带的课程全部返回
                    teacschoolsubjects.forEach(teacschoolsubject -> {
                        subjectForSchoolList.add(teacschoolsubject.getSubjectid());
                    });
                }
            }
        } else if (null != student.getId() && null == teacher) {
            student.getSchoolId().getMajorid().getMajorsubjectSet().forEach(majorsubject -> {
                subjectForSchoolList.add(majorsubject.getSubjectid());
            });
        } else if (null != student.getId() && null != teacher) {//某个教师需要获得在某个学生的课程列表
            Set<Teacschoolsubject> teacschoolsubjects = teacher.getTeacschoolsubjectSet();
            for (Teacschoolsubject teacschoolsubject : teacschoolsubjects) {
                if (teacschoolsubject.getSchoolid().equals(student.getSchoolId())) {
                    subjectForSchoolList.add(teacschoolsubject.getSubjectid());
                }
            }
        }
        return subjectForSchoolList;
    }

    //================For the parents subject ===============
    //temKnowledges is those being selected in the manyListbox
    private HashSet<Subject> predcessorKnowledgeSet, selectedParentsSubjects = new HashSet<>();

    //获得前驱
    public HashSet<Subject> getPredcessorSubjects() {
        if (null == predcessorKnowledgeSet) {
            predcessorKnowledgeSet = new HashSet<>();
        }
        // List<Edgeamongknowledge> predcessorList = edgeamongknowledgeController.getEdgeamongknowledgeListByKnowledge(getSelected());
        if (getSelected().getId() != null) {
            Set<Edgeamongsubject> tem = getSelected().getSuccessorsubjectSet();
            Iterator it = tem.iterator();
            while (it.hasNext()) { //拿到了一条记录，在该记录中，knowledge作后继，因此，可以找到它的前驱
                predcessorKnowledgeSet.add(((Edgeamongsubject) (it.next())).getPredecessornode());
            }
        }
        return predcessorKnowledgeSet;
    }

    //获得被选中的先行课程
    public Set<Subject> getSelectedParentsSubjects() {
        if (null == selectedParentsSubjects) {
            selectedParentsSubjects = getPredcessorSubjects();
        }
        return selectedParentsSubjects;
    }

    public void setSelectedParentsSubjects(HashSet<Subject> subjectList) {
        selectedParentsSubjects = subjectList;
    }

    public void destroy() {
        current = (Subject) getItems().getRowData();
        //selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        mainXhtml.setPageName(this.listpage);
    }

    private void performDestroy() {
        try {
            if (null != applicationSubjectController.find(current.getId())) {
                moveResource(current, candidateSubject);
                applicationSubjectController.remove(current);
                refresh();
                //updateCurrentItem();
                subjectList.remove(current);
                pageOperation.refreshData(subjectList);
                this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
                setSelected(null);
            }
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Subject 2");
        }
    }

    private void moveResource(Subject from, Subject to) {
        Set<Majorsubject> majorsubjects = from.getMajorsubjectSet();
        majorsubjects.forEach((majSub) -> {
            majSub.setSubjectid(to);
            applicationMajorsubjectController.edit(majSub);
        });
        Set<Teacschoolsubject> teacschoolsubjects = from.getTeacschoolsubjectSet();
        teacschoolsubjects.forEach((teacherschoolsubject) -> {
            teacherschoolsubject.setSubjectid(to);
            applicationTeacschoolsubjectController.edit(teacherschoolsubject);
        });
        Set<Leadpoint> leadpoints = from.getLeadpointSet();
        leadpoints.forEach((leadpoint) -> {
            leadpoint.setSubjectid(to);
            applicationLeadpointController.edit(leadpoint);
        });
        Set<Edgeamongsubject> edgeamongsubjects = from.getPrecessorsubjectSet();
        edgeamongsubjects.forEach((edgeamongsubject) -> {
            if (edgeamongsubject.getPredecessornode().equals(from)) {
                edgeamongsubject.setPredecessornode(to);
                applicationEdgeamongSubjectController.edit(edgeamongsubject);
            } else if (edgeamongsubject.getSuccessornode().equals(from)) {
                edgeamongsubject.setSuccessornode(to);
                applicationEdgeamongSubjectController.edit(edgeamongsubject);
            }

        });
        edgeamongsubjects = from.getSuccessorsubjectSet();
        edgeamongsubjects.forEach((edgeamongsubject) -> {
            if (edgeamongsubject.getSuccessornode().equals(from)) {
                edgeamongsubject.setSuccessornode(to);
                applicationEdgeamongSubjectController.edit(edgeamongsubject);
            } else if (edgeamongsubject.getPredecessornode().equals(from)) {
                edgeamongsubject.setPredecessornode(to);
                applicationEdgeamongSubjectController.edit(edgeamongsubject);
            }
        });
        Set<Knowledge> knowledges = from.getKnowledgeSet();
        knowledges.forEach((knowledge) -> {
            knowledge.setSubjectId(to);
            applicationKnowledgeController.edit(knowledge);
        });
    }

//    private void updateCurrentItem() {
//        int count = applicationSubjectController.count();
//        if (selectedItemIndex >= count) {
//            // selected index cannot be bigger than number of items:
//            selectedItemIndex = count - 1;
//            // go to previous page if last page disappeared:
//            if (pageOperation.getPagination().getPageFirstItem() >= count) {
//                pageOperation.getPagination().previousPage();
//            }
//        }
//        if (selectedItemIndex >= 0) {
//            setSelected((Subject) applicationSubjectController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
//            refresh();
//        }
//    }
///
    //-----------------
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getSubjectList(1), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getSubjectList(1), true);
    }

    public boolean isSubjectSelected() {
        return null == getSelected().getId();
    }
    //=================检查课程是否发生了变化 
    Subject oldSubject;
    boolean renewed = false;

    public boolean isRenewed() {
        boolean result = renewed;
        renewed = false;//调用过以后，就不再是new了
        return result;
    }

    public List<Subject> getSelectedSubjectList() {
        return selectedSubjectList;
    }

    public void setSelectedSubjectList(List<Subject> selectedSubjectList) {
        this.selectedSubjectList = selectedSubjectList;
    }

    public Subject getCandidateSubject() {
        return candidateSubject;
    }

    public void setCandidateSubject(Subject candidateSubject) {
        this.candidateSubject = candidateSubject;
    }
    
    public String getSubjectName(){
        if(subjectController.getSelected().getName()==null){
            return commonSession.getResourceBound().getString("Please")
                    +commonSession.getResourceBound().getString("Select")
                    +commonSession.getResourceBound().getString("Subject");
        }else{
            return  subjectController.getSelected().getName()+":";
        }
    }
    
    
}
