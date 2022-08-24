package modelController.applicationController;

import entities.Parent;
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
import sessionBeans.ParentFacadeLocal;

@Named("parentControllerA")
@ApplicationScoped
public class ParentController extends ApplicationCommonController {

    @EJB
    private ParentFacadeLocal ejbFacadelocal;
    @Inject
    SchoolController applicationSchoolController;
    @Inject
    RoleinfoController applicationRoleinfoController;
    @Inject
    CommonSession commonSession;
    @Inject
    LogsController logsController;
    private Parent nullParent;

    public Parent getNullParent() {
        if (null == nullParent) {
            nullParent = (Parent) getFacade().getQueryResultList("select * from parent where name='PNULL'").get(0);
        }
        return nullParent;
    }

    public Parent findByName(String parentIdInSchool) {
        return getFacade().findByName(parentIdInSchool);
    }

    public ParentController() {
    }

    private ParentFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Parent getParent(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacadelocal.find(id);
    }

    public void create(Parent parent) {
        getFacade().create(parent);
        setDataChanged(true);
    }

    public void remove(Parent parent) {
        getFacade().remove(parent);
        setDataChanged(true);
    }

    public void edit(Parent parent) {
        getFacade().edit(parent);
        setDataChanged(true);
    }

    public List<Parent> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Parent.class)
    public static class ParentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ParentController controller = (ParentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "parentControllerA");
            return controller.getParent(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Parent) {
                Parent o = (Parent) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Parent.class.getName());
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

    public List<Parent> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Parent find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Parent> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }
}
