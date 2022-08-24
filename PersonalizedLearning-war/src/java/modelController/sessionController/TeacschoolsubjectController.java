package modelController.sessionController;

import entities.School;
import entities.Subject;
import entities.TeacherAdmin;
import entities.Teacschoolsubject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("teacschoolsubjectController")
@SessionScoped
public class TeacschoolsubjectController extends CommonModelController<Teacschoolsubject> implements Serializable {

    @Inject
    private TestpaperController testpaperController;
    @Inject
    private SchoolController schoolController;
    @Inject
    private SubjectController subjectController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private RoleinfoController roleinfoController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.TeacschoolsubjectController applicationTeacschoolsubjectController;
    @Inject
    private tools.UserMessagor userMessagor;
    private List<Teacschoolsubject> teacschoolsubjectList = new LinkedList<>();
    private Date fromTimeCalendar;
    private final String tableName = "teacschoolsubject",
            editpage = "teacherSchoolSubject/Edit",
            listpage = "teacherSchoolSubject/List",
            viewpage = "teacherSchoolSubject/View",
            createpage = "teacherSchoolSubject/Create";

    public TeacschoolsubjectController() {
    }
    protected Teacschoolsubject current;

    public Teacschoolsubject getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Teacschoolsubject();
        }
        return current;
    }

    public void setSelected(Teacschoolsubject current) {
        this.current = current;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(getTeacschoolsubjectList());
    }

//Get the school list that the teacher teaches in this semester
    HashMap<School, HashSet<Subject>> schoolSubjectMap4LoginedTeac;
//Get all the subjects that the teacher have in this school in this semester

    private HashMap<School, HashSet<Subject>> getCurrentSchool4TeacherMap() {
        if (null == schoolSubjectMap4LoginedTeac || schoolSubjectMap4LoginedTeac.isEmpty()) {
            List<Teacschoolsubject> temTeacschoolsubjects
                    = applicationTeacschoolsubjectController.getTeacschoolsubject4Teacher(teacherAdminController.getLogined());
            schoolSubjectMap4LoginedTeac = new HashMap<>();
            for (Teacschoolsubject tss : temTeacschoolsubjects) {
                if (tss.getTotime().after(Calendar.getInstance().getTime())) {
                    if (null == schoolSubjectMap4LoginedTeac.get(tss.getSchoolid())) {
                        schoolSubjectMap4LoginedTeac.put(tss.getSchoolid(), new HashSet<>());
                    }
                    schoolSubjectMap4LoginedTeac.get(tss.getSchoolid()).add(tss.getSubjectid());
                }
            }
        }
        return schoolSubjectMap4LoginedTeac;
    }

    public List<School> getCurrentSchool4Teacher() {
        if (getCurrentSchool4TeacherMap().keySet().isEmpty()) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Please")
                    + commonSession.getResourceBound().getString("AskSecretary"));
        }
        return new LinkedList<>(getCurrentSchool4TeacherMap().keySet());
    }

    public List<Subject> getCurrentSubject4Teacher() {
        if (null == schoolController.getSelected() || schoolController.getSelected().getId() == null) {
            return new LinkedList<>();
        }
        HashSet tem = new HashSet();
        if (null != getCurrentSchool4TeacherMap()) {
            tem = getCurrentSchool4TeacherMap().get(schoolController.getSelected());
        }
        if (null != tem) {
            return new LinkedList<>(tem);
        } else {
            return new LinkedList<>();
        }
    }

    public String search() {
        //Assign value to teacschoolsubjectList;
        //Get the condition of where
        String conditionString = "";
        if (teacherAdminController.getSelected().getId() != null) {
            conditionString += " teacherid=" + teacherAdminController.getSelected().getId();
        }
        if (schoolController.getSelected().getId() != null) {
            if (conditionString.trim().length() > 0) {
                conditionString += " and schoolid=" + schoolController.getSelected().getId();
            } else {
                conditionString += " schoolid=" + schoolController.getSelected().getId();
            }
        }
        if (subjectController.getSelected().getId() != null) {
            if (conditionString.trim().length() > 0) {
                conditionString += " and subjectid=" + subjectController.getSelected().getId();
            } else {
                conditionString += " subjectid=" + subjectController.getSelected().getId();
            }
        }
        if (null != fromTimeCalendar) {
            Calendar temCalendar = Calendar.getInstance();
            temCalendar.setTime(fromTimeCalendar);
            String dateString = temCalendar.get(Calendar.YEAR) + "-" + (temCalendar.get(Calendar.MONTH) + 1) + "-" + temCalendar.get(Calendar.DAY_OF_MONTH);
            if (conditionString.trim().length() > 0) {
                conditionString += " and fromTime >'" + dateString + "'";
            } else {
                conditionString += " fromTime >'" + dateString + "'";
            }
        }
        teacschoolsubjectList.clear();
        List<Teacschoolsubject> tem = null;
        if (conditionString.trim().length() > 0) {
            tem = applicationTeacschoolsubjectController.getQueryResultList("select * from teacschoolsubject where " + conditionString + " order by totime desc");
        } else {
            tem = applicationTeacschoolsubjectController.getQueryResultList("select * from teacschoolsubject");
        }
        tem.forEach(e -> {
            teacschoolsubjectList.add((Teacschoolsubject) e);
        });
        pageOperation.refreshData(teacschoolsubjectList);
        return null;
    }

