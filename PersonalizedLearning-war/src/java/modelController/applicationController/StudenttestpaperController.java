package modelController.applicationController;

import entities.Leadpoint;
import entities.Question;
import entities.Student;
import entities.Studenttestpaper;
import entities.Subject;
import entities.Testpaper;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import sessionBeans.StudenttestpaperFacadelocal;
import tools.StaticFields;

@Named("studenttestpaperControllerA")
@ApplicationScoped
public class StudenttestpaperController extends ApplicationCommonController {

    @Inject
    private modelController.applicationController.TestpaperController applicationTestpaperController;
    @Inject
    modelController.applicationController.QuestionController applicationQuestionController;
    @Inject
    modelController.applicationController.LeadpointController applicationLeadpointController;
    @Inject
    modelController.applicationController.StudentController applicationStudentController;
    @Inject
    modelController.applicationController.TestpaperController applicationTestpaperController1;
    @Inject
    modelController.applicationController.SubjectController applicationSubjectController;
    @EJB
    private StudenttestpaperFacadelocal ejbFacade;

    private final String practiceString = "Practice", examiniationString = "Examination";
    private String[] studentAnswerStrings;//According to testpaper, give the answer string, which is useful to show students answer on the pages

    private StudenttestpaperFacadelocal getFacade() {
        return ejbFacade;
    }

    public StudenttestpaperController() {
    }
//==================获取学生的答案========开始=================
    private String[] stduentAnswers = new String[1];
    private Studenttestpaper temTestpaper = new Studenttestpaper();

    public String[] getStudentAnswerStrings(Studenttestpaper testpaper) {
        if (null != testpaper) {
            if (null == temTestpaper.getId() || !Objects.equals(temTestpaper.getId(), testpaper.getId())) {
                //如果是取同一个试卷的内容，就不必重复计算了
                temTestpaper = testpaper;
                studentAnswerStrings = testpaper.getStudentAnswer().split(StaticFields.FIRSTDELIMITED);
            }
        }
        return studentAnswerStrings;
    }
//==================获取学生的答案========结束=================

    public String[] getSplitStrings(String answerString) {//For the multi-Selection
        return answerString.split(",");
    }

    public String getPracticeString() {
        return practiceString;
    }

    public String getExaminiationString() {
        return examiniationString;
    }

    public List<Studenttestpaper> getStudenttestpapers(Student student, Subject subject, boolean isTestpaper) {
        List<Studenttestpaper> studentTespapersList = new LinkedList<>();
        studentTespapersList.addAll(getStudenttestpapers(student, subject, false, isTestpaper));
        studentTespapersList.addAll(getStudenttestpapers(student, subject, true, isTestpaper));
        return studentTespapersList;
    }

    public List<Studenttestpaper> getStudenttestpapers(Student student, Subject subject, boolean isfinished, boolean isTestpaper) {
        List<Testpaper> candidateTestpapers = applicationTestpaperController.getTestpaper4SchoolSubject(student.getSchoolId(), subject, isTestpaper);
        List<Studenttestpaper> studentTespapersList = new LinkedList<>();
        for (Testpaper testpaper : candidateTestpapers) {
            studentTespapersList.addAll(getFacade()
                    .getQueryResultList("select * from studenttestpaper where student_id=" + student.getId()
                            + " and testpaperid=" + testpaper.getId() + " and finished=" + isfinished));
        }
        setDataChanged(false);
        return studentTespapersList;
    }

    public List<Studenttestpaper> getStudenttestpaper4Leadpoint(Leadpoint leadpoint) {
        setDataChanged(false);
        return getFacade().getStudenttestpaper4Leadpoint(leadpoint);
    }

    public List<Studenttestpaper> getStudenttestpaper4Testpaper(Testpaper testpaper) {
        setDataChanged(false);
        return getFacade().getStudenttestpaper4Testpaper(testpaper);
    }

    public List<Studenttestpaper> getStudenttestpaper4Student(Student student) {
        setDataChanged(false);
        return getFacade().getStudenttestpaper4Student(student);
    }

    public void create(Studenttestpaper studenttestpaper) {
        getFacade().create(studenttestpaper);
        setDataChanged(true);
    }

    public void remove(Studenttestpaper studenttestpaper) {
        getFacade().remove(studenttestpaper);
        setDataChanged(true);
    }

    public void edit(Studenttestpaper studenttestpaper) {
        getFacade().edit(studenttestpaper);
        setDataChanged(true);
    }

    public Studenttestpaper getStudenttestpaper(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacade.find(id);
    }

    public List<Studenttestpaper> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Studenttestpaper.class)
    public static class StudenttestpaperControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StudenttestpaperController controller = (StudenttestpaperController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studenttestpaperControllerA");
            return controller.getStudenttestpaper(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Studenttestpaper) {
                Studenttestpaper o = (Studenttestpaper) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Studenttestpaper.class.getName());
            }
        }
    }

    public List<Question> getRegular(Studenttestpaper studenttestpaper) {
        List<Question> regularTP = applicationQuestionController.getTestQuestion(studenttestpaper);
        return regularTP;
    }

    public HashMap<String, Integer> getWrongAnswerHashMap(int id) {
        String idString = String.valueOf(id);
        HashMap<String, Integer> result = new HashMap<>();
        List<Studenttestpaper> studenttestpapersList = getFacade().getQueryResultList(
                "select * from studenttestpaper where locate('," + id + "', FIRST_WRONGQUESTIONIDS)>0 or locate('" + id + ",', FIRST_WRONGQUESTIONIDS)>0"
        );
        for (Studenttestpaper studenttestpaper : studenttestpapersList) {
            String[] firstWrongQuestionIds = studenttestpaper.getFirstWrongquestionids().split(",");
            String[] studentWrongAnswers = studenttestpaper.getStudentAnswer().split(",");
            for (String wrongquestionId : firstWrongQuestionIds) {
                if (wrongquestionId.equals(idString)) {
                    // result.get(id)
                }
            }
        }
        return result;
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Studenttestpaper> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Studenttestpaper find(Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public String getName(Studenttestpaper studTstPap) {
        StringBuilder sb = new StringBuilder();
        if (studTstPap.getTestpaperId() != null && studTstPap.getTestpaperId().getSubjectids() != null) {
            String[] subjectIds = studTstPap.getTestpaperId()
                    .getSubjectids()
                    .split(",");
            for (int i = 0; i < subjectIds.length; i++) {
                sb.append(applicationSubjectController.find(Integer.parseInt(subjectIds[i])).getName()).append(",");
            }
            if (studTstPap.getTestpaperId().getStart() != null) {
                sb.append(studTstPap.getTestpaperId().getStart().toString());
            }
        }
        return sb.toString();
    }
}
