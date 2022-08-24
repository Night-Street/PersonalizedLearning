package entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author haogs
 */
@Entity
@Table(name = "QUESTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q")
    , @NamedQuery(name = "Question.findById", query = "SELECT q FROM Question q WHERE q.id = :id")
    , @NamedQuery(name = "Question.findByType", query = "SELECT q FROM Question q WHERE q.type = :type")
    , @NamedQuery(name = "Question.findByScore", query = "SELECT q FROM Question q WHERE q.score = :score")
    , @NamedQuery(name = "Question.findByDegree", query = "SELECT q FROM Question q WHERE q.degree = :degree")
    , @NamedQuery(name = "Question.findByAnswer", query = "SELECT q FROM Question q WHERE q.answer = :answer")
    , @NamedQuery(name = "Question.findByValueinfo", query = "SELECT q FROM Question q WHERE q.valueinfo = :valueinfo")
    , @NamedQuery(name = "Question.findByAnalysis", query = "SELECT q FROM Question q WHERE q.analysis = :analysis")
    , @NamedQuery(name = "Question.findBySecondcontent", query = "SELECT q FROM Question q WHERE q.secondcontent = :secondcontent")
    , @NamedQuery(name = "Question.findByNeedtime", query = "SELECT q FROM Question q WHERE q.needtime = :needtime")
    , @NamedQuery(name = "Question.findByFigure", query = "SELECT q FROM Question q WHERE q.figure = :figure")
    , @NamedQuery(name = "Question.findByknowledgeId", query = "SELECT q FROM Question q WHERE q.knowledgeId = :knowledgeId")
    , @NamedQuery(name = "Question.findByRefer", query = "SELECT q FROM Question q WHERE q.refer = :refer")
})
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TYPE", nullable = false)
    private Integer type;
    @Column(name = "SCORE", nullable = false)
    private Integer score;
    @Column(name = "DEGREE", nullable = false)
    private Integer degree;
    @Column(name = "ANSWER", length = 20480, nullable = false)
    private String answer;
    @Column(name = "VALUEINFO", length = 3000, nullable = false)
    private String valueinfo;
    @Column(name = "ANALYSIS", length = 10000, nullable = false)
    private String analysis;
    @Column(name = "SECONDCONTENT", length = 1000)
    private String secondcontent;
    @Column(name = "NEEDTIME")
    private Integer needtime;
    @Column(name = "FIGURE", length = 100)
    private String figure;
    @Column(name = "PRAISE_CNT")
    private Integer praiseCnt;
    @Column(name = "REFER", length = 1000)
    private String refer;
    @OneToMany(mappedBy = "questionId")
    private Set<WrongquestionCollection> wrongquestionCollectionSet;
    @JoinColumn(name = "KNOWLEDGE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Knowledge knowledgeId;
    @OneToMany(mappedBy = "questionId")
    private Set<Praise> praiseSet;
    @OneToMany(mappedBy = "questionid")
    private Set<Reexamination> reexaminationSet;
     @OneToMany(mappedBy = "question")
    private Set<Questionstudentcosttime> questionstudentcosttimeSet;

    public Question() {
    }

    public Question(Integer id) {
        this.id = id;
    }
    
    public int getAnswerLength(){
        return 20480;
    }

    public int getValueinfoLength(){
        return 3000;
    }
   
    public int getAnalysisLength(){
        return 10000;
    }
    
    public int getSecondcontentLength(){
        return 1000;
    }

    public int getFigureLength(){
        return 100;
    }

    public int getReferLength(){
        return 1000;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getValueinfo() {
        return valueinfo;
    }

    public void setValueinfo(String valueinfo) {
        this.valueinfo = valueinfo;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getSecondcontent() {
        return secondcontent;
    }
    public Integer getPraiseCnt() {
        return praiseCnt;
    }

    public void setPraiseCnt(Integer praiseCnt) {
        this.praiseCnt = praiseCnt;
    }
    public String getSecondContenItems() {
        String result = "";
        if (this.type == 3 || this.type == 6) {
            String[] selectionString = getSecondcontent().split("\\$#");
            for (int i = 1; i <= selectionString.length; i++) {
                result += selectionString[i - 1] + "<br>";
            }
        }
        return result;
    }

    public void setSecondcontent(String secondcontent) {
        this.secondcontent = secondcontent;
    }

    public Integer getNeedtime() {
        return needtime;
    }

    public void setNeedtime(Integer needtime) {
        this.needtime = needtime;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    @XmlTransient
    public Set<WrongquestionCollection> getWrongquestionCollectionSet() {
        return wrongquestionCollectionSet;
    }

    public void setWrongquestionCollectionSet(Set<WrongquestionCollection> wrongquestionCollectionSet) {
        this.wrongquestionCollectionSet = wrongquestionCollectionSet;
    }

    public Knowledge getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Knowledge knowledgeId) {
        this.knowledgeId = knowledgeId;
    }
    
    @XmlTransient
    public Set<Praise> getPraiseSet() {
        return praiseSet;
    }

    public void setPraiseSet(Set<Praise> praiseSet) {
        this.praiseSet = praiseSet;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Question[ id=" + id + " ]";
    }

    @XmlTransient
    public Set<Questionstudentcosttime> getQuestionstudentcosttimeSet() {
        return questionstudentcosttimeSet;
    }

    public void setQuestionstudentcosttimeSet(Set<Questionstudentcosttime> questionstudentcosttimeSet) {
        this.questionstudentcosttimeSet = questionstudentcosttimeSet;
    }

    /**
     * @return the reexaminationSet
     */
    public Set<Reexamination> getReexaminationSet() {
        return reexaminationSet;
    }

    /**
     * @param reexaminationSet the reexaminationSet to set
     */
    public void setReexaminationSet(Set<Reexamination> reexaminationSet) {
        this.reexaminationSet = reexaminationSet;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }
    
}