package modelController.applicationController;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import entities.Subject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import sessionBeans.EdgeamongknowledgeFacadeLocal;
import tools.StaticFields;

@Named("edgeamongknowledgeControllerA")
@ApplicationScoped
public class EdgeamongknowledgeController extends ApplicationCommonController {

    @EJB
    private sessionBeans.EdgeamongknowledgeFacadeLocal ejbFacade;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    private List<Edgeamongknowledge> edgeamongknowledgeList = new ArrayList<>();

    public EdgeamongknowledgeController() {
    }

    public EdgeamongknowledgeFacadeLocal getFacade() {
        return ejbFacade;
    }

    public List<Edgeamongknowledge> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }

    public List<Edgeamongknowledge> getEdgeamongknowledges(Knowledge knowledge, int location) {
        List<Edgeamongknowledge> resultList = null;
        switch (location) {
            case StaticFields.PREDCESSOR:
                resultList = new ArrayList<>(getFacade().findPredecessor(knowledge));
                break;
            case StaticFields.SUCCESSOR:
                resultList = new ArrayList<>(getFacade().findSuccessor(knowledge));
                break;
        }
        setDataChanged(true);
        return resultList;
    }

    public List<Edgeamongknowledge> getEdgeamongknowledgeList(Subject subject) {
        if (null != subject.getId()) {
            this.edgeamongknowledgeList.clear();
            List<Knowledge> knowledgesList = applicationKnowledgeController.getKnowledgeList4Subject(subject);
            Set<Edgeamongknowledge> e = new HashSet<>();
            for (int i = 0; i < knowledgesList.size(); i++) {
                e.addAll(getByPredecessor(knowledgesList.get(i)));
                e.addAll(getBySucessor(knowledgesList.get(i)));
            }
            edgeamongknowledgeList = new ArrayList<>(e);
        }
        return Optional.ofNullable(edgeamongknowledgeList).orElse(new LinkedList<>());
    }

    public void create(Edgeamongknowledge edgeamongknowledge) {
        getFacade().create(edgeamongknowledge);
        setDataChanged(true);
    }

    public void remove(Edgeamongknowledge edgeamongknowledge) {
        getFacade().remove(edgeamongknowledge);
        setDataChanged(true);
    }

    public void edit(Edgeamongknowledge edgeamongknowledge) {
        getFacade().edit(edgeamongknowledge);
        setDataChanged(true);
    }

    public Edgeamongknowledge getEdgeamongknowledge(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Edgeamongknowledge> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public List<Edgeamongknowledge> getByPredecessor(Knowledge knowledge) {
        setDataChanged(false);
        return getFacade().findPredecessor(knowledge);
    }

    private List<Edgeamongknowledge> getBySucessor(Knowledge knowledge) {
        setDataChanged(false);
        return getFacade().findSuccessor(knowledge);
    }

    public Edgeamongknowledge find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public String getString4Dagre(Edgeamongknowledge edge) {
        return "\"" + applicationKnowledgeController.getString4Dagre(edge.getPredecessornode())
                + "\"->\"" + applicationKnowledgeController.getString4Dagre(edge.getSuccessornode()) 
                + "\"" + "[labelType=\"html\" label=\" <span style='color:" 
                + edge.getPredicate().getColorname() + ";'>" + edge.getPredicate().getPname()
                + "</span> \"]\n";
    }

    @FacesConverter(forClass = Edgeamongknowledge.class)
    public static class EdgeamongknowledgeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EdgeamongknowledgeController controller = (EdgeamongknowledgeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "edgeamongknowledgeControllerA");
            return controller.getEdgeamongknowledge(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Edgeamongknowledge) {
                Edgeamongknowledge o = (Edgeamongknowledge) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Edgeamongknowledge.class.getName());
            }
        }
    }

}
