package modelController.sessionController;

import entities.Parent;
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

@Named("parentController")
@SessionScoped
public class ParentController extends CommonModelController<Parent> implements Serializable {

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
    private modelController.applicationController.ParentController applicationParentController;
    @Inject
    private modelController.applicationController.TeacherAdminController applicationTeacherAdminController;
    @Inject
    private tools.UserMessagor userMessagor;
    private String searchName;
    private Parent current;
    private Parent loginParent;
    private String oldPassword, updatedPassword1;
    private List<Parent> searchedParentList = new LinkedList<>();
    private final String tableName = "parent", listpage = "parent/List", editpage = "parent/Edit",
            viewpage = "parent/View", batchParent = "parent/createBatch", singleParent = "parent/createOne";
    private Set<Parent> selectedSchoolParents;//补考时，只选择部分学生
    private Integer praiseTotal = 0;

    public Parent getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Parent();
        }
        return current;
    }
    public void setDataModelList() {
        pageOperation.setDataModelList(searchedParentList);
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
                searchedParentList.clear();
                List<Parent> tem = applicationParentController.getQueryResultList("select * from parent where " + condtionString);
                tem.forEach(e -> {
                    searchedParentList.add((Parent) e);
                });
            }
            pageOperation.refreshData(searchedParentList);
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

    public ParentController() {
    }

    public void setSelected(Parent current) {
        this.current = current;
    }

//-------------------------------
    public void prepareList() {
        mainXhtml.setPageName(this.listpage);
    }

    public void prepareView() {
        setSelected( (Parent) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(this.viewpage);
    }

    public String prepareCreate() {
        setSelected( new Parent());
        selectedItemIndex = -1;
        return null;
    }

    public void create() {
        try {
            //1. check whether the name existed? If true, deny the operation;else continue;
            if (null == applicationParentController.findByName(current.getName())) {
                current.setRoleId(applicationRoleinfoController.getParentRoleinfo());
                current.setPassword(StaticFields.encrypt(current.getPassword()));
                applicationParentController.create(current);
                refreshParentList();
                // schoolController.getSelected().getParentSet().add(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                //current = (Parent) (getFacade().getQueryResultList("select * from parent where  name='" + current.getName().trim() + "'").get(0));
                  this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
                mainXhtml.setPageName(this.viewpage);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Parent 1");
        }
    }

    public void prepareEdit() {
        setSelected( ((Parent) getItems().getRowData()));
        mainXhtml.setPageName(this.editpage);
    }

    public void update() {
        try {
            current.setRoleId(applicationRoleinfoController.getParentRoleinfo());
            applicationParentController.edit(current);
            refreshParentList();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(viewpage);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Parent 2");
        }
    }

    public String destroy() {
        current = (Parent) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        return null;
    }

    private void performDestroy() {
        try {
            applicationParentController.remove(current);
            refreshParentList();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //searchedParentList.remove(current);
            pageOperation.refreshData(searchedParentList);
            this.logs(current.getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Parent 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationParentController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (Parent) applicationParentController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public void refreshParentList() {
        searchedParentList = null;
    }

    //-----------------
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationParentController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationParentController.getAllList(), true);
    }

    public Parent getLogined() {
        return loginParent;
    }

    public void setLoginParent(Parent loginParent) {
        this.loginParent = loginParent;
    }
    //----------------File upload deal with--------------------
    private Part parentExcelFile;

    public Part getParentExcelFile() {
        return parentExcelFile;
    }

    public void setParentExcelFile(Part parentExcelFile) {
        this.parentExcelFile = parentExcelFile;
    }

    public void parentsUpload() {
        int i = 0;
        try {
            if (null == schoolController.getSelected().getId()) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Select") + commonSession.getResourceBound().getString("School"));
            }
            if (null == this.parentExcelFile || this.getFilename(parentExcelFile).trim().length() == 0) {
                userMessagor.addMessage(commonSession.getResourceBound().getString("ExcelSelection"));
            }
            InputStream ins = parentExcelFile.getInputStream();
            Workbook book = Workbook.getWorkbook(ins);
            Sheet sheet = book.getSheet(0);
            int columnum = sheet.getColumns();//得到列数  
            int rownum = sheet.getRows();//得到行数
            if (columnum != StaticFields.COLUMNNUM3 && columnum != StaticFields.COLUMNNUM5) {
                userMessagor.addMessage(MessageFormat.format(commonSession.getResourceBound().getString("ExcelWrong1"), String.valueOf(StaticFields.COLUMNNUM3)));
            } else {
                try {
                    for (; i < rownum; i++) {
                        if (applicationParentController.getQueryResultList("select * from parent where name='" + sheet.getCell(0, i).getContents().trim() + "'").isEmpty()) {
                            Parent parent = new Parent();
                            parent.setName(sheet.getCell(0, i).getContents().trim());
                            String temName = sheet.getCell(1, i).getContents().trim();
                            parent.setSecondname(temName.substring(0, 1));
                            parent.setFirstname(temName.substring(1));
                            parent.setPassword(StaticFields.encrypt(sheet.getCell(2, i).getContents().trim()));
                            if (columnum > StaticFields.COLUMNNUM5) {
                                parent.setEmail(sheet.getCell(3, i).getContents().trim());
                                parent.setPhone(sheet.getCell(4, i).getContents().trim());
                            }
                            parent.setRoleId(applicationRoleinfoController.getParentRoleinfo());
                            applicationParentController.create(parent);
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
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed") + " " + i + commonSession.getResourceBound().getString("Parent"));
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

    //==========================Parent Persoanl update==========
    boolean canUpdate = true;

    public String personalUpdate() {
        try {
            applicationParentController.edit(loginParent);
            canUpdate = false;
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(loginParent.getName(), tableName, StaticFields.OPERATIONUPDATE);
            return null;
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Parent 4");
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
    public Set<Parent> getSelectedSchoolParents() {
        return selectedSchoolParents;
    }

    //补考时，只选择部分学生
    public void setSelectedSchoolParents(Set<Parent> selectedSchoolParents) {
        this.selectedSchoolParents = selectedSchoolParents;
    }

    private String templateOfParent;

    public String getTemplateOfParent() {
        if (null == templateOfParent) {
            templateOfParent = commonSession.getResourceBound().getString("Parentnumber")
                    + "\t" + commonSession.getResourceBound().getString("Account")
                    + "\t" + commonSession.getResourceBound().getString("Password")
                    + "\t" + commonSession.getResourceBound().getString("Email")
                    + "\t" + commonSession.getResourceBound().getString("Phone")
                    + "\t" + commonSession.getResourceBound().getString("Phonenumber");
        }
        return templateOfParent;
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
        applicationParentController.edit(getSelected());
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        mainXhtml.setPageName(listpage);
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void naviChangePassword() {
        mainXhtml.setPageName("parentInfo/changePassword");
    }

    public void exeChangePassword() {
        //validate whether the old password is correct
        boolean oldCorrect = new StrongPasswordEncryptor().checkPassword(getOldPassword(), loginParent.getPassword());
        //If true
        if (oldCorrect) {
            loginParent.setPassword(StaticFields.encrypt(getUpdatedPassword1()));
            applicationParentController.edit(loginParent);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        } //update  the password
        //Else
        else {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller Parent 5");
        }
        mainXhtml.setPageName(this.editpage);
    }

    public void delSelectedStu() {
        setSelected( new Parent());
    }

    public void getBatchParent() {
        prepareCreate();
        mainXhtml.setPageName(this.batchParent);
    }

    public void getSingleParent() {
        prepareCreate();
        mainXhtml.setPageName(this.singleParent);
    }
}
