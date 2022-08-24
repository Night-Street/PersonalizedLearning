package modelController.applicationController;

import entities.School;
import entities.Student;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.sessionController.CommonSession;
import sessionBeans.StudentFacadeLocal;

@Named("studentControllerA")
@ApplicationScoped
public class StudentController extends ApplicationCommonController {

    @EJB
    private StudentFacadeLocal ejbFacadelocal;
    @Inject
    SchoolController applicationSchoolController;
    @Inject
    RoleinfoController applicationRoleinfoController;
    @Inject
    CommonSession commonSession;
    @Inject
    LogsController logsController;
    private Student nullStudent;

    public Student getNullStudent() {
        if (null == nullStudent) {
            nullStudent = (Student) getFacade().getQueryResultList("select * from student where name='SNULL'").get(0);
        }
        return nullStudent;
    }

    public Student findByName(String studentIdInSchool) {
        return getFacade().findByName(studentIdInSchool);
    }

    public List<Student> getStudent4School(School school) {
        return getFacade().getStudent4School(school);
    }

    //This method is too complex. The function get students for the school is given in the above method
    public List<Student> getStudentList(School school) {
        ArrayList<Student> result = new ArrayList<>();
        if (null != school && null != school.getId()) {
            result = new ArrayList<>(getStudent4School(school));
            result.sort((Student o1, Student o2) -> o1.getSecondname().compareToIgnoreCase(o2.getSecondname()));
        }
        return result;
    }

    public StudentController() {
    }

    private StudentFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Student getStudent(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacadelocal.find(id);
    }

    public void create(Student student) {
        getFacade().create(student);
        setDataChanged(true);
    }

    public void remove(Student student) {
        getFacade().remove(student);
        setDataChanged(true);
    }

    public void edit(Student student) {
        getFacade().edit(student);
        setDataChanged(true);
    }

    public List<Student> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Student.class)
    public static class StudentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StudentController controller = (StudentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studentControllerA");
            return controller.getStudent(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Student) {
                Student o = (Student) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Student.class.getName());
            }
        }
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Student> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Student find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Student> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }
}
