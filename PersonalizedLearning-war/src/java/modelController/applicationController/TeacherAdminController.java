package modelController.applicationController;

import entities.Knowledge;
import entities.Question;
import entities.TeacherAdmin;
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
import sessionBeans.TeacherAdminFacadeLocal;

@Named("teacherAdminControllerA")
@ApplicationScoped
public class TeacherAdminController extends ApplicationCommonController {

    @EJB
    private TeacherAdminFacadeLocal ejbFacadelocal;
    @Inject
    private RoleinfoController applicationRoleinfoController;
    private TeacherAdmin nullTeacherAdmin;

    public TeacherAdminController() {
    }

    private TeacherAdminFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public TeacherAdmin getTeacherAdmin(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacadelocal.find(id);
    }

    public void create(TeacherAdmin teacherAdmin) {
        getFacade().create(teacherAdmin);
        setDataChanged(true);

    }

    public void remove(TeacherAdmin teacherAdmin) {
        getFacade().remove(teacherAdmin);
        setDataChanged(true);
    }

    public void edit(TeacherAdmin teacherAdmin) {
        getFacade().edit(teacherAdmin);
        setDataChanged(true);
    }

    public List<TeacherAdmin> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = TeacherAdmin.class)
    public static class TeacherAdminControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TeacherAdminController controller = (TeacherAdminController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "teacherAdminControllerA");
            return controller.getTeacherAdmin(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TeacherAdmin) {
                TeacherAdmin o = (TeacherAdmin) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TeacherAdmin.class.getName());
            }
        }

    }

    public TeacherAdmin getNullTeacherAdmin() {
        if (null == nullTeacherAdmin) {
            nullTeacherAdmin = (TeacherAdmin) getFacade().getQueryResultList("select * from teacher_admin where name='TNULL'").get(0);
        }
        return nullTeacherAdmin;
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<TeacherAdmin> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public TeacherAdmin find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<TeacherAdmin> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }

    public TeacherAdmin findByName(String name) {
        setDataChanged(false);
        return getFacade().findByName(name);
    }

    TeacherAdmin secretary = null;
    public TeacherAdmin getSecretary() {
       List<TeacherAdmin> secretaryList =getQueryResultList("select * from teacher_admin where role_id=" + applicationRoleinfoController.getSecretaryRoleinfo().getId());
       return secretaryList.get(tools.Tool.getRand(secretaryList.toArray()));
    }
   
}
