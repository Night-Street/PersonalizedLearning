package modelController.applicationController;

import entities.Roleinfo;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.sessionController.RoleinfoController;
import entities.User;
import javax.servlet.http.HttpSession;
import tools.StaticFields;
import modelController.viewerController.MainXhtml;

/**
 *
 * @author hgs
 */
@Named("mySystemControllerA")
@ApplicationScoped
public class MySystemController extends ApplicationCommonController {

    @Inject
    RoleinfoController roleinfoController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    private HashMap<Roleinfo, HashSet<HttpSession>> sessionmap = new HashMap<>();
    private HashMap<Roleinfo, HashSet<User>> usersmap = new HashMap<>();

    @PostConstruct
    public void init() {
        sessionmap.put(applicationRoleinfoController.getTeacherRoleinfo(), new HashSet<>());
        sessionmap.put(applicationRoleinfoController.getStudentRoleinfo(), new HashSet<>());
        sessionmap.put(applicationRoleinfoController.getReexaminRoleinfo(), new HashSet<>());
        usersmap.put(applicationRoleinfoController.getTeacherRoleinfo(), new HashSet<>());
        usersmap.put(applicationRoleinfoController.getStudentRoleinfo(), new HashSet<>());
        usersmap.put(applicationRoleinfoController.getReexaminRoleinfo(), new HashSet<>());
        //To avoid the invalide session, add Null in it 
        sessionmap.get(applicationRoleinfoController.getTeacherRoleinfo()).add(null);
        sessionmap.get(applicationRoleinfoController.getStudentRoleinfo()).add(null);
        sessionmap.get(applicationRoleinfoController.getReexaminRoleinfo()).add(null);
    }

    public HashMap<Roleinfo, HashSet<HttpSession>> getSessionMap() {
        return sessionmap;
    }

    public void setSessionMap(HashMap<Roleinfo, HashSet<HttpSession>> sessionmap) {
        this.sessionmap = sessionmap;
    }

    public HashSet<User> getLogingedTeacher() {
        usersmap.get(applicationRoleinfoController.getTeacherRoleinfo()).clear();
        for (HttpSession session : getSessionMap().get(applicationRoleinfoController.getTeacherRoleinfo())) {
            if (null != session) {
                usersmap.get(applicationRoleinfoController.getTeacherRoleinfo()).add((User) session.getAttribute(StaticFields.SESSION_MYUSER));
            }
        }
        return usersmap.get(applicationRoleinfoController.getTeacherRoleinfo());
    }

    public HashSet<User> getLoginedStudent() {
        usersmap.get(applicationRoleinfoController.getStudentRoleinfo()).clear();
        for (HttpSession session : getSessionMap().get(applicationRoleinfoController.getStudentRoleinfo())) {
            if (null != session && null != session.getId()) {
                try {
                    usersmap.get(applicationRoleinfoController.getStudentRoleinfo()).add((User) session.getAttribute(StaticFields.SESSION_MYUSER));
                } catch (Exception e) {
                }
            }
        }
        return usersmap.get(applicationRoleinfoController.getStudentRoleinfo());
    }

    public void setUsersmap(HashMap<Roleinfo, HashSet<User>> usersmap) {
        this.usersmap = usersmap;
    }

    public void viewLoginedUsers() {
        mainXhtml.setPageName("systemConsole/viewLoginedUsers");
    }

}
