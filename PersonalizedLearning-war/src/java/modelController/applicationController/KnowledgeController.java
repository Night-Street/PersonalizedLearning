package modelController.applicationController;

import entities.Edgeamongknowledge;
import entities.Knowledge;
import entities.Leadpoint;
import entities.Question;
import entities.Subject;
import entities.TeacherAdmin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import sessionBeans.KnowledgeFacadeLocal;
import tools.StaticFields;

@Named("knowledgeControllerA")
@ApplicationScoped
public class KnowledgeController {

    @EJB
    private sessionBeans.KnowledgeFacadeLocal ejbFacadelocal;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private EdgeamongknowledgeController applicationEdgeamongknowledgeController;
    @Inject
    private LeadpointController applicationLeadpointController;

    public KnowledgeController() {
    }

    public KnowledgeFacadeLocal getFacade() {
        return ejbFacadelocal;
    }

    //-------------------------------For the search and viewall command button------------------
    //Have an int to check whether the subject is changed, if not, omit the execution of the SQL
    // private int selectedSubjectId = 0;
    public List<Knowledge> getKnowledgeList4Subject(Subject subject) {
        //From major to subject, then to knowledge, therefore, first get the subject===========
        List<Knowledge> knowledgesList = new LinkedList<>();
        if (null != subject && null != subject.getId()) {
            knowledgesList = getFacade().getQueryResultList("select * from knowledge  where subject_id=" + subject.getId() + " order by name");
            //setDataChanged(false);
        }
        return knowledgesList;
    }

    public List<Integer> getKnowledgeId4Subject(Subject subject) {
        if (null != subject) {
            //setDataChanged(false);
            //return subject.getKnowledgeSet();
            return getFacade().getQueryResultIDsList("select id from knowledge where subject_id=" + subject.getId());
        }
        return null;
    }

    public boolean isKnowledgeExisted(String knowledgeName, Subject subject) {
        //setDataChanged(false);
        List<Integer> ids = getFacade().getQueryResultIDsList("select id from knowledge where name='" + knowledgeName.trim() + "' and subject_id=" + subject.getId());
        return (null != ids && ids.size() > 0);
    }

    public Knowledge getStartKnowledge(Subject subject) {
        Knowledge result = null;
        if (null != subject && null != subject.getId()) {
            //setDataChanged(false);
            List<Knowledge> knowledges = getFacade().getQueryResultList("select * from knowledge where subject_id=" + subject.getId() + "   FETCH  FIRST ROW ONLY");
            if (null != knowledges && knowledges.size() > 0) {
                result = knowledges.get(0);
            }
        }
        return result;
    }

    public List<Knowledge> searchByString(String searchedString, int subjectid) {
        //setDataChanged(false);
        List<Knowledge> result = new LinkedList<>();
        if (null != searchedString && searchedString.trim().length() > 0) {
            String searchString = "select * from knowledge where "
                    + "(locate('" + searchedString.toLowerCase() + "',LOWER(name))>0 or "
                    + "locate('" + searchedString.toLowerCase() + "',LOWER(details))>0)";
            if (0 == subjectid) {
                result = getFacade().getQueryResultList(searchString);
            } else {
                result = getFacade().getQueryResultList(searchString + " and  subject_id=" + subjectid);
            }
        }
        return result;
    }

    public List<Knowledge> findByName(String name) {
        // setDataChanged(false);
        return getFacade().findByName(name);
    }

    public String getName4KnowledgeList(List<Knowledge> knowledges) {
        StringBuilder sb = new StringBuilder();
        if (null != knowledges) {
            knowledges.forEach(knw -> {
                sb.append(knw.getName()).append("|");
            });
        }
        return sb.toString();
    }

    public Knowledge getKnowledge(java.lang.Integer id) {
        return ejbFacadelocal.find(id);
    }

    public List<Knowledge> getKnowledgesList4LeadingPoint(Leadpoint leadpoint) {
        if (null == leadpoint || leadpoint.getKnowledgeId() == null || leadpoint.getKnowledgeId().equals("")) {
            return new ArrayList<>();
        } else {
            ArrayList<Knowledge> knowledgeList = new ArrayList<>();
            String[] knowledges = leadpoint.getKnowledgeId().split(",");
            //setDataChanged(false);
            for (String knowledge : knowledges) {
                knowledgeList.add(getFacade().find(Integer.valueOf(knowledge)));
            }
            return knowledgeList;
        }

    }
    //-------------------------------
    //================For the parents knowledge ===============
    //temKnowledges is those being selected in the manyListbox
    private HashSet<Knowledge> predcessorKnowledgeSet = new HashSet<>(), candidateKnowledge = new HashSet<>();

