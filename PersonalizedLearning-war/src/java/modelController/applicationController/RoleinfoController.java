package modelController.applicationController;

import entities.Roleinfo;
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
import sessionBeans.RoleinfoFacadeLocal;

@Named("roleinfoControllerA")
@ApplicationScoped
public class RoleinfoController extends ApplicationCommonController {

    @EJB
    private sessionBeans.RoleinfoFacadeLocal ejbFacadelocal;
    private List<Roleinfo> allRoleinfos = new LinkedList<>();
    private final String[] roleName = {"Admin", "Secretary", "Teacher", "Student", "Parent", "Reexamin"};

    private RoleinfoFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public Roleinfo getRoleinfo(java.lang.Integer id) {
        return ejbFacadelocal.find(id);
    }

    public Roleinfo findByName(String name) {
        return getFacade().findByName(name);
    }

    public void create(Roleinfo roleinfo) {
        getFacade().create(roleinfo);
    }

    public void remove(Roleinfo roleinfo) {
        getFacade().remove(roleinfo);
    }

    public void edit(Roleinfo roleinfo) {
        getFacade().edit(roleinfo);
    }

    public List<Roleinfo> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }

    public boolean isAdmin(TeacherAdmin teacher) {
        if (null == teacher) {
            return false;
        }
        return teacher.getRoleId().getRolename().equals(roleName[0]);
    }

    public boolean isSecreatary(TeacherAdmin teacher) {
        if (null == teacher) {
            return false;
        }
        return teacher.getRoleId().getRolename().equals(roleName[1]);
    }

    public boolean isSecreatary(Roleinfo roleType) {
        return roleType.getRolename().equals(roleName[1]);
    }

    public boolean isAdminOrSecretary(TeacherAdmin teacher) {
        if (teacher == null) {
            return false;
        }
        return isSecreatary(teacher) || isAdmin(teacher);
    }

    public boolean isTeahcer(Roleinfo roleType) {
        return roleType.getRolename().equals(roleName[2]);
    }

    public boolean isStudent(Roleinfo roleType) {
        return roleType.getRolename().equals(roleName[3]);
    }

    public boolean isParent(Roleinfo roleType) {
        return roleType.getRolename().equals(roleName[4]);
    }

    public boolean isReexamin(Roleinfo roleType) {
        return roleType.getRolename().equals(roleName[5]);
    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Roleinfo> findRange(int[] range) {
        return getFacade().findRange(range);
    }

    public Roleinfo find(Integer id) {
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getAllList());
    }

    public List<Roleinfo> getAllList() {
        if (allRoleinfos.isEmpty()) {
            allRoleinfos = getFacade().findAll();
        }
        return allRoleinfos;
    }

    public Roleinfo getRoleinfo(UserType userType) {
        switch (userType) {
            case Student:
                return getStudentRoleinfo();
            case Parent:
                return getParentRoleinfo();
            case Teacher:
                return getTeacherRoleinfo();
            case Secretary:
                return getSecretaryRoleinfo();
            case Admin:
                return getAdminRoleinfo();
            case Reexamin:
                return getReexaminRoleinfo();
        }
        return null;
    }

    public Roleinfo getStudentRoleinfo() {
        Roleinfo result = null;
        for (Roleinfo roleinfo : getAllList()) {
            if (isStudent(roleinfo)) {
                result = roleinfo;
                break;
            }
        }
        return result;
    }

    public Roleinfo getParentRoleinfo() {
        Roleinfo result = null;
        for (Roleinfo roleinfo : getAllList()) {
            if (isParent(roleinfo)) {
                result = roleinfo;
                break;
            }
        }
        return result;
    }

    public Roleinfo getTeacherRoleinfo() {
        Roleinfo result = null;
        for (Roleinfo roleinfo : getAllList()) {
            if (isTeahcer(roleinfo)) {
                result = roleinfo;
                break;
            }
        }
        return result;
    }

    public Roleinfo getAdminRoleinfo() {
        Roleinfo result = null;
        for (Roleinfo roleinfo : getAllList()) {
            if (roleinfo.getRolename().equals("Admin")) {
                result = roleinfo;
                break;
            }
        }
        return result;
    }

    public Roleinfo getSecretaryRoleinfo() {
        Roleinfo result = null;
        for (Roleinfo roleinfo : getAllList()) {
            if (roleinfo.getRolename().equals("Secretary")) {
                result = roleinfo;
                break;
            }
        }
        return result;
    }

    public Roleinfo getReexaminRoleinfo() {
        Roleinfo result = null;
        for (Roleinfo roleinfo : getAllList()) {
            if (roleinfo.getRolename().equals("Reexamin")) {
                result = roleinfo;
                break;
            }
        }
        return result;
    }

    @FacesConverter(forClass = Roleinfo.class)
    public static class RoleinfoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RoleinfoController controller = (RoleinfoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "roleinfoControllerA");
            return controller.getRoleinfo(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Roleinfo) {
                Roleinfo o = (Roleinfo) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Roleinfo.class.getName());
            }
        }
    }

    public String[] getRoleName() {
        return roleName;
    }
}
