package modelController.applicationController;

import entities.Scheduleclass;
import entities.TeacherAdmin;
import entities.Teacherschedule;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.TeacherscheduleFacadeLocal;

@Named("teacherscheduleControllerA")
@ApplicationScoped
public class TeacherscheduleController extends ApplicationCommonController {

    @EJB
    private sessionBeans.TeacherscheduleFacadeLocal ejbFacade;

    public TeacherscheduleController() {
    }

    private TeacherscheduleFacadeLocal getFacade() {
        return ejbFacade;
    }

    public List<Teacherschedule> getTeacherschedule4Teacher(TeacherAdmin teacher) {
        setDataChanged(false);
        return getFacade().getTeacherschedule4Teacher(teacher);
    }

    public List<Teacherschedule> getTeacherschedule4Scheduleclass(Scheduleclass scheduleclass) {
        setDataChanged(false);
        return getFacade().getTeacherschedule4Scheduleclass(scheduleclass);
    }

    public Teacherschedule getTeacherschedule(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public void create(Teacherschedule teacherschedule) {
        getFacade().create(teacherschedule);
        setDataChanged(true);
    }

    public void remove(Teacherschedule teacherschedule) {
        getFacade().remove(teacherschedule);
        setDataChanged(true);
    }

    public void edit(Teacherschedule teacherschedule) {
        getFacade().edit(teacherschedule);
        setDataChanged(true);
    }

    public List<Teacherschedule> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Teacherschedule.class)
    public static class TeacherscheduleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TeacherscheduleController controller = (TeacherscheduleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "teacherscheduleControllerA");
            return controller.getTeacherschedule(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Teacherschedule) {
                Teacherschedule o = (Teacherschedule) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Teacherschedule.class.getName());
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

    public List<Teacherschedule> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Teacherschedule find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }
}
