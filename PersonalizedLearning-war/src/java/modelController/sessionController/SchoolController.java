package modelController.sessionController;

import entities.Parent;
import entities.School;
import entities.Student;
import entities.TeacherAdmin;
import entities.Teacschoolsubject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.MySwitch;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("schoolController")
@SessionScoped
public class SchoolController extends CommonModelController<School> implements Serializable {

    @Inject
    private modelController.applicationController.TeacschoolsubjectController applicationTeacschoolsubjectController;
    @Inject
    SubjectController subjectController;
    @Inject
    private modelController.applicationController.SchoolController applicationSchoolController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private tools.UserMessagor userMessagor;
    // private School selectedSchool;
    private final int dropDownNumber = 7;
    private School[] schools = new School[dropDownNumber];//The last one is the selected
    private List<MySwitch> schoolSwitchsList = new LinkedList<>();
    private String searchName;
    private List<School> searchedSchoolList;
    //private List<School> allSchools;
    private List<School> schoolList = null;
    private final String tableName = "school", listpage = "school/List", editpage = "school/Edit",
            viewpage = "school/View", createpage = "school/Create";
    @Inject
    private MainXhtml mainXhtml;

    public SchoolController() {
        //For the compose component
        for (int i = 0; i < dropDownNumber; i++) {
            schoolSwitchsList.add(new MySwitch(false));
            schools[i] = new School();
        }
        schoolSwitchsList.get(0).turnOn();
    }
    protected School current;

    public School getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new School();
        }
        return current;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(applicationSchoolController.getAllList());
    }

