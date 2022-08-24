package modelController.applicationController;

import entities.Scheduleclass;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.ScheduleclassFacadeLocal;

@Named("scheduleclassControllerA")
@ApplicationScoped
public class ScheduleclassController extends ApplicationCommonController {

    @EJB
    private ScheduleclassFacadeLocal ejbFacade;
    @Inject
    LogsController logsController;
    @Inject
    TeacherAdminController teacherAdminController;

    public ScheduleclassController() {
    }

    private ScheduleclassFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Scheduleclass getScheduleclass(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public List<Scheduleclass> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }

    public void create(Scheduleclass scheduleclass) {
        getFacade().create(scheduleclass);
        setDataChanged(true);
    }

    public void remove(Scheduleclass scheduleclass) {
        getFacade().remove(scheduleclass);
        setDataChanged(true);
    }

    public void edit(Scheduleclass scheduleclass) {
        getFacade().edit(scheduleclass);
        setDataChanged(true);

    }

    public List<Scheduleclass> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Scheduleclass.class)
    public static class ScheduleclassControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ScheduleclassController controller = (ScheduleclassController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "scheduleclassControllerA");
            return controller.getScheduleclass(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Scheduleclass) {
                Scheduleclass o = (Scheduleclass) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Scheduleclass.class.getName());
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

    public List<Scheduleclass> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Scheduleclass find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }
}
