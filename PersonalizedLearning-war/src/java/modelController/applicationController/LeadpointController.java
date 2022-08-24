package modelController.applicationController;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import entities.Leadpoint;
import entities.Student;
import entities.Subject;

import java.util.Calendar;
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
import sessionBeans.LeadpointFacadeLocal;
import tools.StaticFields;

@Named("leadpointControllerA")
@ApplicationScoped
public class LeadpointController extends ApplicationCommonController {

    @EJB
    private sessionBeans.LeadpointFacadeLocal ejbFacadelocal;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.EdgeamongknowledgeController applicationEdgeamongknowledgeController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;

    public LeadpointController() {
    }

    private LeadpointFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    public List<Leadpoint> getAllLeadpointList(Student student, Subject subject) {
        setDataChanged(false);
        return getFacade().getQueryResultList("select * from Leadpoint where subjectid="
                + subject.getId() + "and student_id=" + student.getId() + " order by id desc");
    }

    //如果不满足互斥性质，则删除层次较高的知识点，即属于其他知识点后继的知识点，使其满足；
    public void constraintCheck(Set<Knowledge> knowledgeSet) {
        HashSet<Knowledge> result = new HashSet();
        for (Knowledge ki : knowledgeSet) {
            for (Knowledge kj : knowledgeSet) {
                if (!ki.equals(kj)) {
                    if (applicationKnowledgeController.getSuccessorKnowledges(ki).contains(kj)) {
                        result.add(kj);//kj is in the set of sucessor of ki
                    }
                }
            }
        }
        knowledgeSet.removeAll(result);
    }

    public Leadpoint getNextLeadpoint(Leadpoint leadpoint) {
        Leadpoint result = new Leadpoint();
        result.setStudentId(leadpoint.getStudentId());
        result.setSubjectid(leadpoint.getSubjectid());
        Set<Knowledge> temKnowledges = new HashSet<>(applicationKnowledgeController.getKnowledgesList4LeadingPoint(leadpoint));
        constraintCheck(temKnowledges);
        result.setKnowledgeId(applicationKnowledgeController.getKnowledgeString(temKnowledges));
        return result;
    }

    public boolean isEnd(Leadpoint leadpoint) {
        boolean result = false;
        if (!leadpoint.getKnowledgeId().contains(",")) {
            Knowledge temKnowledge = applicationKnowledgeController.getKnowledge(Integer.parseInt(leadpoint.getKnowledgeId()));
            if (applicationKnowledgeController.getSuccessorKnowledges(temKnowledge).isEmpty()) {
                result = true;
            }
        }
        return result;
    }

    public List<Leadpoint> getKnowledge4Subject(Subject subject) {
        setDataChanged(false);
        return getFacade().getLeadpoint4Subject(subject);
    }

