package modelController.applicationController;

import entities.Edgeamongsubject;

import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import sessionBeans.EdgeamongsubjectFacadeLocal;

@Named("edgeamongsubjectControllerA")
@ApplicationScoped
public class EdgeamongSubjectController extends ApplicationCommonController {

    @EJB
    private sessionBeans.EdgeamongsubjectFacadeLocal ejbFacade;

    public EdgeamongSubjectController() {
    }

    private EdgeamongsubjectFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Edgeamongsubject getEdgeamongsubject(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public void create(Edgeamongsubject edgeamongsubject) {
        getFacade().create(edgeamongsubject);
        setDataChanged(true);
    }

    public void remove(Edgeamongsubject edgeamongsubject) {
        getFacade().remove(edgeamongsubject);
        setDataChanged(true);
    }

    public void edit(Edgeamongsubject edgeamongsubject) {
        getFacade().edit(edgeamongsubject);
        setDataChanged(true);
    }

    public List<Edgeamongsubject> getQueryResultList(String sql) {
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

    public Edgeamongsubject find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public List<Edgeamongsubject> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    @FacesConverter(forClass = Edgeamongsubject.class)
    public static class EdgeamongsubjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EdgeamongSubjectController controller = (EdgeamongSubjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "edgeamongsubjectControllerA");
            return controller.getEdgeamongsubject(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Edgeamongsubject) {
                Edgeamongsubject o = (Edgeamongsubject) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Edgeamongsubject.class.getName());
            }
        }
    }
}
