package modelController.sessionController;

import entities.Majorsubject;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import sessionBeans.MajorsubjectFacadeLocal;
import tools.StaticFields;

/**
 *
 * @author haogs
 */
@Named
@SessionScoped
public class MySystemController extends CommonModelController implements java.io.Serializable {
    @EJB
    private MajorsubjectFacadeLocal majorsubjectFacadeLocal;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.sessionController.SchoolController schoolController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.applicationController.MajorsubjectController applicationMajorsubjectController;
    String switch2Student = "systemConsole/switch2Student";

    public void switch2Student() {
        TeacherAdmin teacher = teacherAdminController.getLogined();//一定不为空，因为只有登录进来，才能看到
        //为 viewedTestpapers赋初值 
        schoolController.getCadidateSchools().clear();
        teacher.getTestpaperSet().forEach(testpaper -> {
            schoolController.getCadidateSchools().add(testpaper.getSchoolid());
        });
        if (schoolController.getCadidateSchools().isEmpty()) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("No")
                    + " " + commonSession.getResourceBound().getString("Student"));
        } else {
            mainXhtml.setPageName(switch2Student);
        }
    }

    public void switch2Teacher() {//切换回教师时，不需要进行班级与学生的设置
        studentController.setLoginStudent(null);
        commonSession.getSession().setAttribute(StaticFields.SESSION_MYUSER, teacherAdminController.getSelected());
        commonSession.getSession().setAttribute(StaticFields.LOGIN_ROLE, applicationRoleinfoController.getTeacherRoleinfo());
        commonSession.setUser(teacherAdminController.getSelected());
        commonSession.setRoleinfo(applicationRoleinfoController.getTeacherRoleinfo());
        commonSession.setWelcomeMess(teacherAdminController.getSelected().getName());
        mainXhtml.setPageName("news");
        mainXhtml.setPageName(switch2Student);
    }

    public String switch2StudentManiPage() {
        studentController.setLoginStudent(studentController.getSelected());
        commonSession.getSession().setAttribute(StaticFields.SESSION_MYUSER, studentController.getSelected());
        commonSession.getSession().setAttribute(StaticFields.LOGIN_ROLE, applicationRoleinfoController.getStudentRoleinfo());
        commonSession.setUser(studentController.getSelected());
        commonSession.setRoleinfo(applicationRoleinfoController.getStudentRoleinfo());
        commonSession.setWelcomeMess(studentController.getSelected().getName());
        mainXhtml.setPageName("news");
        return StaticFields.MAIN_PAGE;
    }

    public String switch2TeacherManiPage() {
        commonSession.getSession().setAttribute(StaticFields.SESSION_MYUSER, teacherAdminController.getLogined());
        commonSession.getSession().setAttribute(StaticFields.LOGIN_ROLE, applicationRoleinfoController.getTeacherRoleinfo());
        commonSession.setUser(teacherAdminController.getLogined());
        commonSession.setRoleinfo(applicationRoleinfoController.getTeacherRoleinfo());
        commonSession.setWelcomeMess(teacherAdminController.getLogined().getName());
        return StaticFields.MAIN_PAGE;
    }

    public void deleteNullValue() {
        //在系统处理的过程中可能会出现null值的情况，使得系统运行时出错，为些在必要时清理一下null值
        //1.Majorsubject随着课程的删除，会出现subject为null的情况，为此删除subject为null的记录
        List<Majorsubject> allMS=applicationMajorsubjectController.getAllList();
        allMS.forEach(majorsubject->{
            if(null==majorsubject.getSubjectid()){
                majorsubjectFacadeLocal.remove(majorsubject);
            }
        });
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
    }
}