    //====================================get Leadpoint based on subject========================
    public Leadpoint getLeadpointFront4Subject4Student(Subject subject, Student student) {
        Leadpoint leadpoint = null;
        if (null != subject && null != subject.getId()) {
            //根据学生id和课程id选定知识点前沿id（保存着以前的知识点前沿），取出最大的id号的一行记录
            //一个知识点前沿有多张试卷，取id号最大的testpaper
            //显然下面的执行SQL语句的方法比在内存中查找更节约数据库服务器带宽
            // int maxLeadpoint = 0;
            String sqlString = "select * from LEARNING.LEADPOINT where id in ("
                    + "select max(id) from LEARNING.LEADPOINT where student_id="
                    + student.getId() + " and subjectid=" + subject.getId() + ")";
            List<Leadpoint> leadpointsList = getFacade().getQueryResultList(sqlString);
            if (!leadpointsList.isEmpty()) {
                leadpoint = leadpointsList.get(0);
                //  maxLeadpoint = leadpointsList.get(0).getId();
            } else {//还未初始化
                Knowledge startKnowledge = applicationKnowledgeController.getStartKnowledge(subject);
                StringBuilder sb = new StringBuilder();
                List<Edgeamongknowledge> startEdgeamongknowledges = applicationEdgeamongknowledgeController.getEdgeamongknowledges(startKnowledge, StaticFields.SUCCESSOR);
                for (Edgeamongknowledge edge : startEdgeamongknowledges) {
                    sb.append(",").append(edge.getSuccessornode().getId());
                }
                String leadpointsKnowledgesIds = sb.toString();
                leadpointsKnowledgesIds = leadpointsKnowledgesIds.contains(",") ? leadpointsKnowledgesIds.substring(1) : "";
                if (leadpointsKnowledgesIds.length() > 0) {
                    leadpoint = new Leadpoint();
                    leadpoint.setKnowledgeId(leadpointsKnowledgesIds);
                    leadpoint.setStudentId(student);
                    leadpoint.setSubjectid(subject);
                    //因为确定这个知识点前沿还不存在，所以可以直接生成
                    getFacade().create(leadpoint);
                } else {//There is no successor node
                    userMessagor.addMessage(commonSession.getResourceBound().getString("KnowledgeOver"));
                    //JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("KnowledgeOver"));
                }
            }
        } else {//redirect to the initknowledge
            userMessagor.addMessage(commonSession.getResourceBound().getString("First")
                    + commonSession.getResourceBound().getString("Select")
                    + commonSession.getResourceBound().getString("Subject"));
//            JsfUtil.addErrorMessage(
//                    commonSession.getResourceBound().getString("First")
//                    + commonSession.getResourceBound().getString("Select")
//                    + commonSession.getResourceBound().getString("Subject"));
        }
        return leadpoint;
    }

    public Leadpoint getLeadpoint(java.lang.Integer id) {
        setDataChanged(false);
        return ejbFacadelocal.find(id);
    }

    public void create(Leadpoint leadpoint) {
        if (leadpoint.getKnowledgeId().length() > 0) {
//            userMessagor.addMessage(commonSession.getResourceBound().getString("Leadpoint")
//                    + commonSession.getResourceBound().getString("Create")
//                    + commonSession.getResourceBound().getString("Failed"));
//            JsfUtil.addErrorMessage(commonSession.getResourceBound().getString("Leadpoint")
//                    + commonSession.getResourceBound().getString("Create")
//                    + commonSession.getResourceBound().getString("Failed"));

            leadpoint.setCreateDate(Calendar.getInstance().getTime());
            List<Leadpoint> lpl = ejbFacadelocal.getQueryResultList("select * from leadpoint where student_id=" + leadpoint.getStudentId().getId() + " and subjectid=" + leadpoint.getSubjectid().getId());
            if (null != lpl && !lpl.isEmpty()) {
                Leadpoint lp = lpl.get(0);
                if (lp.getKnowledgeId().equals(leadpoint.getKnowledgeId())) {
                    getFacade().create(leadpoint);
                    setDataChanged(true);
                }
            }
        }
    }

    public void remove(Leadpoint leadpoint) {
        getFacade().remove(leadpoint);
        setDataChanged(true);
    }

    public void edit(Leadpoint leadpoint) {
        getFacade().edit(leadpoint);
        setDataChanged(true);
    }

    public List<Leadpoint> getQueryResultList(String sql) {
        setDataChanged(false);
        return getFacade().getQueryResultList(sql);
    }

    @FacesConverter(forClass = Leadpoint.class)
    public static class LeadpointControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LeadpointController controller = (LeadpointController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "leadpointControllerA");
            return controller.getLeadpoint(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Leadpoint) {
                Leadpoint o = (Leadpoint) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Leadpoint.class.getName());
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

    public List<Leadpoint> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public List<Leadpoint> findAll() {
        setDataChanged(false);
        return getFacade().findAll();
    }

    public Leadpoint find(java.lang.Integer id) {
        setDataChanged(false);
        return getFacade().find(id);
    }

    public Leadpoint findByKnowledgeIds(String knowledgesId) {
        setDataChanged(false);
        return getFacade().findByKnowledgeIds(knowledgesId);
    }
}
