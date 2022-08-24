package modelController.sessionController;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import entities.Predicate;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("edgeamongknowledgeController")
@SessionScoped
public class EdgeamongknowledgeController extends CommonModelController<Edgeamongknowledge> implements Serializable {

    @Inject
    private modelController.applicationController.EdgeamongknowledgeController applicationEdgeamongknowledgeController;
    @Inject
    private KnowledgeController knowledgeController;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private SubjectController subjectController;
    @Inject
    private LearningresourceController learningresourceController;
    @Inject
    private PredicateController predicateController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    private List<Edgeamongknowledge> edgeamongknowledgeList = null;
    private final String tableName = "edgeamongknowledge",
            listpage = "",
            editpage = "",
            secondPage = "edgeAmongKnowledge/CreateSecondStep",
            viewpage = "";
    protected final String createpage = "edgeAmongKnowledge/Create";

    private List<Edgeamongknowledge> edgeamongknowledgesList = new LinkedList<>();
    protected Edgeamongknowledge current;
    private Knowledge relatedKnolwdge;//始终指向SPO中的当前的O，S是current

    public EdgeamongknowledgeController() {
    }

    public Edgeamongknowledge getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Edgeamongknowledge();
        }
        return current;
    }

    public void setSelected(Edgeamongknowledge edgeamongknowledge) {
        current = edgeamongknowledge;
    }

    public void setDataModelList() {
        edgeamongknowledgeList = applicationEdgeamongknowledgeController.getEdgeamongknowledgeList(subjectController.getSelected());
        pageOperation.setDataModelList(edgeamongknowledgeList);
    }

    public void refreshEdgeamongknowledgeList() {
        edgeamongknowledgeList = null;
    }

    public void create() {
        try {
            //检查该记录是否已经存在的一个解决办法是从subject获得所有的对应的knowledge，然后检查对应的前驱与后继的edgeAmongknowledge,
            //并放入SET中，然后从set中查找要create的记录，如果存在，则不create
            //但这可能会带来一个问题：随着课程这的切换，会反复从数据库读入该课程对应的所有边，带来数据库中大量记录的扇入与扇出
            //反而会增加内存与数据库带宽负担
            //因此，放弃这种方法，而用sql语句实现
            if (applicationEdgeamongknowledgeController.getQueryResultList("select * from Edgeamongknowledge where  Successornode=" + knowledgeController.getSelected().getId()
                    + " and Predecessornode=" + relatedKnolwdge.getId()
                    + " and predicate=" + predicateController.getSelected().getId()
            ).isEmpty()) {
                current.setSuccessornode(knowledgeController.getSelected());
                current.setPredecessornode(relatedKnolwdge);
                current.setPredicate(predicateController.getSelected());
                applicationEdgeamongknowledgeController.create(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                refreshEdgeamongknowledgeList();
                //每次create后，都这样访问数据库，会带来较大的负担，如何在需要访问pageOperation的时候才刷新？
                pageOperation.refreshData(applicationEdgeamongknowledgeController.getEdgeamongknowledgeList(subjectController.getSelected()));
                this.logs(current.getPredecessornode().getName() + "-" + current.getSuccessornode().getName(),
                        tableName, StaticFields.OPERATIONINSERT);
                // prepareCreate();
                learningresourceController.setIs4knowledge(false);//下一步边的资源属于边，而不是知识点
                mainXhtml.setPageName(secondPage);
            } else {
                userMessagor.addMessage(commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller edge among Knowledge 1");
        }
    }

    public void prepareCreate() {
        setSelected(new Edgeamongknowledge());
        if (knowledgeController.getSelected().getId() == null) {
            knowledgeController.prepareCreate();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Knowledge")
                    + commonSession.getResourceBound().getString("Can") + " "
                    + commonSession.getResourceBound().getString("Not") + " "
                    + commonSession.getResourceBound().getString("Be") + " "
                    + commonSession.getResourceBound().getString("Null")
            );
        } else {
            getSelected().setPredecessornode(knowledgeController.getSelected());
            selectedItemIndex = -1;
            mainXhtml.setPageName(this.createpage);
        }
    }

    public void destroy() {
        current = (Edgeamongknowledge) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(this.listpage);
    }

    private void performDestroy() {
        try {
            applicationEdgeamongknowledgeController.remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            refreshEdgeamongknowledgeList();

            //每次create后，都这样访问数据库，会带来较大的负担，如何在需要访问pageOperation的时候才刷新？
            pageOperation.refreshData(applicationEdgeamongknowledgeController.getEdgeamongknowledgeList(subjectController.getSelected()));

            updateCurrentItem();
            edgeamongknowledgeList.remove(current);
            pageOperation.refreshData(edgeamongknowledgeList);
            this.logs(current.getPredecessornode().getName() + "-" + current.getSuccessornode().getName(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "Controller edge among knowledge 2");
        }
    }

    private void updateCurrentItem() {
        int count = applicationEdgeamongknowledgeController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pageOperation.getPagination().getPageFirstItem() >= count) {
                pageOperation.getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected((Edgeamongknowledge) applicationEdgeamongknowledgeController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public List<Edgeamongknowledge> getPrecessorEdgeamongknowledge(Knowledge knowledge) {
        return applicationEdgeamongknowledgeController.getEdgeamongknowledges(knowledge, StaticFields.PREDCESSOR);
    }
    public List<Edgeamongknowledge> getSuccorEdgeamongknowledge(Knowledge knowledge) {
        return applicationEdgeamongknowledgeController.getEdgeamongknowledges(knowledge, StaticFields.SUCCESSOR);
    }

    public Edgeamongknowledge getEdgeamongknowledge(java.lang.Integer id) {
        return (Edgeamongknowledge) applicationEdgeamongknowledgeController.find(id);
    }

    //  int i = 0;
    public void completePreKnowledgeSelection(Knowledge knowledge, HashSet<Knowledge> knowledgeSet) {
        //原来的
        Set<Knowledge> existedKnowledges_A = applicationKnowledgeController.getPredcessorKnowledges(knowledge);
        //现在的
        Set<Knowledge> nowSelectedKnowledges_B = knowledgeSet;
        //得到要删除的
        Set<Knowledge> removedKnowledges = new HashSet<>();
        removedKnowledges.addAll(existedKnowledges_A);
        removedKnowledges.removeAll(nowSelectedKnowledges_B);
        //执行删除
        removedKnowledges.forEach((knowledge1) -> {
            applicationEdgeamongknowledgeController.executUpdate("delete from edgeamongknowledge where successornode=" + knowledge.getId()
                    + " and predecessornode=" + knowledge1.getId());
            this.logs("pre" + knowledge1.getName() + "- post" + knowledge.getName(),
                    tableName, StaticFields.OPERATIONDELETE);

        });
        //得到要添加的
        nowSelectedKnowledges_B.removeAll(existedKnowledges_A);
        nowSelectedKnowledges_B.forEach((Knowledge knowledge1) -> {
            if (null != knowledge.getId()) {
                Edgeamongknowledge edgeAmongknowledge = new Edgeamongknowledge();
                edgeAmongknowledge.setSuccessornode(knowledge);
                edgeAmongknowledge.setPredecessornode(knowledge1);
                applicationEdgeamongknowledgeController.create(edgeAmongknowledge);
                refreshEdgeamongknowledgeList();
                this.logs("pre" + knowledge1.getName() + "- post" + knowledge.getName(),
                        tableName, StaticFields.OPERATIONINSERT);
            }
        });
    }

    //  int i = 0;
    Set<Edgeamongknowledge> existedKnowledges_A = new HashSet<>();

    public void completePredicateKnowledge(Knowledge knowledge, Collection<Edgeamongknowledge> edgeamongknowledgesSet) {
        //把新的添加进来，把旧的删除出去
        existedKnowledges_A.clear();
        existedKnowledges_A.addAll(applicationEdgeamongknowledgeController.getByPredecessor(knowledge));

        List<Edgeamongknowledge> toberemoved = new LinkedList<>();

        existedKnowledges_A.forEach(edge -> {
            Edgeamongknowledge tem = this.contains(edgeamongknowledgesSet, edge);
            if (null == tem) {// 新的集合中不包含该元素，所以需要删除
                toberemoved.add(edge);
            }
        });
        //再求需要添加的
        List<Edgeamongknowledge> tobeadded = new LinkedList<>();
        edgeamongknowledgesSet.forEach(edge -> {//id不一样，所以不能直接用removeAll，而采用检查是否存在的办法
            Edgeamongknowledge tem = this.contains(existedKnowledges_A, edge);
            if (null == tem) {//旧的集合中不存在这个新的，那么是需要添加的
                tobeadded.add(edge);//共同都有的，需要保留，不需要再create了
            }
        });

        //删除要删除的
        toberemoved.forEach(edge -> {
            applicationEdgeamongknowledgeController.remove(edge);
            this.logs("subject" + knowledge.getName() + "-" + edge.getPredicate().getPname() + "- object" + edge.getPredecessornode().getName(),
                    tableName, StaticFields.OPERATIONDELETE);
        });
        tobeadded.forEach(edge -> {
            applicationEdgeamongknowledgeController.create(edge);
            this.logs("pre" + knowledge.getName() + "-" + edge.getPredicate().getPname() + "- post" + edge.getPredecessornode().getName(),
                    tableName, StaticFields.OPERATIONINSERT);
        });
    }

    public String cutEdge(Knowledge currentKnowledge) {
        if (!(currentKnowledge.getSubjectId().getId().equals(subjectController.getSelected().getId()))) {
            //1. get current subject
            //2. get knowledge of this subject
            //3. get edges contain these knowledges
            //4. remove those edges that precessor and successor are from different subject
            Set<Edgeamongknowledge> toBeRemoved = new HashSet<>();
            //1. get current subject
            //2. get knowledge of this subject
            List<Knowledge> subjectKnowledges = applicationKnowledgeController.getKnowledgeList4Subject(subjectController.getSelected());
            //3. get edges contain these knowledges
            for (Knowledge knowledge : subjectKnowledges) {
                Set<Edgeamongknowledge> edgeamongknowledges = knowledge.getPredcessorKnowledgeSet();
                for (Edgeamongknowledge edgeamongknowledge : edgeamongknowledges) {
                    if (!edgeamongknowledge.getPredecessornode().getSubjectId().equals(subjectController.getSelected())) {
                        toBeRemoved.add(edgeamongknowledge);
                    }
                }
                Set<Edgeamongknowledge> edgeamongknowledges1 = knowledge.getSuccessorknowledgeSet();
                for (Edgeamongknowledge edgeamongknowledge : edgeamongknowledges1) {
                    if (!edgeamongknowledge.getSuccessornode().getSubjectId().equals(subjectController.getSelected())) {
                        toBeRemoved.add(edgeamongknowledge);
                    }
                }
            }
            //4. remove those edges that precessor and successor are from different subject       
            for (Edgeamongknowledge edgeamongknowledge : toBeRemoved) {
                applicationEdgeamongknowledgeController.remove(edgeamongknowledge);
            }
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
        }
        return null;
    }

    private Edgeamongknowledge contains(Collection<Edgeamongknowledge> knowledgeSet, Edgeamongknowledge edge) {
        Iterator it = knowledgeSet.iterator();
        while (it.hasNext()) {
            Edgeamongknowledge ekl = (Edgeamongknowledge) it.next();
            if (ekl.getPredecessornode().equals(edge.getPredecessornode()) && ekl.getSuccessornode().equals(edge.getSuccessornode()) && ekl.getPredicate().equals(edge.getPredicate())) {
                return ekl;
            }
        }
        return null;
    }

    HashSet<EdgeAmongKnowledgeWithoutId> temEdgeAmongKnowledges = new HashSet<>();

    class EdgeAmongKnowledgeWithoutId {

        Knowledge preKnowledge, succKnowledge;
        Predicate predicate;

        @Override
        public boolean equals(Object e) {
            EdgeAmongKnowledgeWithoutId o = (EdgeAmongKnowledgeWithoutId) e;
            return this.preKnowledge.equals(o.preKnowledge) && this.succKnowledge.equals(o.succKnowledge) && o.predicate.equals(o.predicate);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.preKnowledge);
            hash = 29 * hash + Objects.hashCode(this.succKnowledge);
            hash = 29 * hash + Objects.hashCode(this.predicate);
            return hash;
        }
    }

    public void removeDuplicate() {
        //查找重复记录，并删除
        HashMap<EdgeAmongKnowledgeWithoutId, Integer> record = new HashMap<>();
        int count = applicationEdgeamongknowledgeController.count();
        List<Edgeamongknowledge> allEdgeamongknowledges = applicationEdgeamongknowledgeController.findRange(new int[]{0, count});
        List<Edgeamongknowledge> removed = new LinkedList<>();
        allEdgeamongknowledges.forEach(edge -> {
            EdgeAmongKnowledgeWithoutId tem = new EdgeAmongKnowledgeWithoutId();
            tem.preKnowledge = edge.getPredecessornode();
            tem.succKnowledge = edge.getSuccessornode();
            tem.predicate = edge.getPredicate();
            if (record.containsKey(tem)) {
                record.put(tem, record.get(tem) + 1);
                removed.add(edge);
            } else {
                record.put(tem, 1);
            }
        });
        for (Edgeamongknowledge edge : removed) {
            applicationEdgeamongknowledgeController.remove(edge);
        }
    }

    public void saveLearningResource() {
        try {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            mainXhtml.setPageName(this.createpage);
            this.logs(current.toString(), tableName, StaticFields.OPERATIONUPDATE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "edgeAmongKnowledge Create");
            mainXhtml.setPageName(this.createpage);
        }
    }

    public Knowledge getRelatedKnolwdge() {
        return relatedKnolwdge;
    }

    public void setRelatedKnolwdge(Knowledge relatedKnolwdge) {
        this.relatedKnolwdge = relatedKnolwdge;
    }
}
