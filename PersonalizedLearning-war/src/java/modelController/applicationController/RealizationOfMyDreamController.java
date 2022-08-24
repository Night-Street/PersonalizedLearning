package modelController.applicationController;

import entities.Realizationofmydream;
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
import sessionBeans.RealizationOfMyDreamFacadeLocal;

@Named("realizationOfMyDreamControllerA")
@ApplicationScoped
public class RealizationOfMyDreamController extends ApplicationCommonController {

    @EJB
    private sessionBeans.RealizationOfMyDreamFacadeLocal ejbFacadelocal;
    @Inject
    CommonSession commonSession;
    @Inject
    LogsController logsController;

 
    public RealizationOfMyDreamController() {
    }

    private RealizationOfMyDreamFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Realizationofmydream getRealizationOfMyDream(java.lang.Integer id) {
        return ejbFacadelocal.find(id);
    }

    public void create(Realizationofmydream realizationofmydream) {
        getFacade().create(realizationofmydream);
        setDataChanged(true);
    }

    public void remove(Realizationofmydream realizationofmydream) {
        getFacade().remove(realizationofmydream);
    }

    public void edit(Realizationofmydream realizationofmydream) {
        getFacade().edit(realizationofmydream);
    }

    public List<Realizationofmydream> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Realizationofmydream.class)
    public static class RealizationOfMyDreamControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RealizationOfMyDreamController controller = (RealizationOfMyDreamController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "RealizationOfMyDreamControllerA");
            return controller.getRealizationOfMyDream(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Realizationofmydream) {
                Realizationofmydream o = (Realizationofmydream) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Realizationofmydream.class.getName());
            }
        }
    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
    }

    public List<Realizationofmydream> findRange(int[] range) {
        return getFacade().findRange(range);
    }

    public Realizationofmydream find(Integer id) {
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Realizationofmydream> getAllList() {
        return getFacade().findAll();
    }
}
