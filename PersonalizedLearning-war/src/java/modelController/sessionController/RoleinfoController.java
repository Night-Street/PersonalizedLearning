package modelController.sessionController;

import entities.Roleinfo;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import tools.StaticFields;

@Named("roleinfoController")
@SessionScoped
public class RoleinfoController extends CommonModelController<Roleinfo> implements Serializable {

    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private final String tableName = "roleinfo";
    private String newRoleName = "";

    public void setDataModelList() {
        pageOperation.setDataModelList(applicationRoleinfoController.getAllList());
    }
    protected Roleinfo current;

    public Roleinfo getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Roleinfo();
        }
        return current;
    }
    public void setSelected(Roleinfo current) {
        this.current = current;
    }

    public void edit() {
        applicationRoleinfoController.edit(current);
    }

    public String create() {
        try {
            if (null == applicationRoleinfoController.findByName(getNewRoleName())) {
                setSelected(new Roleinfo());
                current.setRolename(getNewRoleName());
                applicationRoleinfoController.create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                this.logs(current.getRolename(), tableName, StaticFields.OPERATIONINSERT);
                createdEnabled = false;
                return null;
            } else {
                userMessagor.addMessage(current.getRolename() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
                return null;
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller role 1");
            return null;
        }
    }

    public String getNewRoleName() {
        return newRoleName;
    }

    public void setNewRoleName(String newRoleName) {
        createdEnabled = true;
        this.newRoleName = newRoleName;
    }
    boolean createdEnabled = false;//To determine wether the command create a new role is enabled

    public boolean isCreateEnabled() {
        return createdEnabled;
    }

    public boolean isSecretary() {
        return commonSession.getRoleinfo().equals(applicationRoleinfoController.getSecretaryRoleinfo());
    }

    public boolean isTeacher() {
        return commonSession.getRoleinfo().equals(applicationRoleinfoController.getTeacherRoleinfo());
    }

    public boolean isStudent() {
        return commonSession.getRoleinfo().equals(applicationRoleinfoController.getStudentRoleinfo());
    }

    public boolean isParent() {
        return commonSession.getRoleinfo().equals(applicationRoleinfoController.getParentRoleinfo());
    }

    public boolean isAdmin() {
        return commonSession.getRoleinfo().equals(applicationRoleinfoController.getAdminRoleinfo());
    }

    public boolean isReexamin() {
        return commonSession.getRoleinfo().equals(applicationRoleinfoController.getReexaminRoleinfo());
    }
    
}
