package modelController.applicationController;

import entities.WrongReason;
import java.io.Serializable;
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
import sessionBeans.WrongReasonFacadeLocal;

@Named("wrongReasonControllerA")
@ApplicationScoped
public class WrongReasonController   extends ApplicationCommonController {

    @EJB
    private WrongReasonFacadeLocal ejbFacade;

    public WrongReasonController() {
    }

    private WrongReasonFacadeLocal getFacade() {
        return ejbFacade;
    }
    public WrongReason findByName(String name){
        setDataChanged(false);
        return getFacade().findByName(name);
    }
    public WrongReason getWrongReason(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public void create(WrongReason wrongReason) {
        getFacade().create(wrongReason);setDataChanged(true);
    }

    public void remove(WrongReason wrongReason) {
        getFacade().remove(wrongReason); setDataChanged(true);
    }

    public void edit(WrongReason wrongReason) {
        getFacade().edit(wrongReason);setDataChanged(true);
    }

    public List<WrongReason> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }
    @FacesConverter(forClass = WrongReason.class)
    public static class WrongReasonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WrongReasonController controller = (WrongReasonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "wrongReasonControllerA");
            return controller.getWrongReason(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WrongReason) {
                WrongReason o = (WrongReason) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + WrongReason.class.getName());
            }
        }

    }
    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }
    
    public void executUpdate(String updateString) {
        setDataChanged(true);
        getFacade().executUpdate(updateString);
    }

    public List<WrongReason> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }
    
    public WrongReason find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }
        public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<WrongReason> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }
}
