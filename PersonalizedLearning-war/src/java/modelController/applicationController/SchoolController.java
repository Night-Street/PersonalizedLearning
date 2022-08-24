package modelController.applicationController;

import entities.Major;
import entities.School;
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
import javax.inject.Inject;
import sessionBeans.SchoolFacadeLocal;

@Named("schoolControllerA")
@ApplicationScoped
public class SchoolController extends ApplicationCommonController {

    @EJB
    private SchoolFacadeLocal ejbFacadelocal;
    @Inject
    TeacherAdminController teacherAdminController;
    private Set<School> schoolsOnLeaves;
    private List<School> schoolRootList;
    private List<School> allSchools;
    
    private List<School> getAllSchools(){
        if(null==allSchools){
            allSchools= getFacade().findAll();
        }
        return allSchools;
    }
    

    public List<School> getRootSchoolList() {
        if (null == schoolRootList || schoolRootList.isEmpty()) {
            schoolRootList = new LinkedList<>();
            allSchools.forEach((School school) -> {
                if (null == ((School) school).getParentid()) {
                    schoolRootList.add((School) school);
                }
            });
        }
        return schoolRootList;
    }

    private SchoolFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public School findByName(String name) {
        setDataChanged(false);
        return getFacade().findByName(name);
    }

    public School getSchool(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public List<School> getSchool4School(School school) {
        setDataChanged(false);
        return getFacade().getSchool4School(school);
    }

    public List<School> getSchool4Major(Major major) {
        setDataChanged(false);
        return getFacade().getSchool4Major(major);
    }

    public void create(School school) {
        getFacade().create(school);
        setDataChanged(true);
    }

    public void remove(School school) {
        getFacade().remove(school);
        setDataChanged(true);
    }

    public void edit(School school) {
        getFacade().edit(school);
        setDataChanged(true);
    }

    public List<School> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    //------------------------Converter-----------------------
    @FacesConverter(forClass = School.class)
    public static class SchoolControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SchoolController controller = (SchoolController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "schoolControllerA");
            return controller.getSchool(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof School) {
                School o = (School) object;
                return o.getId().toString();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + School.class.getName());
            }
        }
    }

    public Set<School> getSchoolsOnLeaves() {
        if (null == schoolsOnLeaves) {
            schoolsOnLeaves = new HashSet<>();
            for (School school:getAllSchools()) {
                 if (school.getSchoolSet().isEmpty()) {
                    schoolsOnLeaves.add(school);
                }
            }
        }
        return schoolsOnLeaves;
    }

    public void setSchoolsOnLeaves(Set<School> schoolsOnLeaves) {
        this.schoolsOnLeaves = schoolsOnLeaves;
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<School> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public School find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getAllSchools());
    }

    public List<School> getAllList() {
        setDataChanged(false);
        return this.getAllSchools();
    }
}
