package modelController.applicationController;

import entities.Edulevel;
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
import sessionBeans.EdulevelFacadeLocal;

/**
 *
 * @author hgs
 */
@Named("edulevelControllerA")
@ApplicationScoped
public class EdulevelController extends ApplicationCommonController {

    @EJB
    private EdulevelFacadeLocal edulevelFacadeLocal;

    private EdulevelFacadeLocal getFacade() {
        return edulevelFacadeLocal;
    }

    public Edulevel getEduLevel(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public void create(Edulevel edulevel) {
        getFacade().create(edulevel);
        setDataChanged(true);
    }

    public void remove(Edulevel edulevel) {
        getFacade().remove(edulevel);
        setDataChanged(true);
    }

    public void edit(Edulevel edulevel) {
        getFacade().edit(edulevel);
        setDataChanged(true);
    }

    public List<Edulevel> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Edulevel> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Edulevel find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    //------------------------Converter-----------------------
    @FacesConverter(forClass = Edulevel.class)
    public static class EdulevelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EdulevelController controller = (EdulevelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "edulevelControllerA");
            return controller.getEduLevel(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Edulevel) {
                Edulevel o = (Edulevel) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Edulevel.class.getName());
            }
        }

    }

    public Set getAllSet() {
        return new HashSet(getAllList());
    }

    public List<Edulevel> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }

    public Edulevel findByName(String name) {
        setDataChanged(false);
        return getFacade().findByName(name);
    }
}
