package modelController.sessionController;

import entities.Knowledge;
import entities.Question;
import entities.TeacherAdmin;
import entities.Teacschoolsubject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.applicationController.UserType;
import modelController.viewerController.MainXhtml;
import org.jasypt.util.password.StrongPasswordEncryptor;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("teacherAdminController")
@SessionScoped
public class TeacherAdminController extends CommonModelController<TeacherAdmin> implements Serializable {

    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private TeacherAdmin loginTeacher;
    private final List<TeacherAdmin> teacherList = new LinkedList<>();
    private String searchName;
    private final String tableName = "teacher", listpage = "teacher/List", editpage = "teacher/Edit",
            viewpage = "teacher/View", createpage = "teacher/Create", changePassword = "teacherinfo/teacherChangePassword", techinfo = "teacherinfo/teacherPersonalInformation";
    @Inject
    private TeachermajorController teachermajorController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.MajorController applicationMajorController;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.applicationController.TeacherAdminController applicationTeacherAdminController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.applicationController.TeacschoolsubjectController applicationTeacschoolsubjectController;
    TeacherAdmin newTeacherAdmin;
    protected TeacherAdmin current;

    public TeacherAdmin getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new TeacherAdmin();
        }
        return current;
    }

    public void setSelected(TeacherAdmin teacherAdmin) {
        current = teacherAdmin;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(applicationTeacherAdminController.getAllList());
    }

    public void create() {
        try {
            if (null == applicationTeacherAdminController.findByName(current.getName())) {
                current.setPassword(StaticFields.encrypt(current.getPassword()));
                current.setRoleId(applicationRoleinfoController.getTeacherRoleinfo());
                applicationTeacherAdminController.create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                //current = (TeacherAdmin) (getFacade().getQueryResultList("select * from major where  name='" + current.getName().trim() + "'").get(0));
                // applicationTeacherAdminController.getTeacheradminList().add(current);
                // applicationTeacherAdminController.refreshTeacheradminList();
                pageOperation.refreshData(applicationTeacherAdminController.getAllList());
                this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 1");
        }
    }

    public void prepareList() {
        pageOperation.refreshData(applicationTeacherAdminController.getAllList());
        mainXhtml.setPageName(listpage);
    }

    public void prepareCreate() {
        setSelected(new TeacherAdmin());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
        applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.TEACHER);
    }

    public void prepareView() {
        setSelected((TeacherAdmin) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(viewpage);
    }

    public void prepareEdit() {
        try {
            setSelected((TeacherAdmin) getItems().getRowData());
            applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.TEACHER);
            selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        } catch (Exception e) {

        }
        mainXhtml.setPageName(editpage);
    }

    public String update() {
        try {
            applicationMajorController.setMajorRelatedEntity(modelController.applicationController.MajorController.MajorRelatedEntity.TEACHER);
            teachermajorController.completeMajorSelection();
            applicationTeacherAdminController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
            return null;
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 2");
            return null;
        }
    }

    public void destroy() {
        current = (TeacherAdmin) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        try {
            applicationTeacherAdminController.remove(current);
            updateCurrentItem();
            pageOperation.refreshData(applicationTeacherAdminController.getAllList());
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationTeacherAdminController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((TeacherAdmin) applicationTeacherAdminController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationTeacherAdminController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationTeacherAdminController.getAllList(), true);
    }

    public TeacherAdmin getLogined() {
        return loginTeacher;
    }

    public boolean isTeacherLogined() {
        return null != loginTeacher;
    }

    public void setLogined(TeacherAdmin loginTeacherAdmin) {
        this.loginTeacher = loginTeacherAdmin;
    }

    public List<TeacherAdmin> getTeacherList() {
        String conditonString = "";
        if (null != searchName && searchName.trim().length() > 0) {
            conditonString = "where  locate ('" + searchName.toLowerCase() + "',LOWER(name))>0";
        }
        if (teacherList.isEmpty()) {
            List<TeacherAdmin> tem = applicationTeacherAdminController.getQueryResultList("select * from Teacher_Admin " + conditonString + " order by name");
            tem.forEach(e -> {
                teacherList.add((TeacherAdmin) e);
            });
        }
        return teacherList;
    }

    //-------------------------------For the search and viewall command button------------------
    public void search() {
        if (null != searchName && searchName.trim().length() > 0) {
            pageOperation.refreshData(getTeacherList());
        }
        mainXhtml.setPageName(this.listpage);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    String updatedPassword1, oldPassword;

    public String getUpdatedPassword1() {
        return updatedPassword1;
    }

    public void setUpdatedPassword1(String updatedPassword1) {
        this.updatedPassword1 = updatedPassword1;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void naviChangePassword() {
        mainXhtml.setPageName(changePassword);
    }

    public void exeChangePassword() {
        //validate whether the old password is correct
        boolean oldCorrect = new StrongPasswordEncryptor().checkPassword(getOldPassword(), loginTeacher.getPassword());
        //If true
        if (oldCorrect) {
            loginTeacher.setPassword(StaticFields.encrypt(getUpdatedPassword1()));
            applicationTeacherAdminController.edit(loginTeacher);
            mainXhtml.setPageName(this.techinfo);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } //update  the password
        //Else
        else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 4");
        }
    }
    //==========================Teacher Persoanl update==========
    boolean canUpdate = true;

    public String personalUpdate() {
        try {
            applicationTeacherAdminController.edit(loginTeacher);
            canUpdate = false;
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(loginTeacher.getName(), tableName, StaticFields.OPERATIONUPDATE);
            return null;
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 5");
            return null;
        }
    }

    public boolean getCanUpdate() {
        return canUpdate;
    }

    public String setUpdate() {
        canUpdate = true;
        return null;
    }

    public TeacherAdmin getWhom2Examin(UserType userType, Question question, Knowledge knowledge) {
        Teacschoolsubject teacschoolsubject = null;
        Calendar calendar = Calendar.getInstance();
        String currentTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
        switch (userType) {
            case Student:
                if (null != knowledge) {
                    teacschoolsubject = applicationTeacschoolsubjectController.getTeacschoolsubject(null, studentController.getLogined().getSchoolId(), knowledge.getSubjectId(), currentTime);
                } else if (null != question) {
                    teacschoolsubject = applicationTeacschoolsubjectController.getTeacschoolsubject(null, studentController.getLogined().getSchoolId(), question.getKnowledgeId().getSubjectId(), currentTime);
                }
                break;
            case Teacher:
                if (null != knowledge) {
                    teacschoolsubject = applicationTeacschoolsubjectController.getTeacschoolsubject(null, null, knowledge.getSubjectId(), currentTime);
                } else if (null != question) {
                    teacschoolsubject = applicationTeacschoolsubjectController.getTeacschoolsubject(null, null, question.getKnowledgeId().getSubjectId(), currentTime);
                }
                break;
            case Parent:
                if (null != knowledge) {
                    teacschoolsubject = applicationTeacschoolsubjectController.getTeacschoolsubject(null, studentController.getSelected().getSchoolId(), knowledge.getSubjectId(), currentTime);
                } else if (null != question) {
                    teacschoolsubject = applicationTeacschoolsubjectController.getTeacschoolsubject(null, studentController.getSelected().getSchoolId(), question.getKnowledgeId().getSubjectId(), currentTime);
                }
                break;
        }
        if (null != teacschoolsubject) {
            return teacschoolsubject.getTeacherid();
        } else {
            return applicationTeacherAdminController.getSecretary();
        }
    }

}
