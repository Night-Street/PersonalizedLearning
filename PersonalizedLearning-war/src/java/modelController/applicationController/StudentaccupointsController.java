package modelController.applicationController;

import entities.Student;
import entities.Studentaccupoints;
import entities.TeacherAdmin;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import sessionBeans.StudentaccupointsFacadeLocal;

/**
 *
 * @author hadoop
 */
@Named("studentaccupointsControllerA")
@ApplicationScoped
public class StudentaccupointsController extends ApplicationCommonController {

    @EJB
    private StudentaccupointsFacadeLocal ejbFacadeLocal;

    public Set<Studentaccupoints> getAllSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public StudentaccupointsFacadeLocal getFacade() {
        return ejbFacadeLocal;
    }

    public void setFacade(StudentaccupointsFacadeLocal ejbFacadeLocal) {
        this.ejbFacadeLocal = ejbFacadeLocal;
    }

    public void create(Studentaccupoints sap) {
        getFacade().create(sap);
        setDataChanged(true);
    }

    public void edit(Studentaccupoints sap) {
        getFacade().edit(sap);
        setDataChanged(true);
    }

    public void remove(Studentaccupoints sap) {
        getFacade().remove(sap);
        setDataChanged(true);
    }

    //传入学生，计算此学生的积分
    public double calculateStudentPoints(Student stu) {
        //创建资源获得的积分与人家点赞获得的积分
        List<Studentaccupoints> curStuAllPoints = this.getStudentaccupoints4Student(stu);
        if (curStuAllPoints.isEmpty()) {
            return 0;
        } else {
            double allPoints = 0;
            allPoints = curStuAllPoints.stream().map((csp) -> csp.getPointsNum()).reduce(allPoints, (Double accumulator, Double _item) -> accumulator + _item);
            return allPoints;
        }
    }

    public List<Studentaccupoints> getStudentaccupoints4Student(Student student) {
        setDataChanged(false);
        return getFacade().getStudentaccupoints4Student(student);
    }

    public List<Studentaccupoints> getStudentaccupoints4Teacher(TeacherAdmin teacher) {
        setDataChanged(false);
        return getFacade().getStudentaccupoints4Teacher(teacher);
    }

    @FacesConverter(forClass = Studentaccupoints.class)
    public static class StudentaccupointsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            modelController.sessionController.StudentaccupointsController controller = (modelController.sessionController.StudentaccupointsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studentaccupointsController");
            return controller.getStudentaccupoints(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Studentaccupoints) {
                Studentaccupoints o = (Studentaccupoints) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Studentaccupoints.class.getName());
            }
        }

    }
}