//---------------------------for School drop down cascade ---------------------------
    public int getDropDownNumber() {
        return dropDownNumber - 1;
    }

    public Set<School> getChildrens(int i) {
        if (i == 0) {
            return applicationSchoolController.getAllSet();
        }
        School tem = schools[i - 1];
        return tem.getSchoolSet();
    }

    public MySwitch getMySwitch(int i) {//如果前面关了，后继都要关；如果前面是打开的，后边是否打开要看他们的造化了
        if (i != 0) {
            if (schoolSwitchsList.get(i - 1).isStatus()) {//前驱开着,读父结点的状态
                if (null != schools[i - 1].getSchoolSet() && !schools[i - 1].getSchoolSet().isEmpty()) {//前驱有无后继？有
                    dealTurnOn(i);//具备打开条件 
                } else {//前驱无后继
                    dealTurnOff(i);
                }
            } else {
                dealTurnOff(i);
            }
        } else {//0是常开的
            setSelected(schools[i]);
        }
        return schoolSwitchsList.get(i);
    }

    private void dealTurnOn(int i) {
        schoolSwitchsList.get(i).turnOn();
        setSelected(schools[i]);
    }

    private void dealTurnOff(int i) {
        schoolSwitchsList.get(i).turnOff();
        schools[i] = new School();
        int tem = 0;
        for (int j = i - 1; j > 0; j--) {
            if (null != schools[j].getName()) {
                tem = j;
                break;
            }
        }
        setSelected(schools[tem]);
    }

    public School[] getSchools() {
        return schools;
    }
    
    public List<School> getSchools4Teacher(TeacherAdmin teacherAdmin){
        List<School> result=new ArrayList<>();
        teacherAdmin.getTeacschoolsubjectSet().forEach(teaschSub->{
            result.add(teaschSub.getSchoolid());
        });
        return result;
    }

    public void setSchools(School[] schools) {
        this.schools = schools;
    }

    //与模板<ezcomp:schoolStudent/>相关联
    private Set<School> cadidateSchools = new HashSet<>();

    public Set<School> getCadidateSchools() {
        return cadidateSchools;
    }

    public void setCadidateSchools(Set<School> cadidateSchools) {
        this.cadidateSchools = cadidateSchools;
    }

    //-------------------------------For the search and viewall command button------------------
    public String search() {
        if (null != searchName && searchName.trim().length() > 0) {
            searchedSchoolList = new LinkedList<>();
            applicationSchoolController.getAllSet().forEach((school) -> {
                // System.out.println(((School) school).getName()+searchName);
                if (((School) school).getName().contains(searchName)) {
                    searchedSchoolList.add((School) school);
                }
            });
            pageOperation.refreshData(searchedSchoolList);
        }
        return null;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSelected(School school) {
        current = school;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
//---------------------------Codes for the function of create, list, viewer and edit-----------------------------

//------------------prepare View, edit, list,create------------------
    public void prepareList() {
        pageOperation.refreshData(applicationSchoolController.getAllList());
        mainXhtml.setPageName(listpage);
    }

    public void prepareView() {
        setSelected((School) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(viewpage);
    }

    public Set<School> getCurrentSubjectSchools() {
        Set<School> resuSchools = new HashSet<>();
        Calendar today = Calendar.getInstance();
        if (applicationRoleinfoController.isAdminOrSecretary(teacherAdminController.getLogined())) {
            resuSchools = applicationSchoolController.getSchoolsOnLeaves();
        } else {
            Set<Teacschoolsubject> teaschsubSet = applicationTeacschoolsubjectController.getTeacschoolsubjects(teacherAdminController.getLogined(), today);
            for (Teacschoolsubject tss : teaschsubSet) {
                resuSchools.add(tss.getSchoolid());
            }
        }
        return resuSchools;
    }

    public void prepareCreate() {
        setSelected(new School());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
    }

    public void create() {
        try {
            //1. check whether the name existed? If true, deny the operation;else continue;
            if (null == applicationSchoolController.findByName(current.getName())) {
                applicationSchoolController.create(current);
                applicationSchoolController.getAllSet().add(current);
//                if (null != current.getMajorid() && null != current.getMajorid().getId()) {
//                    majorController.evict(current.getMajorid());
//                }
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                applicationSchoolController.setSchoolsOnLeaves(null);
                //current = (School) getFacade().getQueryResultList("select * from school where name='" + current.getName().trim() + "'").get(0);
                //applicationSchoolController.refresh();
                pageOperation.refreshData(applicationSchoolController.getAllList());
                this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller school 1");
        }
    }

    public void prepareEdit() {
        try {
            setSelected(((School) getItems().getRowData()));
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            mainXhtml.setPageName(editpage);
        } catch (Exception e) {//getItems().getRowData() return NoRowAvailableException
            selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
            mainXhtml.setPageName(editpage);
        }
    }

    public void update() {
        try {
            applicationSchoolController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            //applicationSchoolController.refresh();
            pageOperation.refreshData(applicationSchoolController.getAllList());
            // Necessary to refresh the schoolList, because it is in the list
            //pageOperation.setDataModelList(getSchoolList());
            mainXhtml.setPageName(viewpage);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller school 2");
        }
    }

    public void destroy() {
        current = (School) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        updateCurrentItem();
        setSelected(null);
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        try {
            applicationSchoolController.remove(current);
            // applicationSchoolController.refresh();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            //School is updated,so also update the variable in publicFields
            updateCurrentItem();
            pageOperation.refreshData(applicationSchoolController.getAllList());
            this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller school 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationSchoolController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((School) applicationSchoolController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }
    //-----------------

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationSchoolController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationSchoolController.getAllList(), true);
    }

    // 获取上这门课程的所有班级
//    public List<School> getAllClasses() {
//        if (null == subjectController.getSelected() || null == subjectController.getSelected().getId()) {
//            return null;
//        } else {
//            String temSQL = null;
//            List<School> classList = new ArrayList<>();
//            
////            temSQL = "select * from teacschoolsubject where teacherid=" + teacherAdminController.getLogined().getId()
////                    + subjectController.getSelected().getId() == null ? "" : " and subjectid="
////                    + subjectController.getSelected().getId();
//            
//            temSQL = "select * from teacschoolsubject where subjectid=" + subjectController.getSelected().getId();
//             
//            List<Teacschoolsubject> teacschoolsubjects = applicationTeacschoolsubjectController.getQueryResultList(temSQL);
//            for (Teacschoolsubject t : teacschoolsubjects) {
//                classList.add((t).getSchoolid());
//            }
//            return classList;
//        }
//
//    }
    
     // 查询该学生所在的班级
//     public List<School> getClass4Student() {
//         if(null == studentController.getSelected() || null == studentController.getSelected().getSchoolId()){
//             return null;
//         }else{        
//             String temSQL = null;
//            List<School> classList = new ArrayList<>();
//            School schoolId = studentController.getSelected().getSchoolId();
//            classList.add(schoolId);
//            return classList;
//         }
//    }

}
