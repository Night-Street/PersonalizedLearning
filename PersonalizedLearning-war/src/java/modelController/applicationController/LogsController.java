package modelController.applicationController;

import entities.Logs;
import entities.Student;
import entities.TeacherAdmin;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.LogsFacadeLocal;

@Named("logsControllerA")
@ApplicationScoped
public class LogsController extends ApplicationCommonController {

    @EJB
    private LogsFacadeLocal ejbFacade;
    @Inject
    private TeacherAdminController applicationTeacherAdminController;
    @Inject
    private StudentController applicationStudentController;

    private boolean logsStduentChanged = true;

    private List<Student> logStudents = new LinkedList<>();
    private List<TeacherAdmin> logTeachers = new LinkedList<>();

    public List<Student> getLogsStudents() {
        if (logsStduentChanged) {
            logStudents = getFacade().getQueryResultList("select * from student where id in ("
                    + "select studentid from logs where studentid!=" + applicationStudentController.getNullStudent().getId() + ")");
            logsStduentChanged = false;
        }
        return logStudents;
    }
    private boolean logsTeacherChanged = true;

    public List getLogsTeachers() {
        if (logsTeacherChanged) {
            logTeachers = getFacade().getQueryResultList("select * from teacher_admin where id in ("
                    + "select teacherid from logs where teacherid!=" + applicationTeacherAdminController.getNullTeacherAdmin().getId() + ")");
            logsTeacherChanged = false;
        }
        return logTeachers;
    }

    public LogsController() {
    }

    private LogsFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Logs getLogs(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public List<Logs> getLogs4Student(Student student) {
        setDataChanged(false);
        return getFacade().getLogs4Student(student);
    }

    public List<Logs> getLogs4Teacher(TeacherAdmin teacher) {
        setDataChanged(false);
        return getFacade().getLogs4Teacher(teacher);
    }

    public void create(Logs logs) {
        getFacade().create(logs);
        setDataChanged(true);
        if (!Objects.equals(logs.getStudentid().getId(), applicationStudentController.getNullStudent().getId())) {
            logsStduentChanged = true;
        } else if (!Objects.equals(logs.getTeacherid().getId(), applicationTeacherAdminController.getNullTeacherAdmin().getId())) {
            logsTeacherChanged = true;
        }
    }

    public void remove(Logs logs) {
        getFacade().remove(logs);
        setDataChanged(true);
    }

    public void edit(Logs logs) {
        getFacade().edit(logs);
        setDataChanged(true);
    }

    public List<Logs> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Logs.class)
    public static class LogsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LogsController controller = (LogsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "logsControllerA");
            return controller.getLogs(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Logs) {
                Logs o = (Logs) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Logs.class.getName());
            }
        }

    }

    public void persist(TeacherAdmin teacherid, Student studentid, String recorderinfo, String tablename, String operationName) {
        if (null == teacherid) {
            teacherid = applicationTeacherAdminController.getNullTeacherAdmin();
        }
        if (null == studentid) {
            studentid = applicationStudentController.getNullStudent();
        }
        if (recorderinfo.length() > 20) {
            recorderinfo = recorderinfo.substring(0, 19);
        }
        Logs logs = new Logs();
        logs.setTeacherid(teacherid);
        logs.setStudentid(studentid);
        logs.setRecorderinfo(recorderinfo);
        logs.setTablename(tablename);
        logs.setOperation(operationName);
        logs.setOperationtime(Calendar.getInstance().getTime());
        getFacade().create(logs);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Logs> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Logs find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

}
