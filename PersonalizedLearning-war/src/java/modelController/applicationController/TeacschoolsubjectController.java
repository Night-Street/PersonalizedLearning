package modelController.applicationController;

import entities.School;
import entities.Subject;
import entities.TeacherAdmin;
import entities.Teacschoolsubject;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import sessionBeans.TeacschoolsubjectFacadeLocal;
import tools.StaticFields;

@Named("teacschoolsubjectControllerA")
@ApplicationScoped
public class TeacschoolsubjectController extends ApplicationCommonController {

    @EJB
    private TeacschoolsubjectFacadeLocal ejbFacade;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.applicationController.TeacherAdminController applicationTeacherAdminController;

    public TeacschoolsubjectController() {
    }
    private final List<SelectItem> teaSchoolSubjectItems = new LinkedList<>();

    public List<SelectItem> getTeaSchoolSubjectItems() {
        if (teaSchoolSubjectItems.size() <= 0) {
            teaSchoolSubjectItems.add(new SelectItem(StaticFields.TEACHERREF, commonSession.getResourceBound().getString("Teacher")));
            teaSchoolSubjectItems.add(new SelectItem(StaticFields.CLASSREF, commonSession.getResourceBound().getString("Class")));
            teaSchoolSubjectItems.add(new SelectItem(StaticFields.SUBJECTREF, commonSession.getResourceBound().getString("Subject")));
        }
        return teaSchoolSubjectItems;
    }
//Get the school list that the teacher teaches in this semester
    HashMap<School, HashSet<Subject>> schoolSubjectMap4LoginedTeac;
//Get all the subjects that the teacher have in this school in this semester

    private TeacschoolsubjectFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Teacschoolsubject getTeacschoolsubject(TeacherAdmin teacher, School school, Subject subject, String aftertime) {
        setDataChanged(false);
        String whereString = "";
        if (null != teacher&&null!=teacher.getId()) {
            whereString += " teacherid=" + teacher.getId();
        }
        if (null != school&&null!=school.getId()) {
            whereString += " and schoolid=" + school.getId();
        }
        if (null != subject&&null!=subject.getId()) {
            whereString += " and subjectid=" + subject.getId();
        }
        if (whereString.startsWith(" and ")) {
            whereString = whereString.substring(4);
        }
        if (whereString.trim().length() > 0) {
            whereString += " and totime>'" + aftertime + "'";
        }
        List<Teacschoolsubject> resultList;
        if (whereString.trim().length() == 0) {
            resultList = getFacade().getQueryResultList("select * from Teacschoolsubject");
        } else {
            resultList = getFacade().getQueryResultList("select * from Teacschoolsubject where " + whereString);
        }
        if (!resultList.isEmpty()) {
            return (Teacschoolsubject) resultList.get(tools.Tool.getRand(resultList.toArray()));
        } else {
            return null;
        }
    }

    public List<Teacschoolsubject> getTeacschoolsubject4School(School school) {
        setDataChanged(false);
        return getFacade().getTeacschoolsubject4School(school);
    }

    public List<Teacschoolsubject> getTeacschoolsubject4Subject(Subject subject) {
        setDataChanged(false);
        return getFacade().getTeacschoolsubject4Subject(subject);
    }

    public List<Teacschoolsubject> getTeacschoolsubject4Teacher(TeacherAdmin teacher) {
        setDataChanged(false);
        return getFacade().getTeacschoolsubject4Teacher(teacher);
    }

    public Set<Teacschoolsubject> getTeacschoolsubjects(TeacherAdmin teacher, Calendar calendar) {
        setDataChanged(false);
        //一定不是管理员或秘书，因为他们不用上课
        Set<Teacschoolsubject> resultSet = new HashSet<>();
        Date datePara = calendar.getTime();
        List<Teacschoolsubject> teachersTeacschoolsubjects = getFacade().getTeacschoolsubject4Teacher(teacher);
        teachersTeacschoolsubjects.forEach((Teacschoolsubject teaSchoolSubjectiterator) -> {
            if (teaSchoolSubjectiterator.getFromtime().before(datePara) && teaSchoolSubjectiterator.getTotime().after(datePara)) {
                resultSet.add(teaSchoolSubjectiterator);
            }
        });
        return resultSet;
    }

    public Teacschoolsubject getTeacschoolsubject(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);

    }

    public void create(Teacschoolsubject teacschoolsubject) {
        getFacade().create(teacschoolsubject);
        setDataChanged(true);
    }

    public void remove(Teacschoolsubject teacschoolsubject) {
        getFacade().remove(teacschoolsubject);
        setDataChanged(true);
    }

    public void edit(Teacschoolsubject teacschoolsubject) {
        getFacade().edit(teacschoolsubject);
        setDataChanged(true);
    }

    public List<Teacschoolsubject> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Teacschoolsubject.class)
    public static class TeacschoolsubjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TeacschoolsubjectController controller = (TeacschoolsubjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "teacschoolsubjectControllerA");
            return controller.getTeacschoolsubject(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Teacschoolsubject) {
                Teacschoolsubject o = (Teacschoolsubject) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Teacschoolsubject.class.getName());
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

    public List<Teacschoolsubject> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Teacschoolsubject find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }
}
