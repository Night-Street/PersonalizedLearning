package modelController.sessionController;

import entities.Major;
import entities.Majorsubject;
import entities.Subject;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import tools.StaticFields;

@Named("majorsubjectController")
@SessionScoped
public class MajorsubjectController extends CommonModelController<Majorsubject> implements Serializable {


    @Inject
    private MajorController majorController;
    @Inject
    private SubjectController subjectController;
    @Inject
    private modelController.applicationController.MajorsubjectController applicationMajorsubjectController;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    //HashMap<Major, List<Subject>> majorSubjectMap = new LinkedHashMap<>();
    //HashMap<Subject, List<Major>> subjectMajorMap = new LinkedHashMap<>();
    //For the major: set the subject
    private HashSet<Subject> selectedSubject = new HashSet<>();
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
  @Inject
    private tools.UserMessagor userMessagor;
    private final String tableName = "majorsubject";

    public MajorsubjectController() {
    }
    protected Majorsubject current;

    public Majorsubject getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Majorsubject();
        }
        return current;
    }
    List<Majorsubject> majorSubjectListLocal = new LinkedList<>();

    public HashSet<Subject> getSelectedSubject() {
        selectedSubject.clear();
        majorSubjectListLocal = applicationMajorsubjectController.getMajorsubjects4Major(majorController.getSelected());
        majorSubjectListLocal.forEach((majorsubject) -> {
            selectedSubject.add(majorsubject.getSubjectid());
        });
        return selectedSubject;
    }

    public void setSelectedSubject(HashSet<Subject> selectedSubject) {
        this.selectedSubject = selectedSubject;
    }

    public void completeSubjectSelection() {
        Set<Subject> existedSubjectsSet_A = majorController.getSubjectsOfSelectedMajor();
        Set<Subject> nowSelectedSubjects_B = selectedSubject;
        //get those to be removed:
        Set<Subject> subjectTobeRemoved = new HashSet<>();
        subjectTobeRemoved.addAll(existedSubjectsSet_A);
        subjectTobeRemoved.removeAll(nowSelectedSubjects_B);
        for (Subject subject : subjectTobeRemoved) {
            applicationMajorsubjectController.executUpdate("delete from majorsubject where majorid=" + majorController.getSelected().getId()
                    + " and subjectId=" + subject.getId());
            try {
                this.logs(current.getMajorid().getName() + "-" + current.getSubjectid().getName(), tableName, StaticFields.OPERATIONDELETE);
            } catch (Exception e) {
            }
        }
        //get those to be added:
        nowSelectedSubjects_B.removeAll(existedSubjectsSet_A);
        for (Subject subject : nowSelectedSubjects_B) {
            Majorsubject majorSubject = new Majorsubject();
            majorSubject.setSubjectid(subject);
            majorSubject.setMajorid(majorController.getSelected());
            applicationMajorsubjectController.create(majorSubject);
            this.logs(majorSubject.getMajorid().getName() + "-" + majorSubject.getSubjectid().getName(), tableName, StaticFields.OPERATIONINSERT);
        }
        applicationMajorsubjectController.refreshMajorSubjectMap();
    }

    public void refresh() {
        selectedMajors = null;
    }
    //select major for subject
    private Set<Major> selectedMajors = new HashSet<>(), oldMajors;

//Only for the subjects
    public Set<Major> getSelectedMajors() {
        if (null == selectedMajors) {
            selectedMajors = new HashSet<>();
            if (subjectController.getSelected().getId()!=null) {
                Set<Majorsubject> majorsubjects = subjectController.getSelected().getMajorsubjectSet();
                majorsubjects.forEach(majorsubt -> {
                    selectedMajors.add(majorsubt.getMajorid());
                });
            }
        }
        return selectedMajors;
    }

    public void setSelectedMajors(Set<Major> selectedMajors) {
        this.selectedMajors = selectedMajors;
    }

    List<Major> oldMajorlocal = new LinkedList<>();

    public void completeMajorSelection() {
        //old MajorSet
        List<Majorsubject> majorsubject4Subject = applicationMajorsubjectController.getMajorsubjects4Subject(subjectController.getSelected());

        oldMajorlocal.clear();
        majorsubject4Subject.forEach(ms -> {
            oldMajorlocal.add(ms.getMajorid());
        });
        Set<Major> existedMajorSet_A;
        if (null != oldMajorlocal) {
            existedMajorSet_A = new HashSet<>(oldMajorlocal);
        } else {
            existedMajorSet_A = new HashSet<>();
        }
        //new MajorSet
        Set<Major> selectedMajorSet_B = selectedMajors;
        Set<Major> majorTobeRemoved = new HashSet<>();
        majorTobeRemoved.addAll(existedMajorSet_A);
        majorTobeRemoved.removeAll(selectedMajorSet_B);
        //remove deselected
        for (Major major : majorTobeRemoved) {
            applicationMajorsubjectController.executUpdate("delete from majorsubject where majorid=" + major.getId()
                    + " and subjectId=" + subjectController.getSelected().getId(), major, subjectController.getSelected(), StaticFields.OPERATIONDELETE);
            // majorController.evict(major);
            this.logs(major.getName() + "-" + subjectController.getSelected().getName(), tableName, StaticFields.OPERATIONDELETE);
        }
        //Add new selected
        selectedMajorSet_B.removeAll(existedMajorSet_A);
        for (Major major : selectedMajorSet_B) {
            Majorsubject majorSubject = new Majorsubject();
            majorSubject.setSubjectid(subjectController.getSelected());
            majorSubject.setMajorid(major);
            applicationMajorsubjectController.create(majorSubject);
            getSelectedMajors().add(major);
//            majorController.evict(major);
            // subjectController.evict(subjectController.getSelected());
            this.logs(major.getName() + "-" + subjectController.getSelected().getName(), tableName, StaticFields.OPERATIONINSERT);
        }
        applicationMajorsubjectController.refreshMajorSubjectMap();
        userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
    }

}