//------------------prepare View, edit, list,create------------------
    public void prepareList() {
        pageOperation.refreshData(getTeacschoolsubjectList());
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareCreate() {
        setSelected(new Teacschoolsubject());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
    }

    public void create() {
        try {
//            for (Teacschoolsubject tss : teacherAdminController.getSelected().getTeacschoolsubjectSet()) {
//                if (tss.getSchoolid().equals(schoolController.getSelected())) {//这个教师还过这个班的这门课！！！
//                    if (testpaperController.getSubjectList4Testpaper().contains(tss.getSubjectid())) {//有这门课的考试安排
//                        testpaperController.getSubjectList4Testpaper().remove(tss.getSubjectid());//删除
//                        break;
//                    }
//                }
//            }
//            if (!testpaperController.getSubjectList4Testpaper().isEmpty()) {
            for (Subject subject : subjectController.getSelectedSubjectList()) {
                current.setSubjectid(subject);
                current.setSchoolid(schoolController.getSelected());
                current.setTeacherid(teacherAdminController.getSelected());
                applicationTeacschoolsubjectController.create(current);
                teacherAdminController.getSelected().getTeacschoolsubjectSet().add(current);
                this.logs(current.getTeacherid().getName() + "-" + current.getSchoolid().getName()
                        + "-" + current.getSubjectid().getName(), tableName, StaticFields.OPERATIONINSERT);
            }
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(listpage);
//            } else {
//                userMessagor.addMessage(current.getTeacherid().getName() + ":" + current.getSchoolid().getName() + ":" + current.getSubjectid().getName()
//                        + commonSession.getResourceBound().getString("Already")
//                        + commonSession.getResourceBound().getString("Exist"));
//            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller subject teacher school 3");
        }
    }

    public void update() {
        try {
            applicationTeacschoolsubjectController.edit(current);
            //evictForeignKey();
            pageOperation.refreshData(getTeacschoolsubjectList());
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(this.viewpage);
            this.logs(current.getTeacherid().getName() + "-" + current.getSchoolid().getName()
                    + "-" + current.getSubjectid().getName(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller subject teacher school 1");
        }
    }

    public void prepareEdit() {
        try {
            setSelected(((Teacschoolsubject) getItems().getRowData()));
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            mainXhtml.setPageName(this.editpage);
        } catch (Exception e) {//getItems().getRowData() return NoRowAvailableException
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            mainXhtml.setPageName(this.editpage);
        }
    }

    public void destroy() {
        setSelected((Teacschoolsubject) getItems().getRowData());
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareView() {
        setSelected((Teacschoolsubject) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
    }

    private void performDestroy() {
        try {
            applicationTeacschoolsubjectController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //teacschoolsubjectList.remove(current);
            //evictForeignKey();
            pageOperation.refreshData(getTeacschoolsubjectList());
            this.logs(current.getTeacherid().getName() + "-" + current.getSchoolid().getName()
                    + "-" + current.getSubjectid().getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller subject teacher school 2");
        }
    }

    private void updateCurrentItem() {
        int count = applicationTeacschoolsubjectController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((Teacschoolsubject) applicationTeacschoolsubjectController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public List<Teacschoolsubject> getTeacschoolsubjectList() {
        search();
        return teacschoolsubjectList;
    }

    public void refreshTeacschoolsubjectList() {
        teacschoolsubjectList.clear();
    }

    public Date getFromTimeCalendar() {
        return fromTimeCalendar;
    }

    public void setFromTimeCalendar(Date fromTimeCalendar) {
        this.fromTimeCalendar = fromTimeCalendar;
    }

    @Override
    public DataModel getItems() {
        if (null == pageOperation.getItems()) {
            if (roleinfoController.isStudent()) {
                pageOperation.refreshData(new LinkedList(studentController.getLogined().getSchoolId().getTeacschoolsubjectSet()));
            }
            if (roleinfoController.isSecretary()) {//需要搜索才能得到
            }
            if (roleinfoController.isTeacher()) {
                pageOperation.refreshData(new LinkedList(teacherAdminController.getLogined().getTeacschoolsubjectSet()));
            }
            if (roleinfoController.isParent()) {
                pageOperation.refreshData(new LinkedList(studentController.getSelected().getSchoolId().getTeacschoolsubjectSet()));
            }
        }
        return pageOperation.getItems();
    }
    
    // 查询当前session下的老师所教的当前选中课程的班级
    public List<School> getClasses4TeacherSubject() {
        if(null == subjectController.getSelected().getId()){
//            没有选中任何课程时返回null
            return null;
        }else{
//            获取当前session下的老师ID
            TeacherAdmin teacherAdmin = (TeacherAdmin) commonSession.getUser();
            int teacherId = teacherAdmin.getId();
//            构造SQL语句，查询在当前session下的老师教的当前选中课程的所有班级
            String sql = "SELECT * FROM TEACSCHOOLSUBJECT WHERE TEACHERID=" + teacherId +" AND SUBJECTID=" + subjectController.getSelected().getId();
//            存储查询结果
            List<Teacschoolsubject> teacschoolsubjects = applicationTeacschoolsubjectController.getQueryResultList(sql);
//            遍历查询结果，将所有班级的ID记录在ret中
            List<School> ret = new ArrayList<>();
            teacschoolsubjects.forEach((tss) -> {
                ret.add(tss.getSchoolid());
            });
            return ret;  
        }
    }
}
