package modelController.sessionController;

import entities.Logs;
import entities.Roleinfo;
import entities.Student;
import entities.TeacherAdmin;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Inject;

@Named("logsController")
@SessionScoped
public class LogsController extends CommonModelController<Logs> implements Serializable {

    private Logs current;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    private Roleinfo roleType;
    private TeacherAdmin teacherAdmin;
    private Student student;

    public LogsController() {
    }


    public Roleinfo getRoleType() {
        return roleType;
    }

    public void setRoleType(Roleinfo roleType) {
        this.roleType = roleType;
    }

    public TeacherAdmin getTeacherAdmin() {
        return teacherAdmin;
    }

    public void setTeacherAdmin(TeacherAdmin teacherAdmin) {
        this.teacherAdmin = teacherAdmin;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isStudentShow() {
        if (null == getRoleType()) {
            return false;
        }
        return applicationRoleinfoController.isStudent(roleType);
    }

    public boolean isTeacherShow() {
        if (null == getRoleType()) {
            return false;
        }
        return applicationRoleinfoController.isTeahcer(roleType);
    }

    private List<Logs> getLogs4Selection() {
        List<Logs> result = new LinkedList<>();
        if (isTeacherShow() && teacherAdmin != null) {
            result = applicationLogsController.getLogs4Teacher(teacherAdmin);
        } else if (isStudentShow() && null != student) {
            result = applicationLogsController.getLogs4Student(student);
        }
        pageOperation.setDataModelList(result);
        return result;
    }

    @Override
    public DataModel getItems() {
        pageOperation.refreshData(getLogs4Selection());
        return pageOperation.getItems();
    }
}
