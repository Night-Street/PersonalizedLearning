package modelController.applicationController;

import entities.Knowledge;
import entities.Learningresource;
import entities.Subject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.servlet.http.Part;
import sessionBeans.LearningresourceFacadeLocal;
import tools.StaticFields;

@Named("learningresourceControllerA")
@ApplicationScoped
public class LearningresourceController extends ApplicationCommonController {

    @EJB
    private sessionBeans.LearningresourceFacadeLocal ejbFacadelocal;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private List<Learningresource> learningResourceList = null;
    public final static int MAXPICTURENUMBER = 5;
    private final int maxVideoSize = 1024 * 1000 * 500;
    private final int maxAudioSize = 1024 * 1000 * 100;
    private final int maxSize = 1024000;
    private final int maxPDFSize = 10240000;
    String valueInfoString = "";
    private Set<Learningresource> learningresourcesAllList = null;

    public LearningresourceController() {
    }

    public int getMaxVideoSize() {
        return maxVideoSize;
    }

    public int getMaxAudioSize() {
        return maxAudioSize;
    }

    public int getMaxPDFSize() {
        return maxPDFSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    private LearningresourceFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public List<Learningresource> getLearningresources4Knowledge(Knowledge knowledge) {
        return getFacade().getLearningResources4Subject(knowledge);
    }

    public List<Learningresource> getLearningResourceList(Subject subject) {
        learningResourceList = new LinkedList<>();
        if (null != subject && null != subject.getId()) {
            applicationKnowledgeController.getKnowledgeList4Subject(subject).stream().forEachOrdered((knowledge) -> {
                learningResourceList.addAll(getLearningresources4Knowledge(knowledge));
            });
        }
        return learningResourceList;
    }

    public Learningresource findByName(String name) {
        return getFacade().findByName(name);
    }

    public Learningresource getLearningresource(java.lang.Integer id) {
        return ejbFacadelocal.find(id);
    }

    public void create(Learningresource learningresource) {
        getFacade().create(learningresource);
    }

    public void remove(Learningresource learningresource) {
        getFacade().remove(learningresource);
    }

    public void edit(Learningresource learningresource) {
        getFacade().edit(learningresource);
    }

    public void pictureValidate(FacesContext ctx, UIComponent comp, Object value) {
       String msgs = "";
        Part file = (Part) value;
        if (null != value) {
            if (file.getContentType().contains("image")) {
                if (file.getSize() > getMaxSize()) {
                    msgs=commonSession.getResourceBound().getString("Too")
                            + commonSession.getResourceBound().getString("Big");
                    userMessagor.addMessage(msgs);
                }
            } else {
                msgs=commonSession.getResourceBound().getString("Not") + commonSession.getResourceBound().getString("Image");
                userMessagor.addMessage(msgs);
            }
        }
        if (msgs.trim().length()>0) {
            throw new ValidatorException(new FacesMessage(msgs));
        }

    }

    HashMap<Integer, String> mediaMap;

    public HashMap<Integer, String> getMediaMap() {
        if (null == mediaMap || mediaMap.isEmpty()) {
            mediaMap = new HashMap<>();
            mediaMap.put(StaticFields.IMAGETYPE, commonSession.getResourceBound().getString("Image"));
            mediaMap.put(StaticFields.VIDEOTYPE, commonSession.getResourceBound().getString("Video"));
            mediaMap.put(StaticFields.AUDIOTYPE, commonSession.getResourceBound().getString("Audio"));
            mediaMap.put(StaticFields.PDFTYPE, commonSession.getResourceBound().getString("PDF"));
        }
        return mediaMap;
    }

    public List<Learningresource> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
    }

    public List<Learningresource> findRange(int[] range) {
        return getFacade().findRange(range);
    }

    public Learningresource find(Integer id) {
        return getFacade().find(id);
    }

    @FacesConverter(forClass = Learningresource.class)
    public static class LearningresourceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LearningresourceController controller = (LearningresourceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "learningresourceControllerA");
            return controller.getLearningresource(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Learningresource) {
                Learningresource o = (Learningresource) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Learningresource.class.getName());
            }
        }
    }

}
