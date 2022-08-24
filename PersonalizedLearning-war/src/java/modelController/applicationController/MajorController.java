package modelController.applicationController;

import entities.Major;
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
import sessionBeans.MajorFacadeLocal;

@Named("majorControllerA")
@ApplicationScoped
public class MajorController extends ApplicationCommonController {

    @EJB
    private MajorFacadeLocal ejbFacadelocal;
    @Inject
    MajorsubjectController majorsubjectController;
    @Inject
    TeacherAdminController teacherAdminController;
    @Inject
    StudentController studentController;
    private MajorRelatedEntity majorRelatedEntity;

    private MajorFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Major getMajor(java.lang.Integer id) {
        return getFacade().find(id);
    }
    //-------------------------------
    //================For the majorSubject ===============
    //temSubjects is those being selected in the manyListbox
    // 
    // private Set<Major> candidateMajors;

    public Set<Major> getCandidateMajors() {
        return getAllSet();
    }

    public MajorRelatedEntity getMajorRelatedEntity() {
        return majorRelatedEntity;
    }

    public void setMajorRelatedEntity(MajorRelatedEntity majorRelatedEntity) {
        this.majorRelatedEntity = majorRelatedEntity;
    }

    public Major findByName(String name) {
        setDataChanged(false);
        return getFacade().findByName(name);
    }

    public static enum MajorRelatedEntity {
        SUBJECT, TEACHER
    }

    public void create(Major major) {
        getFacade().create(major);
        setDataChanged(true);
    }

    public void remove(Major major) {
        getFacade().remove(major);
        setDataChanged(true);
    }

    public void edit(Major major) {
        getFacade().edit(major);
        setDataChanged(true);
    }

    public List<Major> getQueryResultList(String sql) {
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

    public List<Major> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Major find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    //=====================================================
    //------------------------Converter-----------------------
    @FacesConverter(forClass = Major.class)
    public static class MajorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MajorController controller = (MajorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "majorControllerA");
            return controller.getMajor(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Major) {
                Major o = (Major) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Major.class.getName());
            }
        }
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Major> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }
}