    //获得前驱
    public HashSet<Knowledge> getPredcessorKnowledges(Knowledge knowledge) {
        predcessorKnowledgeSet.clear();
        if (null != knowledge.getId()) {
            List<Edgeamongknowledge> edgeamongknowledges = applicationEdgeamongknowledgeController.getEdgeamongknowledges(knowledge, StaticFields.PREDCESSOR);
            edgeamongknowledges.forEach((Edgeamongknowledge edge) -> {
                //拿到了一条记录，在该记录中，knowledge作后继，因此，可以找到它的前驱
                predcessorKnowledgeSet.add(edge.getPredecessornode());
            });
        }
        return predcessorKnowledgeSet;
    }

    public Set<Knowledge> getCandidateKnowledges(Subject subject, Knowledge removed) {
        if (null == candidateKnowledge || candidateKnowledge.isEmpty()) {
            candidateKnowledge = new LinkedHashSet<>();
            candidateKnowledge.addAll(getKnowledgeList4Subject(subject));
            //candidateKnowledge.removeAll(getPredcessorKnowledges());
            candidateKnowledge.remove(removed);
        }
        return candidateKnowledge;
    }

    public List<Map.Entry<Knowledge, Integer>> sortbyCountEntrys(Map<Knowledge, Integer> map) {
        if (map.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map.Entry<Knowledge, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Knowledge, Integer>>() {
//降序排序
            @Override
            public int compare(Map.Entry<Knowledge, Integer> o1,
                    Map.Entry<Knowledge, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return list;
    }

    private void removeStyle(Knowledge knowledge) {
        if (knowledge.getDetails().contains("<td")) {
            knowledge.setDetails(StaticFields.replaceCommonStyle(knowledge.getDetails()));
        }
    }

    public void create(Knowledge knowledge) {
        removeStyle(knowledge);
        try {//修改的也从这儿走，所以try一下
            getFacade().create(knowledge);
        } catch (Exception e) {
            System.out.println("修改的从这儿走的");
        }

    }

    public void remove(Knowledge knowledge) {
        getFacade().remove(knowledge);
    }

    public void edit(Knowledge knowledge) {
        removeStyle(knowledge);
        getFacade().edit(knowledge);
    }

    public List<Knowledge> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }

    public List<Knowledge> getKnowledgeList4Leadpoint(Leadpoint leadpoint) {
        return getQueryResultList("select * from knowledge where id in (" + leadpoint.getKnowledgeId() + ") order by id");
    }

    //=====================================================
    //------------------------Converter-----------------------
    @FacesConverter(forClass = Knowledge.class)
    public static class KnowledgeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            KnowledgeController controller = (KnowledgeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "knowledgeControllerA");
            return controller.getKnowledge(Integer.valueOf(value));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Knowledge) {
                Knowledge o = (Knowledge) object;
                return String.valueOf(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Knowledge.class.getName());
            }
        }

    }
    String knowledgeIdsString4Subject = "";

    public String getKnowledgeRelationship4dagre(Knowledge paraKnowledge) {
        if (null != paraKnowledge && null != paraKnowledge.getSubjectId()) {
            String resultString = "digraph {"
                    + "node [rx=5 ry=5 labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"] "
                    + "edge [labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"]\n";
            List<Edgeamongknowledge> tem = applicationEdgeamongknowledgeController.getEdgeamongknowledgeList(paraKnowledge.getSubjectId());
            for (Edgeamongknowledge edge : tem) {
                if (edge.getPredecessornode().equals(paraKnowledge)) {// 对当前的知识点进行字体处理
                    String fontSize = "font-size:32px;";
                    resultString += getShapeString(edge.getPredecessornode(), fontSize);
                } else {
                    resultString += getShapeString(edge.getPredecessornode(), "");
                }
                if (edge.getSuccessornode().equals(paraKnowledge)) {// 对当前的知识点进行字体处理
                    String fontSize = "font-size:32px;";
                    resultString += getShapeString(edge.getSuccessornode(), fontSize);
                } else {
                    resultString += getShapeString(edge.getSuccessornode(), "");
                }
                resultString += applicationEdgeamongknowledgeController.getString4Dagre(edge);
            }
            return resultString + "}";
        } else {
            return "digraph {}";
        }
    }

    public String getString4Dagre(Knowledge knowledge) {
        return knowledge.getName().replace("\"", "") + knowledge.getId();
    }

    private String getShapeString(Knowledge knowledge, String fontsize) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(getString4Dagre(knowledge))
                .append("\"").append(" ");
        switch (knowledge.getLevelnumber()) {
            case 1:
                sb.append("[labelType=\"html\"  label=\"<span style='color:#000080;").append(fontsize).append("'>")
                        .append(getString4Dagre(knowledge))
                        .append("</span>\" shape=\"rect\"];\n");
                break;
            case 2:
                sb.append("[labelType=\"html\"  label=\"<span style='color:#000080;").append(fontsize).append("'>")
                        .append(getString4Dagre(knowledge))
                        .append("</span>\"];\n");
                break;
            case 3:
                sb.append("[labelType=\"html\"  label=\"<span style='color:#800000;").append(fontsize).append("'>")
                        .append(getString4Dagre(knowledge))
                        .append("</span>\" shape=\"circle\"];\n");
                break;
            case 4:
                sb.append("[labelType=\"html\"  label=\"<span style='color:#a30000;").append(fontsize).append("'>")
                        .append(getString4Dagre(knowledge))
                        .append("</span>\" shape=\"ellipse\"];\n");
                break;
            default:
                sb.append("[labelType=\"html\"  label=\"<span style='color:#5d1818;'").append(fontsize).append("'>")
                        .append(getString4Dagre(knowledge))
                        .append("</span>\" shape=\"circle\"];\n");
        }

        return sb.toString();
    }
