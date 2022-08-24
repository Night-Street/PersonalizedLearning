package modelController.viewerController;

import entities.Parent;
import entities.Roleinfo;
import entities.Student;
import entities.TeacherAdmin;
import entities.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modelController.applicationController.UserType;
import modelController.sessionController.ParentController;
import modelController.sessionController.StudentController;
import modelController.sessionController.TeacherAdminController;
import org.jasypt.util.password.StrongPasswordEncryptor;
import sessionBeans.StudentFacadeLocal;
import sessionBeans.TeacherAdminFacadeLocal;
import tools.MySwitch;
import tools.StaticFields;
import tools.pagination.JsfUtil;

/**
 *
 * @author Haogs
 */
@Named
@ViewScoped
public class CheckLogin implements Serializable {

    @EJB
    private StudentFacadeLocal studentFacadeLocal;
    @EJB
    private TeacherAdminFacadeLocal teacherAdminFacadeLocal;
    @Inject
    private TeacherAdminController teacherAdminController;
    @Inject
    private StudentController studentController;
    @Inject
    private ParentController parentController;
    @Inject
    private modelController.applicationController.MySystemController applicationMySystemcontroller;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private tools.UserMessagor userMessagor;
    private User user = new User();
    private String welcomeMess;
    private String rand;
    private boolean logined = false, rendered = true;
    private int tryTime = 0;
    private final int tryTimeLimitedNumber = 5;
    private Roleinfo roleinfo;
    private final HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    private final MySwitch loginButtonSwitch = new MySwitch(false);

