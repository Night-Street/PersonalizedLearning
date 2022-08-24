package modelController.applicationController;

import entities.Major;
import entities.TeacherAdmin;
import entities.Teachermajor;
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
import sessionBeans.TeachermajorFacadeLocal;

@Named("teachermajorControllerA")
@ApplicationScoped
public class TeachermajorController extends ApplicationCommonController {

    @EJB
    private TeachermajorFacadeLocal ejbFacade;
    @Inject
    modelController.applicationController.MajorController applicationMajorController;

    public TeachermajorController() {
    }

    private TeachermajorFacadeLocal getFacade() {
        return ejbFacade;
    }
    private Set<Major> teachMajors = new HashSet<>();

    public Set<Major> getTeachMajors(TeacherAdmin teacherAdmin) {
        if (null != teacherAdmin) {
            teachMajors.clear();
            List<Teachermajor> ts = getTeachermajor4Teacher(teacherAdmin);
            ts.forEach((teachermajor) -> {
                teachMajors.add(teachermajor.getMajorid());
            });
        }
        return teachMajors;
    }

    public List<Teachermajor> getTeachermajor4Teacher(TeacherAdmin teacher) {
        setDataChanged(false);
        return getFacade().getTeachermajor4Teacher(teacher);
    }

    public List<Teachermajor> getTeachermajor4Major(Major major) {
        setDataChanged(false);
        return getFacade().getTeachermajor4Major(major);
    }
    private String majorsString = "";

    public String getMajorsString(TeacherAdmin teacherAdmin) {
        majorsString = "";
        teachMajors.clear();
        Set<Teachermajor> ts = teacherAdmin.getTeachermajorSet();
        ts.forEach((teachermajor) -> {
            majorsString += "," + teachermajor.getMajorid().getId();
        });
        if (majorsString.length() > 1) {
            majorsString = majorsString.substring(1);
        }
        return majorsString;

    }

    public Teachermajor getTeachermajor(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public void create(Teachermajor teachermajor) {
        getFacade().create(teachermajor);
        setDataChanged(true);
    }

    public void remove(Teachermajor teachermajor) {
        getFacade().remove(teachermajor);
        setDataChanged(true);
    }

    public void edit(Teachermajor teachermajor) {
        getFacade().edit(teachermajor);
        setDataChanged(true);
    }

    public List<Teachermajor> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Teachermajor.class)
    public static class TeachermajorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TeachermajorController controller = (TeachermajorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "teachermajorControllerA");
            return controller.getTeachermajor(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Teachermajor) {
                Teachermajor o = (Teachermajor) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Teachermajor.class.getName());
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

    public List<Teachermajor> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Teachermajor find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }
}