//For special students, maybe one student or more than one student
    //颜色由浅到深的10种颜色
     private final String[] colorStrArr = new String[]{"#FFE1FF", "#FFD39B", "#FFBBFF", "#FFAEB9", "#FF8C69", "#FF8247", "#FF7256", "#FF6347", "#FF34B3", "#FF0000"};
     private final Integer[] fontSizeArr = new Integer[]{32, 38, 44, 50, 56, 62, 68, 74, 80, 80};

    public Set<Knowledge> getKnowledges4Question(Set<Question> questionSet) {
        Set<Knowledge> result = new HashSet<>();
        for (Question question : questionSet) {
            result.add(question.getKnowledgeId());
        }
        return result;
    }

    public Set<Knowledge> getSuccessorKnowledges(Knowledge knowledge) {
        Set<Knowledge> kc = new HashSet<>();
        List<Edgeamongknowledge> edgeSet = applicationEdgeamongknowledgeController.getEdgeamongknowledges(knowledge, StaticFields.PREDCESSOR);
        for (Edgeamongknowledge edgeamongknowledge : edgeSet) {
            kc.add(edgeamongknowledge.getSuccessornode());
        }
        return kc;
    }

    public String getKnowledgeString(Set<Knowledge> knowledges) {
        String result = "";
        for (Knowledge knowledge : knowledges) {
            result += "," + knowledge.getId();
        }
        return result.length() > 0 ? result.substring(1) : "";
    }

    public String getTemplateOfKonwledge() {
        return "Excel:" + commonSession.getResourceBound().getString("Name") + "\t" + commonSession.getResourceBound().getString("Detail");
    }

    public int count() {
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        //setDataChanged(true);
    }

    public List<Knowledge> findRange(int[] range) {
        //setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Knowledge find(Integer id) {
        //setDataChanged(false);
        return getFacade().find(id);
    }

    public boolean isTeacherEditable(TeacherAdmin teacherAdmin) {
        boolean editable = false;
        if (null != teacherAdmin) {
            editable = true;
        }
        return editable;
    }

    public List<Knowledge> getSimilarKnowledges(String text, int type) {
        List<Knowledge> result = new LinkedList<>();
        List<Knowledge> allKnowledges = getFacade().findAll();
        allKnowledges.forEach((Knowledge knwo) -> {
            double simlarity = 0;
            String comparedText = "";
            switch (type) {
                case 1://知识点名称
                    comparedText = knwo.getName();
                    break;
                case 2://知识点详情
                    comparedText = knwo.getDetails();
                    break;
            }
//            simlarity = applicationAppparaminfoController.getSimilarValue(text, comparedText);
//            if (simlarity > StaticFields.SIMPLEANSWERCORECTTHRESHOLD) {
//                result.add(knwo);
//            }
        });
        return result;
    }

    /**
     * @return the colorStrArr
     */
    public String[] getColorStrArr() {
        return colorStrArr;
    }

    /**
     * @return the fontSizeArr
     */
    public Integer[] getFontSizeArr() {
        return fontSizeArr;
    }

}
