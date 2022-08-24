package modelController.applicationController;

import entities.Major;
import entities.Majorsubject;
import entities.Subject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import sessionBeans.MajorsubjectFacadeLocal;
import tools.StaticFields;

@Named("majorsubjectControllerA")
@ApplicationScoped
public class MajorsubjectController extends ApplicationCommonController {

    @Inject
    MajorController applicationMajorController;
    @Inject
    SubjectController applicationSubjectController;
    @EJB
    private MajorsubjectFacadeLocal ejbFacade;
    HashMap<Major, List<Subject>> majorSubjectMap = new LinkedHashMap<>();
    HashMap<Subject, List<Major>> subjectMajorMap = new LinkedHashMap<>();

    public MajorsubjectController() {
    }

    private MajorsubjectFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void executUpdate(String sqlString, Major major, Subject subject, String type) {
        switch (type) {
            case StaticFields.OPERATIONDELETE:
                List<Majorsubject> ms = getFacade().getQueryResultList("select * from Majorsubject  where majorid=" + major.getId()
                        + " and subjectId=" + subject.getId());

                //deal with other objects
//                Set<Major> majorsSet = applicationMajorController.getAllSet();
//                Major currentMajor = null;
//                for (Major majorIterator : majorsSet) {
//                    if (majorIterator.equals(major)) {
//                        currentMajor = majorIterator;
//                        break;
//                    }
//                }
//                
                //need to be continued......
                break;
            case StaticFields.OPERATIONINSERT:
                break;
            case StaticFields.OPERATIONUPDATE:
                break;
        }
        getFacade().executUpdate(sqlString);
    }

//    /**
//     * 获取专业和课程的键值对，其中课程是list类型
//     *
//     * @return the majorsubjectMap
//     */
//    public HashMap<Major, List<Subject>> getMajorSubjectMap() {
//        if (null == majorSubjectMap || majorSubjectMap.isEmpty()) {
//            /*首先获取所有专业*/
//            List<Major> majors = majorFacadeLocal.getQueryResultList("select * from major");
//            /*遍历年级及专业，按其将课程选择出来并存入Map中*/
//            majors.forEach((major) -> {
//                String sql = "select * from subject where id in(SELECT subjectid FROM MAJORSUBJECT where majorid=" + major.getId() + ")";
//                majorSubjectMap.put(major, subjectFacadeLocal.getQueryResultList(sql));
//            });
//            majorSubjectMap.forEach((majorObject, subjectList) -> {
//                subjectList.forEach((subject) -> {
//                    if (null == subjectMajorMap.get(subject)) {
//                        subjectMajorMap.put(subject, new LinkedList<>());
//                    }
//                    subjectMajorMap.get(subject).add(majorObject);
//                });
//            });
//        }
//        return majorSubjectMap;
//    }
    public void refreshMajorSubjectMap() {
        majorSubjectMap.clear();
        subjectMajorMap.clear();
    }

//    public HashMap<Subject, List<Major>> getSubjectMajorHashMap() {
//        if (null == subjectMajorMap || subjectMajorMap.isEmpty()) {
//            getMajorSubjectMap();
//        }
//        return subjectMajorMap;
//    }
    public List<Majorsubject> getMajorsubjects4Major(Major major) {
        return getFacade().getMajorsubject4Major(major);
    }

    public List<Majorsubject> getMajorsubjects4Subject(Subject subject) {
        return getFacade().getMajorsubject4Subject(subject);
    }

    public Set<Major> getMajors4Subject(Subject subject) {
        Set<Major> result = new HashSet<>();
        if (null != subject) {
            List<Majorsubject> subjectMajors = getMajorsubjects4Subject(subject);
            subjectMajors.forEach(majorSubject -> {
                result.add(majorSubject.getMajorid());
            });
        }
        return result;
    }

    public String MajorNames4Subject(Subject subject) {
        String result = "";
        Set<Major> majors = getMajors4Subject(subject);
        result = majors.stream().map((major) -> major.getName() + " ").reduce(result, String::concat);
        return result;
    }

    public Majorsubject getMajorsubject(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public void create(Majorsubject majorsubject) {
        getFacade().create(majorsubject);
        setDataChanged(true);
    }

    public void remove(Majorsubject majorsubject) {
        getFacade().remove(majorsubject);
        setDataChanged(true);
    }

    public void edit(Majorsubject majorsubject) {
        getFacade().edit(majorsubject);
        setDataChanged(true);
    }

    public List<Majorsubject> getQueryResultList(String sql) {
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

    public List<Majorsubject> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Majorsubject find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    @FacesConverter(forClass = Majorsubject.class)
    public static class MajorsubjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MajorsubjectController controller = (MajorsubjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "majorsubjectControllerA");
            return controller.getMajorsubject(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Majorsubject) {
                Majorsubject o = (Majorsubject) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Majorsubject.class.getName());
            }
        }
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Majorsubject> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }
}
