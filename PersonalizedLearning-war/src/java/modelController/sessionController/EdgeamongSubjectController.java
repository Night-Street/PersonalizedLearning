package modelController.sessionController;

import entities.Edgeamongsubject;
import entities.Subject;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("edgeamongsubjectController")
@SessionScoped
public class EdgeamongSubjectController extends CommonModelController<Edgeamongsubject> implements Serializable {

    @Inject
    private SubjectController subjectController;
    @Inject
    private modelController.applicationController.EdgeamongSubjectController applicationEdgeamongSubjectController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    private tools.UserMessagor userMessagor;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private final String tableName = "Edgeamongsubject", listpage = "", editpage = "", viewpage = "", createpage = "";

    public EdgeamongSubjectController() {
    }
    protected Edgeamongsubject current;

    public Edgeamongsubject getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Edgeamongsubject();
        }
        return current;
    }

    public void setSelected(Edgeamongsubject edgeamongsubject) {
        current = edgeamongsubject;
    }

    public void create() {
        try {
            //检查该记录是否已经存在的一个解决办法是从edgeamongsubject检查对应的前驱与后继
            //并放入SET中，然后从set中查找要create的记录，如果存在，则不create

            if (applicationEdgeamongSubjectController.getQueryResultList("select * from Edgeamongsubject where  Predecessornode=" + current.getPredecessornode().getId() + ", Successornode=" + current.getSuccessornode().getId()).isEmpty()) {
                applicationEdgeamongSubjectController.create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //JsfUtil.addSuccessMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                //applicationEdgeamongSubjectController.refreshEdgeamongsubjectList();
                this.logs(current.getPredecessornode().getName() + "-" + current.getSuccessornode().getName(),
                        tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
                //JsfUtil.addErrorMessage(new Exception(), commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed")
                    + "Controller edge among subject 1");
//            JsfUtil.addErrorMessage(e, commonSession.getResourceBound().getString("Failed")+
//                    "Controller edge among subject 1");
        }
    }

    public void prepareCreate() {
        setSelected(new Edgeamongsubject());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
    }

    public void destroy() {
        current = (Edgeamongsubject) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(this.listpage);
    }

    private void performDestroy() {
        try {
            applicationEdgeamongSubjectController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            //JsfUtil.addSuccessMessage(commonSession.getResourceBound().getString("Succeed"));
            //applicationEdgeamongSubjectController.refreshEdgeamongsubjectList();
            updateCurrentItem();
            this.logs(current.getPredecessornode().getName() + "-" + current.getSuccessornode().getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller edge among subject 2");
            //JsfUtil.addErrorMessage(e, commonSession.getResourceBound().getString("Failed")+"Controller edge among subject 2");
        }
    }

    private void updateCurrentItem() {
        int count = applicationEdgeamongSubjectController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }

        if (selectedItemIndex >= 0) {
            setSelected((Edgeamongsubject) applicationEdgeamongSubjectController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public void completePreSubjectSelection() {
        //原来的
        Set<Subject> existedSubjects_A = subjectController.getPredcessorSubjects();
        //现在的
        Set<Subject> nowSelectedSubjects_B = subjectController.getSelectedParentsSubjects();
        //得到要删除的
        Set<Subject> removedSubjects = new HashSet<>();
        removedSubjects.addAll(existedSubjects_A);
        removedSubjects.removeAll(nowSelectedSubjects_B);
        //执行删除
        removedSubjects.forEach((subject) -> {
            applicationEdgeamongSubjectController.executUpdate("delete from Edgeamongsubject where successornode=" + subjectController.getSelected().getId()
                    + " and predecessornode=" + subject.getId());
            this.logs("pre" + subject.getName() + "- post" + subjectController.getSelected().getName(),
                    tableName, StaticFields.OPERATIONDELETE);

        });
        //得到要添加的
        nowSelectedSubjects_B.removeAll(existedSubjects_A);
        nowSelectedSubjects_B.forEach((Subject knowledge) -> {
            if (null != subjectController.getSelected().getId()) {
                Edgeamongsubject edgeAmongknowledge = new Edgeamongsubject();
                edgeAmongknowledge.setSuccessornode(subjectController.getSelected());
                edgeAmongknowledge.setPredecessornode(knowledge);
                applicationEdgeamongSubjectController.create(edgeAmongknowledge);
                //          subjectController.evict(knowledge);
                //          i++;
                this.logs("pre" + knowledge.getName() + "- post" + subjectController.getSelected().getName(),
                        tableName, StaticFields.OPERATIONINSERT);
            }
        });
    }

}
