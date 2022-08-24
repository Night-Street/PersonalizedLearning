package modelController.applicationController;

import entities.Student;
import entities.WrongquestionCollection;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.WrongquestionCollectionFacadeLocal;

@Named("wrongquestionCollectionControllerA")
@ApplicationScoped
public class WrongquestionCollectionController   extends ApplicationCommonController  {

    @EJB
    private sessionBeans.WrongquestionCollectionFacadeLocal ejbFacade;

    public WrongquestionCollectionController() {
    }

    private WrongquestionCollectionFacadeLocal getFacade() {
        return ejbFacade;
    }

    public WrongquestionCollection getWrongquestionCollection(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public List<WrongquestionCollection> getWrongquestionCollection4Student(Student student) {
        return getFacade().getWrongquestionCollection4Student(student);
    }

    public void create(WrongquestionCollection wrongquestionCollection) {
        getFacade().create(wrongquestionCollection);
        setDataChanged(true);
    }

    public void remove(WrongquestionCollection wrongquestionCollection) {
        getFacade().remove(wrongquestionCollection);
        setDataChanged(true);
    }

    public void edit(WrongquestionCollection wrongquestionCollection) {
        getFacade().edit(wrongquestionCollection);
        setDataChanged(true);
    }

    public List<WrongquestionCollection> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = WrongquestionCollection.class)
    public static class WrongquestionCollectionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WrongquestionCollectionController controller = (WrongquestionCollectionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "wrongquestionCollectionControllerA");
            return controller.getWrongquestionCollection(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WrongquestionCollection) {
                WrongquestionCollection o = (WrongquestionCollection) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + WrongquestionCollection.class.getName());
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

    public List<WrongquestionCollection> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public WrongquestionCollection find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }
}
