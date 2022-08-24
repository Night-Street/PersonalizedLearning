package modelController.applicationController;

import entities.Predicate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import sessionBeans.PredicateFacadeLocal;

@Named("predicateControllerA")
@ApplicationScoped
public class PredicateController extends ApplicationCommonController {
    @EJB
    private PredicateFacadeLocal ejbFacadelocal; 

    private PredicateFacadeLocal getFacade() {
        return ejbFacadelocal;
    }
    
     public Predicate getPredicate(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public void create(Predicate predicate) {
        getFacade().create(predicate);
        setDataChanged(true);
    }

    public void remove(Predicate predicate) {
        getFacade().remove(predicate);
        setDataChanged(true);
    }

    public void edit(Predicate predicate) {
        getFacade().edit(predicate);
        setDataChanged(true);
    }

    public List<Predicate> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }
       

    @FacesConverter(forClass = Predicate.class)
    public static class PredicateControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PredicateController controller = (PredicateController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "predicateControllerA");
            return controller.getPredicate(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Predicate) {
                Predicate o = (Predicate) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Predicate.class.getName());
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

    public List<Predicate> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Predicate find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    HashSet<Integer> allPOrderSet;
    public Set getAllPorder() {
        if(null==allPOrderSet){
            allPOrderSet=new HashSet<>();
            allPOrderSet.add(1);
            allPOrderSet.add(2);
        }
        return allPOrderSet;
    }

    public List<Predicate> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }

    public Predicate findByName(String name) {
        setDataChanged(false);
        return getFacade().findByName(name);
    }

}
