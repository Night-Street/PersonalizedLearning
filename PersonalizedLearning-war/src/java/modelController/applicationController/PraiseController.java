package modelController.applicationController;

import entities.Knowledge;
import entities.Learningresource;
import entities.Praise;
import entities.Question;
import entities.Student;

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
import sessionBeans.PraiseFacadeLocal;

@Named("praiseControllerA")
@ApplicationScoped
public class PraiseController extends ApplicationCommonController {

    @EJB
    private sessionBeans.PraiseFacadeLocal ejbFacade;

    public PraiseController() {
    }

    public PraiseFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void create(Praise praise) {
        getFacade().create(praise);
        setDataChanged(true);
    }

    public void edit(Praise praise) {
        getFacade().edit(praise);
        setDataChanged(true);

    }

    public void remove(Praise Praise) {
        getFacade().remove(Praise);
        setDataChanged(true);
    }

    public Praise getPraise(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public List<Praise> getQueryResultList(String sql) {
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

    public List<Praise> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public List<Praise> getPraise4Student(Student student) {
        setDataChanged(false);
        return getFacade().getPraise4Student(student);
    }

    public List<Praise> getPraise4Learningresource(Learningresource learingresource) {
        setDataChanged(false);
        return getFacade().getPraise4Learningresource(learingresource);
    }

    public List<Praise> getPraise4Question(Question question) {
        setDataChanged(false);
        return getFacade().getPraise4Question(question);
    }

    public List<Praise> getPraise4Knowledge(Knowledge knowledge) {
        setDataChanged(false);
        return getFacade().getPraise4Knowledge(knowledge);
    }

    public Praise find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    @FacesConverter(forClass = Praise.class)
    public static class PraiseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PraiseController controller = (PraiseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "praiseController");
            return controller.getPraise(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Praise) {
                Praise o = (Praise) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Praise.class.getName());
            }
        }

    }

}
