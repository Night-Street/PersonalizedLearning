package modelController.sessionController;

import entities.Roleinfo;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import modelController.applicationController.UserType;
import tools.MySwitch;
import tools.StaticFields;

/**
 *
 * @author haogs
 */
@Named
@SessionScoped
public class CommonSession implements java.io.Serializable {
    
    @Inject
    private modelController.applicationController.MySystemController applicationMySystemcontroller;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.sessionController.LocaleController localeController;
    @Inject
    private modelController.sessionController.StudentController studentController;
    @Inject
    private modelController.sessionController.TeacherAdminController teacherAdminController;
    @Inject
    modelController.sessionController.ParentController parentController;
    private HttpSession session;
    private Roleinfo roleinfo;
    private User user = new User();
    private String welcomeMess;
    private final MySwitch loginButtonSwitch = new MySwitch(false);
    private boolean logined = false;
    //boolean currentTeacher = false;//只要是教师登录，这个就设置为true
    private ResourceBundle MESSAGES_PROPTES;
    private UserType userType;
    private List<String> previousPages = new ArrayList<>();
    
    public HttpSession getSession() {
        if (null == session) {
            this.session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        }
        return this.session;
    }
    
    public void setSession(HttpSession session) {
        this.session = session;
    }
    
    public Roleinfo getRoleinfo() {
        return roleinfo;
    }
    
    public void setRoleinfo(Roleinfo roleinfo) {
        this.roleinfo = roleinfo;
//        if (roleinfo.equals(applicationRoleinfoController.getTeacherRoleinfo())
//                || roleinfo.equals(applicationRoleinfoController.getAdminRoleinfo())
//                || roleinfo.equals(applicationRoleinfoController.getSecretaryRoleinfo())
//                || roleinfo.equals(applicationRoleinfoController.getReexaminRoleinfo())) {
//            currentTeacher = true;
//        } else {
//            currentTeacher = false;
//        }
    }
    
    public User getUser() {
        if (null != userType) {
            switch (userType) {
                case Teacher:
                    user = teacherAdminController.getLogined();
                    break;
                case Student:
                    user = studentController.getLogined();
                    break;
                case Parent:
                    user = parentController.getLogined();
                    break;
                case Secretary:
                    break;
                case Admin:
                    break;
            }
        }
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getWelcomeMess() {
        return welcomeMess;
    }
    
    public void setWelcomeMess(String welcomeMess) {
        this.welcomeMess = welcomeMess;
    }
    
    public String login() {
        if (null == this.roleinfo) {
            loginButtonSwitch.turnOff();
        } else {
            loginButtonSwitch.turnOn();
        }
        return StaticFields.LONGIN_PATH;
    }
    
    public String logout() {
        if (roleinfo.equals(applicationRoleinfoController.getStudentRoleinfo())) {
            applicationMySystemcontroller.getSessionMap().get(applicationRoleinfoController.getStudentRoleinfo()).remove(getSession());
        } else if (roleinfo.equals(applicationRoleinfoController.getAdminRoleinfo())
                || roleinfo.equals(applicationRoleinfoController.getSecretaryRoleinfo())
                || roleinfo.equals(applicationRoleinfoController.getTeacherRoleinfo())
                || roleinfo.equals((applicationRoleinfoController.getParentRoleinfo()))) {
            applicationMySystemcontroller.getSessionMap().get(applicationRoleinfoController.getTeacherRoleinfo()).remove(getSession());
        }
        this.user = null;
        this.setLogined(false);
        this.roleinfo = null;
        getSession().invalidate();
        return StaticFields.NOLOGIN_MAIN_PAGE;
    }

    //下面为学生与教师角色间切换而定义
    //currentTeacher表明是教师登录，且其值从来不改变
    //现在是教师，还是学生，是通过getRoleinfo()来进行设置和判断的
//    public boolean getCurrentTeacher() {
//        return currentTeacher;
//    }

    
    public boolean change2Teacher() {
        return  teacherAdminController.isTeacherLogined()&&getRoleinfo().equals(applicationRoleinfoController.getStudentRoleinfo());
    }
    
    public boolean change2Student() {
        return  teacherAdminController.isTeacherLogined()&&getRoleinfo().equals(applicationRoleinfoController.getTeacherRoleinfo());
    }
    
    public boolean isLogined() {
        return logined;
    }
    
    public void setLogined(boolean logined) {
        this.logined = logined;
    }
    //下面是一个简单的奇偶变换的方法
    private int evenodd = 2;
    
    public int getCurrentEvenOdd() {
        evenodd = evenodd == 2 ? 0 : 2;
        return evenodd;
    }
    
    public ResourceBundle getResourceBound() {
        if (null == MESSAGES_PROPTES) {
            this.MESSAGES_PROPTES = ResourceBundle.getBundle("messages", localeController.getLocale());
        }
        return MESSAGES_PROPTES;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public List<String> getPreviousPages() {
        return previousPages;
    }
    
    public void setPreviousPages(List<String> previousPages) {
        this.previousPages = previousPages;
    }
}
