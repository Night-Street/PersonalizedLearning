package modelController.sessionController;

import entities.Student;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import modelController.viewerController.MainXhtml;
import org.jasypt.util.password.StrongPasswordEncryptor;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("studentController")
@SessionScoped
public class StudentController extends CommonModelController<Student> implements Serializable {

    @Inject
    SchoolController schoolController;
    @Inject
    RoleinfoController roleinfoController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private modelController.applicationController.TeacherAdminController applicationTeacherAdminController;
    @Inject
    private tools.UserMessagor userMessagor;
    private String searchName;
    private Student current;
    private Student loginStudent;
    private String oldPassword, updatedPassword1;
    private List<Student> searchedStudentList = new LinkedList<>();
    private final String tableName = "student", listpage = "student/List", editpage = "student/Edit",
            viewpage = "student/View", batchStudent = "student/createBatch", singleStudent = "student/createOne";
    private Set<Student> selectedSchoolStudents;//补考时，只选择部分学生
    private Integer praiseTotal = 0;

    public Student getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Student();
        }
        return current;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(searchedStudentList);
    }

    public Integer getPraiseTotal() {
        return praiseTotal;
    }

    public void setPraiseTotal(Integer praiseTotal) {
        this.praiseTotal = praiseTotal;
    }

    //-------------------------------For the search and viewall command button------------------
    public void search() {
        if (null != searchName && searchName.trim().length() > 0) {
            String schoolCondstring = "", searchCondString = "";
            if (schoolController.getSelected().getId() != null) {
                schoolCondstring = " school_id=" + schoolController.getSelected().getId();
            }
            if (null != searchName && searchName.trim().length() > 0) {
                searchCondString = " locate('" + searchName.trim() + "',name)>0 or "
                        + " locate('" + searchName.trim().toLowerCase() + "',LOWER(FIRSTNAME))>0 or "
                        + " locate('" + searchName.trim().toLowerCase() + "',LOWER(SECONDNAME))>0 or "
                        + " locate('" + searchName.trim().toLowerCase() + "',LOWER(EMAIL))>0 or "
                        + " locate('" + searchName.trim().toLowerCase() + "',LOWER(PHONE))>0";
            }
            //================compose cases
            int con = 0;
            if (schoolCondstring.trim().length() == 0 && searchCondString.trim().length() == 0) {
                con = 0;//nothing to search
            } else {
                if (schoolCondstring.trim().length() > 0 & searchCondString.trim().length() == 0) {
                    con = 1;//Only school condition
                } else {
                    if (schoolCondstring.trim().length() == 0 & searchCondString.trim().length() > 0) {
                        con = 2;//Only name conditon
                    } else {
                        con = 3;//both condition
                    }
                }
            }

            //===================conditionString===============
            String condtionString = "";
            switch (con) {
                case 0:
                    break;
                case 1:
                    condtionString = schoolCondstring;
                    break;
                case 2:
                    condtionString = searchCondString;
                    break;
                case 3:
                    condtionString = schoolCondstring + " and " + searchCondString;
                    break;
            }
            if (condtionString.trim().length() > 0) {
                searchedStudentList.clear();
                List<Student> tem = applicationStudentController.getQueryResultList("select * from student where " + condtionString);
                tem.forEach(e -> {
                    searchedStudentList.add((Student) e);
                });
            }
            pageOperation.refreshData(searchedStudentList);
        }
        mainXhtml.setPageName(this.listpage);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
//------------------prepare View, edit, list,create------------------

    public StudentController() {
    }

    public void setSelected(Student current) {
        this.current = current;
    }

//-------------------------------
    public void prepareList() {
        pageOperation.refreshData(applicationStudentController.getStudentList(schoolController.getSelected()));
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareView() {
        setSelected((Student) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
    }

    public String prepareCreate() {
        setSelected(new Student());
        selectedItemIndex = -1;
        return null;
    }

    public void create() {
        try {
            //1. check whether the name existed? If true, deny the operation;else continue;
            if (null == applicationStudentController.findByName(current.getName())) {
                current.setSchoolId(schoolController.getSelected());
                current.setRoleId(applicationRoleinfoController.getStudentRoleinfo());
                current.setPassword(StaticFields.encrypt(current.getPassword()));
                applicationStudentController.create(current);
                refreshStudentList();
                // schoolController.getSelected().getStudentSet().add(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                //current = (Student) (getFacade().getQueryResultList("select * from student where  name='" + current.getName().trim() + "'").get(0));
                pageOperation.refreshData(applicationStudentController.getStudentList(schoolController.getSelected()));
                this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
                mainXhtml.setPageName(this.viewpage);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student 1");
        }
    }

    public void prepareEdit() {
        setSelected(((Student) getItems().getRowData()));
        mainXhtml.setPageName(this.editpage);
    }

    public void update() {
        try {
            current.setRoleId(applicationRoleinfoController.getStudentRoleinfo());
            applicationStudentController.edit(current);
            refreshStudentList();
            pageOperation.refreshData(applicationStudentController.getStudentList(schoolController.getSelected()));
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student 2");
        }
    }

    public String destroy() {
        current = (Student) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        return null;
    }

    private void performDestroy() {
        try {
            applicationStudentController.remove(current);
            refreshStudentList();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //searchedStudentList.remove(current);
            pageOperation.refreshData(searchedStudentList);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationStudentController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((Student) applicationStudentController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public void refreshStudentList() {
        searchedStudentList = null;
    }

    //-----------------
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationStudentController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationStudentController.getAllList(), true);
    }

    public Student getLogined() {
        return loginStudent;
    }

    public boolean isStudentLogined() {
        return null != loginStudent;
    }

    public void setLoginStudent(Student loginStudent) {
        this.loginStudent = loginStudent;
    }
    //----------------File upload deal with--------------------
    private Part studentExcelFile;

    public Part getStudentExcelFile() {
        return studentExcelFile;
    }

    public void setStudentExcelFile(Part studentExcelFile) {
        this.studentExcelFile = studentExcelFile;
    }

    public void studentsUpload() {
        int i = 0;
        try {
            if (null == schoolController.getSelected().getId()) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Select") + commonSession.getResourceBound().getString("School"));
            }
            if (null == this.studentExcelFile || this.getFilename(studentExcelFile).trim().length() == 0) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("ExcelSelection"));
            }
            InputStream ins = studentExcelFile.getInputStream();
            Workbook book = Workbook.getWorkbook(ins);
            Sheet sheet = book.getSheet(0);
            int columnum = sheet.getColumns();//得到列数  
            int rownum = sheet.getRows();//得到行数
            if (columnum != StaticFields.COLUMNNUM3 && columnum != StaticFields.COLUMNNUM5) {
                userMessagor.addMessage(MessageFormat.format(commonSession.getResourceBound().getString("ExcelWrong1"), String.valueOf(StaticFields.COLUMNNUM3)));
            } else {
                try {
                    for (; i < rownum; i++) {
                        if (applicationStudentController.getQueryResultList("select * from student where name='" + sheet.getCell(0, i).getContents().trim() + "'").isEmpty()) {
                            Student student = new Student();
                            student.setName(sheet.getCell(0, i).getContents().trim());
                            String temName = sheet.getCell(1, i).getContents().trim();
                            student.setSecondname(temName.substring(0, 1));
                            student.setFirstname(temName.substring(1));
                            student.setPassword(StaticFields.encrypt(sheet.getCell(2, i).getContents().trim()));
                            if (columnum > StaticFields.COLUMNNUM5) {
                                student.setEmail(sheet.getCell(3, i).getContents().trim());
                                student.setPhone(sheet.getCell(4, i).getContents().trim());
                            }
                            student.setSchoolId(schoolController.getSelected());
                            student.setRoleId(applicationRoleinfoController.getStudentRoleinfo());
                            applicationStudentController.create(student);
                        } else {
                            userMessagor.addMessage(sheet.getCell(0, i).getContents() + " " + commonSession.getResourceBound().getString("Exist"));
                        }
                    }

                } catch (Exception e) {
                    i = -1;
                } finally {
                    book.close();
                }
            }
        } catch (BiffException | IOException ex) {
            i = -2;
        }
        switch (i) {
            case -2:
                userMessagor.addMessage(commonSession.getResourceBound().getString("ExcelWrong3"));
            case -1:
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("ExcelWrong2"));
                break;
            case 0:
                userMessagor.addMessage(commonSession.getResourceBound().getString("Import") + commonSession.getResourceBound().getString("Empty"));
                break;
            default:
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed") + " " + i + commonSession.getResourceBound().getString("Student"));
        }
        mainXhtml.setPageName(listpage);
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

    //==========================Student Persoanl update==========
    boolean canUpdate = true;

    public String personalUpdate() {
        try {
            applicationStudentController.edit(loginStudent);
            canUpdate = false;
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(loginStudent.getName(), tableName, StaticFields.OPERATIONUPDATE);
            return null;
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student 4");
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

    //补考时，只选择部分学生
    public Set<Student> getSelectedSchoolStudents() {
        return selectedSchoolStudents;
    }

    //补考时，只选择部分学生
    public void setSelectedSchoolStudents(Set<Student> selectedSchoolStudents) {
        this.selectedSchoolStudents = selectedSchoolStudents;
    }

    private String templateOfStudent;

    public String getTemplateOfStudent() {
        if (null == templateOfStudent) {
            templateOfStudent = commonSession.getResourceBound().getString("Studentnumber")
                    + "\t" + commonSession.getResourceBound().getString("Account")
                    + "\t" + commonSession.getResourceBound().getString("Password")
                    + "\t" + commonSession.getResourceBound().getString("Email")
                    + "\t" + commonSession.getResourceBound().getString("Phone")
                    + "\t" + commonSession.getResourceBound().getString("Phonenumber");
        }
        return templateOfStudent;
    }

    public String getUpdatedPassword1() {
        return updatedPassword1;
    }

    public void setUpdatedPassword1(String updatedPassword1) {
        this.updatedPassword1 = updatedPassword1;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void resetPassword() {
        getSelected().setPassword(new StrongPasswordEncryptor().encryptPassword(commonSession.getResourceBound().getString("DefaultNumber")));
        applicationStudentController.edit(getSelected());
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        mainXhtml.setPageName(listpage);
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void naviChangePassword() {
        mainXhtml.setPageName("studentInfo/changePassword");
    }

    public void exeChangePassword() {
        //validate whether the old password is correct
        boolean oldCorrect = new StrongPasswordEncryptor().checkPassword(getOldPassword(), loginStudent.getPassword());
        //If true
        if (oldCorrect) {
            loginStudent.setPassword(StaticFields.encrypt(getUpdatedPassword1()));
            applicationStudentController.edit(loginStudent);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } //update  the password
        //Else
        else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Student 5");
        }
        mainXhtml.setPageName(this.editpage);
    }

    public void delSelectedStu() {
        setSelected(new Student());
    }

    public void getBatchStudent() {
        prepareCreate();
        mainXhtml.setPageName(this.batchStudent);
    }

    public void getSingleStudent() {
        prepareCreate();
        mainXhtml.setPageName(this.singleStudent);
    }

}
