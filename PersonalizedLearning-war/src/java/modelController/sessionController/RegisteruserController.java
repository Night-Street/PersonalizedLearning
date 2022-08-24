/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.sessionController;

import entities.Registeruser;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

/**
 *
 * @author Administrator
 */
@Named("registeruserController")
@SessionScoped
public class RegisteruserController extends CommonModelController<Registeruser> implements Serializable {

    protected Registeruser current;
    @Inject
    private modelController.applicationController.RegisteruserController applicationRegisteruserController;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private MainXhtml mainXhtml;
    private final String tableName = "Registeruser", listpage = "secretaryRegisteruser/List";

    public Registeruser getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Registeruser();
        }
        return current;
    }

    public RegisteruserController() {

    }

    public void setSelected(Registeruser reexamination) {
        current = reexamination;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(applicationRegisteruserController.getAllList());
    }

    public void create() {
        //检查用户名是否存在，如果存在，则通知需要重新注册
        String sqlString = "select * from ";
        if (applicationRoleinfoController.isParent(current.getRoleid())) {
            sqlString += " parent ";
        } else if (applicationRoleinfoController.isTeahcer(current.getRoleid())) {
            sqlString += " teacher_admin ";
        } else if (applicationRoleinfoController.isStudent(current.getRoleid())) {
            sqlString += " student ";
        }
        sqlString += " where name='" + current.getName().trim() + "'";
        if (applicationRegisteruserController.countExisted(sqlString) > 0) {
            userMessagor.addMessage(current.getName() + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
        } else {
            applicationRegisteruserController.create(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        }
    }

    public void destroy() {
        current = (Registeruser) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(this.listpage);
    }
    private void performDestroy() {
        try {
            applicationRegisteruserController.remove(current);
            applicationRegisteruserController.refresh();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            //resourceinfosList.remove(current);
            pageOperation.refreshData(applicationRegisteruserController.getAllList());
            this.logs(current.getName(), tableName, StaticFields.OPERATIONINSERT);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller resource 2");
        }
    }
    
       private void updateCurrentItem() {
        int count = applicationRegisteruserController.getAllList().size();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected(applicationRegisteruserController.getAllList().get(selectedItemIndex));
        }
    }
}
