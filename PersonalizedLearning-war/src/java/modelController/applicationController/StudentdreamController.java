package modelController.applicationController;

import entities.Studentdream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import modelController.sessionController.CommonSession;
import sessionBeans.StudentdreamFacadeLocal;

@Named("studentdreamControllerA")
@ApplicationScoped
public class StudentdreamController extends ApplicationCommonController {

    @EJB
    private StudentdreamFacadeLocal ejbFacadelocal;
    @Inject
    StudentController applicationStudentController;
    @Inject
    CommonSession commonSession;
    @Inject
    LogsController logsController;

 
    public StudentdreamController() {
    }

    private StudentdreamFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Studentdream getStudentdream(java.lang.Integer id) {
        return ejbFacadelocal.find(id);
    }

    public void create(Studentdream student) {
        getFacade().create(student);
        setDataChanged(true);
    }

    public void remove(Studentdream student) {
        getFacade().remove(student);
    }

    public void edit(Studentdream student) {
        getFacade().edit(student);
    }

    public List<Studentdream> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Studentdream.class)
    public static class StudentdreamControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StudentdreamController controller = (StudentdreamController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studentControllerA");
            return controller.getStudentdream(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Studentdream) {
                Studentdream o = (Studentdream) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Studentdream.class.getName());
            }
        }
    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
    }

    public List<Studentdream> findRange(int[] range) {
        return getFacade().findRange(range);
    }

    public Studentdream find(Integer id) {
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Studentdream> getAllList() {
        return getFacade().findAll();
    }
}
