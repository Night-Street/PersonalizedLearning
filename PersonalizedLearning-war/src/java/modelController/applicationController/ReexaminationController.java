package modelController.applicationController;

import entities.Knowledge;
import entities.Question;
import entities.Reexamination;
import entities.Statusofresources;
import entities.TeacherAdmin;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import sessionBeans.ReexaminationFacadeLocal;

@Named("reexaminationControllerA")
@ApplicationScoped
public class ReexaminationController extends ApplicationCommonController {

    @EJB
    private sessionBeans.ReexaminationFacadeLocal ejbFacadelocal;

    public ReexaminationController() {
    }

    public ReexaminationFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public List<Reexamination> getReexaminationId2Examin(TeacherAdmin teacher, Statusofresources status) {
        //From major to subject, then to reexamination, therefore, first get the subject===========
        List<Reexamination> reexaminationsList = new LinkedList<>();
        if (null != status && null != status.getId() && null != teacher && null != teacher.getId()) {
            reexaminationsList = getFacade().getQueryResultList("select * from Reexamination where toteacher=" + teacher.getId()
                    + " and status=" + status.getId());
            setDataChanged(false);
        }
        return reexaminationsList;
    }

    public boolean isKnowledgeInStatus(Knowledge knowledge, int status) {
        return isQuestionOrKnowledgeInStatus(null, knowledge, status);
    }

    public boolean isQuestionInStatus(Question question, int status) {
        return isQuestionOrKnowledgeInStatus(question, null, status);
    }

    private boolean isQuestionOrKnowledgeInStatus(Question question, Knowledge knowledge, int status) {
        Statusofresources statusofresources = getStatus(question, knowledge);
        if (null != statusofresources) {
            return statusofresources.getId().equals(status);
        }
        return false;
    }

    public Statusofresources getStatus(Question question, Knowledge knowledge) {
        Reexamination reexamination = null;
        if (null != knowledge) {
            Set<Reexamination> reexaminations = getReexamination4Knowledge(knowledge);
            if (!reexaminations.isEmpty()) {
                reexamination = (Reexamination) reexaminations.toArray()[0];
                if (null != reexamination) {
                    return reexamination.getStatus();
                }
            }
        }
        if (null != question) {
            reexamination = (Reexamination) getReexamination4Question(question).toArray()[0];
            if (null != reexamination) {
                return reexamination.getStatus();
            }
        }
        return null;
    }

    public void create(Reexamination reexamination) {
        getFacade().create(reexamination);
    }

    public void remove(Reexamination reexamination) {
        getFacade().remove(reexamination);
    }

    public void edit(Reexamination reexamination) {
        getFacade().edit(reexamination);
    }

    public List<Reexamination> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }

    //=====================================================
    //------------------------Converter-----------------------
    @FacesConverter(forClass = Reexamination.class)
    public static class ReexaminationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReexaminationController controller = (ReexaminationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reexaminationControllerA");
            return controller.find(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Reexamination) {
                Reexamination o = (Reexamination) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Reexamination.class.getName());
            }
        }

    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Reexamination> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Reexamination find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public Set<Reexamination> getReexamination4Knowledge(Knowledge knowledge) {
        Set<Reexamination> temSet = knowledge.getReexaminationSet();
        if (temSet.isEmpty()) {//未更新到内存中来
            temSet = new HashSet<>(this.getQueryResultList("select * from Reexamination where knowledgeid=" + knowledge.getId()));
        }
        return temSet;
    }

    public Set<Reexamination> getReexamination4Question(Question question) {
        Set<Reexamination> temSet = question.getReexaminationSet();
        if (temSet.isEmpty()) {//未更新到内存中来
            temSet = new HashSet<>(this.getQueryResultList("select * from Reexamination where questionid=" + question.getId()));
        }
        return temSet;
    }
}
