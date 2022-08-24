package modelController.applicationController;

import entities.School;
import entities.Student;
import entities.Studentschedule;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.StudentscheduleFacadeLocal;

@Named("studentscheduleControllerA")
@ApplicationScoped
public class StudentscheduleController extends ApplicationCommonController {

    @EJB
    private StudentscheduleFacadeLocal ejbFacade;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;

    public StudentscheduleController() {
    }

    private StudentscheduleFacadeLocal getFacade() {
        return ejbFacade;
    }

    public List<Studentschedule> getStudentscheduleList(School school) {
        List<Studentschedule> result = new ArrayList<>();
        Calendar current = Calendar.getInstance();
        String currentTime = current.get(Calendar.YEAR) + "-" + current.get(Calendar.MONTH) + "-" + current.get(Calendar.DAY_OF_MONTH);
        //SELECT * FROM LEARNING.STUDENTSCHEDULE where endtime>'2018-05-01' and userid=439;
        if (null != school && null != school.getId()) {
            applicationStudentController.getStudentList(school).forEach((student) -> {
                result.addAll(getFacade().getQueryResultList(
                        "SELECT * FROM STUDENTSCHEDULE where endtime>'"
                        + currentTime + "'  and userid=" + student.getId()
                ));
            });
        }
        return result;
    }

    public List<Studentschedule> getStudentschedule4Student(Student Student) {
        setDataChanged(false);
        return getFacade().getStudentschedule4Student(Student);
    }

    public Studentschedule getStudentschedule(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public void create(Studentschedule studentschedule) {
        getFacade().create(studentschedule);
        setDataChanged(true);
    }

    public void remove(Studentschedule studentschedule) {
        getFacade().remove(studentschedule);
        setDataChanged(true);
    }

    public void edit(Studentschedule studentschedule) {
        getFacade().edit(studentschedule);
        setDataChanged(true);
    }

    public List<Studentschedule> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Studentschedule.class)
    public static class StudentscheduleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StudentscheduleController controller = (StudentscheduleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studentscheduleControllerA");
            return controller.getStudentschedule(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Studentschedule) {
                Studentschedule o = (Studentschedule) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Studentschedule.class.getName());
            }
        }
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Studentschedule> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Studentschedule find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }
}
