package modelController.sessionController;

import entities.Knowledge;
import entities.Leadpoint;
import entities.Question;
import entities.Subject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("leadpointController")
@SessionScoped
public class LeadpointController extends CommonModelController<Leadpoint> implements Serializable {

    private List<Leadpoint> leadpointList = null;
    private final String tableName = "leadpoint", listpage = "", editpage = "", viewpage = "", createpage = "";
    @Inject
    private SubjectController subjectController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.LeadpointController applicationLeadpointController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;

    public void setDataModelList() {
        pageOperation.setDataModelList(getLeadpointList());
    }

    public LeadpointController() {
    }
    protected Leadpoint current;

    public Leadpoint getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Leadpoint();
        }
        return current;
    }
    public void setSelected(Leadpoint currentL) {
        current = currentL;
    }

    public void create() {
        try {
            if (null == applicationLeadpointController.findByKnowledgeIds(current.getKnowledgeId().trim())) {
                applicationLeadpointController.create(current);
                refreshLeadpointList();
                //evictForeignKey();
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                this.logs(current.getId().toString(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getStudentId().getName() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }

        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller lead pointi 1");
        }
    }

    public void prepareCreate() {
        setSelected(new Leadpoint());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
    }

    public void destroy() {
        current = (Leadpoint) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(this.listpage);
    }

    private void performDestroy() {
        try {
            applicationLeadpointController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("LeadpointDeleted"));
            refreshLeadpointList();
            updateCurrentItem();
            //leadpointList.remove(current);
            //evictForeignKey();
            pageOperation.refreshData(getLeadpointList());
            this.logs("student:" + current.getStudentId().getId(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller lead pointi 2");
        }
    }

    private void updateCurrentItem() {
        int count = applicationLeadpointController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((Leadpoint) applicationLeadpointController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public List<Leadpoint> getLeadpointList() {
        if (null == leadpointList || leadpointList.isEmpty()) {
            if (null != current && null != current.getStudentId() && null != current.getSubjectid()) {
                this.leadpointList = applicationLeadpointController.getAllLeadpointList(current.getStudentId(), current.getSubjectid());
            }
        }
        return leadpointList;
    }

    public void refreshLeadpointList() {
        leadpointList = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationLeadpointController.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationLeadpointController.findAll(), true);
    }

    public void updateLeadPoint(Set<Question> correctQuestionList, Set<Question> wrongQuestionList) {
        /**
         * 知识点前沿KNF更新步骤如下：\n 1） 在t时，由对题集得到知识点集合K1；\n 2）
         * 由K1得到其后继集合Kc，该集合满足互斥性质；如果不满足互斥性质，则删除层次较高的知识点，即属于其他知识点后继的知识点，使其满足； \n
         * 3） 由错题集得到知识点集合Kw，该集合满足互斥性质；如果不满足，处理方法如上； \n 4） 从K1.removeAll(Kw) 5)
         * 从KNF(t)中删除K1； \n 6) 合并集合Kc,Kw,得到满足互斥性质的集合K1(t+1)；\n 7）
         * KNF(t+1)=KNF(t)\K1∪K1(t+1)\n
         */
        //1）	在t时，由对题集得到知识点集合K1；\n  
        Set<Knowledge> k1 = applicationKnowledgeController.getKnowledges4Question(correctQuestionList);
        //2）	从KNF(t)中删除K1； \n 
        Set<Knowledge> currentKnowledgeSet = new HashSet<>(applicationKnowledgeController.getKnowledgesList4LeadingPoint(getSelected()));
        currentKnowledgeSet.removeAll(k1);
        //3）  由K1得到其后继集合Kc，该集合满足互斥性质；
        //放到后面统一处理:如果不满足互斥性质，则删除层次较高的知识点，即属于其他知识点后继的知识点，使其满足；
        Set<Knowledge> kc = new HashSet<>();
        for (Knowledge knowledge : k1) {
            kc.addAll(applicationKnowledgeController.getSuccessorKnowledges(knowledge));
        }
        //4）  由错题集得到知识点集合Kw，该集合满足互斥性质；如果不满足，处理方法如上；  
        Set<Knowledge> kw = applicationKnowledgeController.getKnowledges4Question(wrongQuestionList);
        //5）  合并集合Kc,Kw,得到满足互斥性质的集合K1(t+1)； 
        kc.addAll(kw);
        //如果不满足互斥性质，则删除层次较高的知识点，即属于其他知识点后继的知识点，使其满足
        applicationLeadpointController.constraintCheck(kc);
        //生成新的知识点前沿
        String knowledgeIds = "";
        for (Knowledge knowledge : kc) {
            knowledgeIds += knowledge.getId() + ",";
        }
        if (knowledgeIds.length() > 0) {
            Leadpoint leadpoint = new Leadpoint();
            leadpoint.setSubjectid(subjectController.getSelected());
            leadpoint.setStudentId(studentController.getLogined());

            knowledgeIds = knowledgeIds.substring(1, knowledgeIds.length());
            leadpoint.setKnowledgeId(knowledgeIds);
            applicationLeadpointController.create(leadpoint);
            setSelected( leadpoint);
        } 
    }
    List<Knowledge> knowledges = new ArrayList<>();

    public List<Knowledge> knowledgeListBySubjectId(Subject subject) {
        List<Leadpoint> leadpoints = new ArrayList<>();
        Leadpoint lp = new Leadpoint();
        knowledges.clear();
        if (subject != null && subject.getId() != null) {
            leadpoints = applicationLeadpointController.getQueryResultList("select * from leadpoint where subjectid=" + subject.getId() + " and student_id=" + studentController.getLogined().getId() + " order by id desc");
            if (!leadpoints.isEmpty()) {
                lp = (Leadpoint) leadpoints.get(0);
                knowledges = new LinkedList<>();
                List<Knowledge> tem = applicationKnowledgeController.getQueryResultList("select * from knowledge where id in(" + lp.getKnowledgeId() + ")");
                tem.forEach(e -> {
                    knowledges.add((Knowledge) e);
                });
            }
        }
        return knowledges;
    }

}
