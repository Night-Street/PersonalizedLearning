package modelController.applicationController;

import entities.Partofspeech;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import sessionBeans.PartofspeechFacadeLocal;

/**
 *
 * @author hgs
 */
@Named("partofSpeechControllerA")
@ApplicationScoped
public class PartofSpeechController extends ApplicationCommonController {

    @EJB
    private PartofspeechFacadeLocal facade;

    public PartofspeechFacadeLocal getFacade() {
        return facade;
    }

    public Partofspeech findByName(String name) {
        setDataChanged(false);
        return getFacade().findByName(name);
    }

    public Partofspeech getPartofspeech(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public void create(Partofspeech partofspeech) {
        getFacade().create(partofspeech);
        setDataChanged(true);
    }

    public void remove(Partofspeech partofspeech) {
        getFacade().remove(partofspeech);
        setDataChanged(true);
    }

    public void edit(Partofspeech partofspeech) {
        getFacade().edit(partofspeech);
        setDataChanged(true);
    }

    public List<Partofspeech> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Partofspeech> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Partofspeech find(Integer id) {
        return getFacade().find(id);
    }

    //=====================================================
    //------------------------Converter-----------------------
    @FacesConverter(forClass = Partofspeech.class)
    public static class MajorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PartofSpeechController controller = (PartofSpeechController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "partofSpeechControllerA");
            return controller.getPartofspeech(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Partofspeech) {
                Partofspeech o = (Partofspeech) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Partofspeech.class.getName());
            }
        }
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Partofspeech> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }
}