    public String validateUser() {
        if (!FacesContext.getCurrentInstance().getMessageList().isEmpty()) {
            FacesContext.getCurrentInstance().getMessageList().clear();
        }
        if (showCode()) {
            if (null != commonSession.getSession().getAttribute("rand")) {
                String genRand = ((String) commonSession.getSession().getAttribute("rand")).trim();
                if (!genRand.equals(this.rand)) {//验证码错误
                    this.rand = null;
                    JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("InvValiCode"));
                    return null;
                }
            }
        }
        if (tryTime++ < tryTimeLimitedNumber) {//尝试登录未超过规定次数
            if (isUserlegal()) {//用户存在
                commonSession.getSession().setAttribute(StaticFields.LOGIN_ROLE, this.getRoleinfo());
                this.welcomeMess = this.getUser().getName();
                tryTime = 0;
                this.logined = true;
                //登录成功，把所有将来能用得着的内容存放到CommSession中
                commonSession.setSession(session);
                commonSession.setRoleinfo(roleinfo);
                commonSession.setUser(user);
                commonSession.setWelcomeMess(welcomeMess);
                commonSession.setLogined(true);
                return StaticFields.MAIN_PAGE;
            } else {//用户名或密码错误
                JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("IvalidUP"));
                this.rand = null;
                return null;
            }
        } else {
            this.rendered = false;
            JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("TryFaild"));
            this.rand = null;
            return null;
        }
    }

    public String submit() {//注册
        //判断是学生还是教师，如果是教师，则选择Teacher表，否则选择Student表
        if (this.getRoleinfo().equals(applicationRoleinfoController.getStudentRoleinfo())) {
            Student student = new Student();
            student.setEmail(this.getUser().getEmail());
            student.setFirstname(this.getUser().getFirstname());
            student.setSecondname(this.getUser().getSecondname());
            student.setName(this.getUser().getName());
            student.setPassword(StaticFields.encrypt(this.getUser().getPassword()));
            studentFacadeLocal.create(student);
        } else if (this.getRoleinfo().equals(applicationRoleinfoController.getTeacherRoleinfo())) {
            TeacherAdmin teacherAdmin = new TeacherAdmin();
            teacherAdmin.setEmail(this.getUser().getEmail());
            teacherAdmin.setFirstname(this.getUser().getFirstname());
            teacherAdmin.setSecondname(this.getUser().getSecondname());
            teacherAdmin.setName(this.getUser().getName());
            teacherAdmin.setPassword(StaticFields.encrypt(this.getUser().getPassword()));
            teacherAdminFacadeLocal.create(teacherAdmin);
        }
        return null;
    }

    private boolean isUserlegal() {
        boolean legalResult = false;
        if (null != roleinfo) {
            if (roleinfo.equals(applicationRoleinfoController.getStudentRoleinfo())) {
                List<User> studentsList = studentFacadeLocal.getQueryResultList("select * from student where name='" + user.getName().trim() + "'");
                if (null == studentsList || studentsList.isEmpty()) {
                    legalResult = false;
                } else {
                    legalResult = this.isRedudantLogin(UserType.Student, studentsList.get(0));
                }
            } else if (roleinfo.equals(applicationRoleinfoController.getAdminRoleinfo())
                    || roleinfo.equals(applicationRoleinfoController.getSecretaryRoleinfo())
                    || roleinfo.equals(applicationRoleinfoController.getTeacherRoleinfo())) {
                List<User> teacherAdminList = teacherAdminFacadeLocal.getQueryResultList("select * from TEACHER_ADMIN where name='" + user.getName().trim() + "'");
                if (null == teacherAdminList || teacherAdminList.isEmpty()) {
                    legalResult = false;
                } else {
                    legalResult = this.isRedudantLogin(UserType.Teacher, teacherAdminList.get(0));
                }
            } else if (roleinfo.equals(applicationRoleinfoController.getParentRoleinfo())) {
                List<User> parentList = teacherAdminFacadeLocal.getQueryResultList("select * from parent where name='" + user.getName().trim() + "'");
                if (null == parentList || parentList.isEmpty()) {
                    legalResult = false;
                } else {
                    legalResult = this.isRedudantLogin(UserType.Parent, parentList.get(0));
                }
            } else if (roleinfo.equals(applicationRoleinfoController.getReexaminRoleinfo())) {
                List<User> reexaminList = teacherAdminFacadeLocal.getQueryResultList("select * from TEACHER_ADMIN where name='" + user.getName().trim() + "'");
                if (null == reexaminList || reexaminList.isEmpty()) {
                    legalResult = false;
                } else {
                    legalResult = this.isRedudantLogin(UserType.Reexamin, reexaminList.get(0));
                }
            }
        }
        return legalResult;
    }

    private boolean isRedudantLogin(UserType loginRoleType, User loginUser) {
        //检查是否重复登录
        boolean ckresult = false;
        ckresult = new StrongPasswordEncryptor().checkPassword(getUser().getPassword(), loginUser.getPassword());
        if (ckresult) {
            String ipaddress = getIPAddress();
            int haslogined = 0;
            //0 means has not logined, 1 means logined and from the same IP, 2 means logined and from different IP
            if (roleinfo.equals(loginUser.getRoleId())) {
                this.user = (User) loginUser;
                List<HttpSession> toBeremoved = new LinkedList<>();
                commonSession.setUserType(loginRoleType);
                switch (loginRoleType) {
                    case Parent:
                        parentController.setLoginParent((Parent) loginUser);
                        commonSession.getSession().setAttribute(StaticFields.SESSION_MYUSER, loginUser);
                        break;
                    case Teacher:
                    case Secretary:
                    case Admin:
                    case Reexamin:
                        teacherAdminController.setLogined((TeacherAdmin) loginUser);
                        commonSession.getSession().setAttribute(StaticFields.SESSION_MYUSER, loginUser);
                        applicationMySystemcontroller.getSessionMap().get(applicationRoleinfoController.getRoleinfo(loginRoleType)).add(commonSession.getSession());
                        break;
                    case Student:
                        HashSet<HttpSession> existedSessions = applicationMySystemcontroller.getSessionMap().get(applicationRoleinfoController.getRoleinfo(loginRoleType));
                        toBeremoved.clear();
                        for (HttpSession session1 : existedSessions) {
                            try {
                                if (null != session1 && ((Student) (session1.getAttribute(StaticFields.SESSION_MYUSER))).getId().equals(((Student) loginUser).getId())) {
                                    if (ipaddress.equals(session1.getAttribute(StaticFields.IPADDRESS))) {
                                        haslogined = 1;
                                        //mySystemcontroller.getSessionMap().get(applicationRoleinfoController.getStudentRoleinfo()).add(getSession());
                                        studentController.setLoginStudent((Student) loginUser);
                                        commonSession.getSession().setAttribute(StaticFields.SESSION_MYUSER, loginUser);
                                    } else {
                                        this.reloginWarn();//From different IP address
                                        haslogined = 2;
                                    }
                                    break;
                                }
                            } catch (Exception e) {
                                toBeremoved.add(session1);//Guess that session1 is invalidated
                                // haslogined = false;
                            }
                        }
                        if (haslogined == 0) {
                            studentController.setLoginStudent((Student) loginUser);
                            commonSession.getSession().setAttribute(StaticFields.SESSION_MYUSER, loginUser);
                        }
                        break;
                }
            } else {
                return false;
            }
        }
        return ckresult;
    }

    private void reloginWarn() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(StaticFields.RELOGINWARN);
        } catch (IOException ex) {
        }

    }

    public String registerSave() {
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succedd") + ", "
                + commonSession.getResourceBound().getString("Please")
                + commonSession.getResourceBound().getString("Wait"));
        //写入数据库
        
        return null;
    }

    public String register() {
        return StaticFields.REGISTER_PATH;
    }

    public void setRand(String rand) {
        this.rand = rand;
    }

    public String getRand() {
        return this.rand;
    }

    public String getWelcomeMess() {
        return welcomeMess;
    }

    public void setWelcomeMess(String welcomemessage) {
        this.welcomeMess = welcomemessage;
    }

    public boolean isLogined() {
        return logined;
    }

    public void setLogined(boolean rendered) {
        this.logined = rendered;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Roleinfo getRoleinfo() {
        return roleinfo;
    }

    public boolean isTeacher() {
        return roleinfo.equals(applicationRoleinfoController.getTeacherRoleinfo());
    }

    public void setRoleinfo(Roleinfo rolelogin) {
        this.roleinfo = rolelogin;
        if (null != rolelogin) {
            this.loginButtonSwitch.turnOn();
        }
    }

    public String toHome() {
        return StaticFields.NOLOGIN_MAIN_PAGE;
    }

    public tools.MySwitch getLoginButtonSwitch() {
        return loginButtonSwitch;
    }

    public boolean showCode() {
        return tryTime > 1;
    }

    public String getIPAddress() {
        //IP set-------------
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        commonSession.getSession().setAttribute(StaticFields.IPADDRESS, ipAddress);
        //System.out.println(ipAddress + "==========================================================+++++++++++++");
        return ipAddress;
        //---------IP set finished--------
    }
    public static void main(String[] args){
        String encryptedPwd = StaticFields.encrypt("1");
        System.out.println("固定加密: "+encryptedPwd);
    }

}
