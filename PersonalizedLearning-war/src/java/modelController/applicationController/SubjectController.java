package modelController.applicationController;

import entities.Subject;
import java.util.HashMap;
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
import sessionBeans.SubjectFacadeLocal;

@Named("subjectControllerA")
@ApplicationScoped
public class SubjectController extends ApplicationCommonController {

    @EJB
    private sessionBeans.SubjectFacadeLocal ejbFacadelocal;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;

    private SubjectFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Subject getSubject(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public void create(Subject subject) {
        getFacade().create(subject);
        setDataChanged(true);
    }

    public void remove(Subject subject) {
        getFacade().remove(subject);
        setDataChanged(true);
    }

    public void edit(Subject subject) {
        getFacade().edit(subject);
        setDataChanged(true);
    }

    public List<Subject> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Subject.class)
    public static class SubjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SubjectController controller = (SubjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "subjectControllerA");
            return controller.getSubject(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Subject) {
                Subject o = (Subject) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Subject.class.getName());
            }
        }

    }

    public String getKnowlegeIdsString(Subject subject) {
        List<Integer> ids = applicationKnowledgeController.getKnowledgeId4Subject(subject);
//        String result = "";
//        if (null != subject && null != subject.getId()) {
//            List<Knowledge> knowledgeSet = applicationKnowledgeController.getKnowledgeList4Subject(subject);
//            for (Knowledge knowledge : knowledgeSet) {
//                result += "," + knowledge.getId();
//            }
//        }
//        if (result.trim().length() > 0) {
//            result = result.substring(1);
//        }
        if (ids.size() > 0) {
            return ids.toString().substring(1, ids.toString().length() - 1);
        } else {
            return "";
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

    public List<Subject> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Subject find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Subject> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }

    public Subject findByName(String name) {
        setDataChanged(false);
        return getFacade().findByName(name);
    }

    HashMap<Integer, Subject> subjectMap;

    public HashMap<Integer, Subject> getSubjectMap() {
        if (subjectMap == null || subjectMap.isEmpty()) {
            List<Subject> all = getFacade().findAll();
            for (Subject subject:all) {
                subjectMap.put(subject.getId(), subject);
            }
        }
        return subjectMap;
    }

//    public boolean isHashSetEqual(HashSet<Subject> a, HashSet<Subject> b) {
//        boolean equal = true;
//        if (null == a || null == b) {
//            return false;
//        }
//        for (Subject aElement : a) {
//            if (!b.contains(aElement)) {
//                equal = false;
//                break;
//            }
//        }
//        if (!equal) {//传来的都在现在的里边，再比较现有的是否都在传来的里边
//            for (Subject bElement : b) {
//                if (!a.contains(bElement)) {
//                    equal = false;
//                    break;
//                }
//            }
//        }
//        return equal;
//    }
}
