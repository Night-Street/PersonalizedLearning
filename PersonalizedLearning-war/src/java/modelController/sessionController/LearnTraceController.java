package modelController.sessionController;

import entities.Knowledge;
import entities.Leadpoint;
import entities.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author hadoop
 */
@Named("learnTraceController")
@SessionScoped
public class LearnTraceController extends CommonModelController implements Serializable {

    @Inject
    private modelController.applicationController.LeadpointController applicationLeadpointController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.sessionController.SubjectController subjectController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private modelController.sessionController.SubjectController subjectController1;
    List<Leadpoint> studentLeadpoints = new ArrayList<>();

    public void setDataModelList() {
        pageOperation.setDataModelList(getCurStudentLeadPoints());
    }

    public String drawCurStudentLeadpoints(Student stu) {
        String result = "digraph {"
                + "node [rx=5 ry=5 labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"] "
                + "edge [labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"]";;

        if (!studentLeadpoints.isEmpty()) {
            StringBuffer nodeStringBuffer = null;
            List<String> nodeList = new ArrayList<>();
            //画节点
            for (Leadpoint lp : studentLeadpoints) {
                nodeStringBuffer = new StringBuffer();
                List<Knowledge> knowledges = knowledges4StuSubjectMap.get(lp);
                for (Knowledge k : knowledges) {
                    nodeStringBuffer.append(((Knowledge) k).getName())
                            .append(((Knowledge) k).getId())
                            .append(" ");
                }
                nodeList.add(nodeStringBuffer.toString().trim());
                result += "\"" + nodeStringBuffer.toString().trim()
                        + "\" [labelType=\"html\" label=\"<span style='font-size:32px; color:blue; background-color:gray'>" + nodeStringBuffer.toString().trim()
                        + "</span>\"];";
            }

            //画边
            for (int i = 0; i < nodeList.size(); i++) {
                if (i != nodeList.size() - 1) {
                    result += "\"" + nodeList.get(i) + "\"->\"" + nodeList.get(i + 1) + "\";";
                }
            }
        }
        return result + "}";
    }

    private List<Leadpoint> getCurStudentLeadPoints() {
        studentLeadpoints.clear();
        if (!subjectController.isSelectedNull()) {
//            StringBuilder sb = new StringBuilder();
//            List knowledgeIdsList = applicationKnowledgeController.getKnowledgeId4Subject(subjectController.getSelected());
//            knowledgeIdsList.forEach((t) -> {
//                sb.append("'").append(t).append("',");
//            });
//            if (sb.length() > 0) {
            Student stu = studentController.getLogined();
            List<Leadpoint> tem = applicationLeadpointController.getQueryResultList("select * from leadpoint where student_id=" + stu.getId()
                    + " and subjectid=" + subjectController.getSelected().getId() + " order by create_date desc");
            tem.forEach(e -> {
                studentLeadpoints.add((Leadpoint) e);
            });
        }
        return studentLeadpoints;
    }

    public String getName4KnowledgeListFromLeadpoint(Leadpoint leadpoint) {
        List<Knowledge> knowledgeList = knowledges4StuSubjectMap.get(leadpoint);
        String result = applicationKnowledgeController.getName4KnowledgeList(knowledgeList);
        if (result.length() == 0) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Noresult"));
        }
        return result;
    }

    HashMap<Leadpoint, List<Knowledge>> knowledges4StuSubjectMap = new HashMap<>();

    public String refreshKnowledgeStructure() {
        pageOperation.refreshData(getCurStudentLeadPoints());
        knowledges4StuSubjectMap.clear();
        getCurStudentLeadPoints().forEach(leadpoint -> {
            knowledges4StuSubjectMap.put(leadpoint, applicationKnowledgeController.getKnowledgeList4Leadpoint(leadpoint));
        });
        if (studentLeadpoints.size() > 0) {
            setLearntraceString(
                    commonSession.getResourceBound().getString("Learntrace")
                    + commonSession.getResourceBound().getString("Included")
                    + commonSession.getResourceBound().getString("Number")
                    + ":"
                    + studentLeadpoints.size()
            );
        } else {
            setLearntraceString(
                    commonSession.getResourceBound().getString("No")
                    + commonSession.getResourceBound().getString("Learntrace")
            );
        }
        return null;
    }

    public HashMap<Leadpoint, List<Knowledge>> getKnowledges4StuSubjectMap() {
        return knowledges4StuSubjectMap;
    }

    private String learntraceString = "";

    public String getLearntraceString() {
        refreshKnowledgeStructure();
        return subjectController1.getSubjectName()+learntraceString;
    }

    public void setLearntraceString(String learntraceString) {
        this.learntraceString = learntraceString;
    }
}
