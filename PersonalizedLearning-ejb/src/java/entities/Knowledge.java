package entities;

import java.io.Serializable;
import java.util.Iterator;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "KNOWLEDGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Knowledge.findAll", query = "SELECT k FROM Knowledge k ORDER BY k.name")
    , @NamedQuery(name = "Knowledge.findById", query = "SELECT k FROM Knowledge k WHERE k.id = :id")
    , @NamedQuery(name = "Knowledge.findByName", query = "SELECT k FROM Knowledge k WHERE k.name = :name")
    , @NamedQuery(name = "Knowledge.findByDetails", query = "SELECT k FROM Knowledge k WHERE k.details = :details")
    , @NamedQuery(name = "Knowledge.findByLevelnumber", query = "SELECT k FROM Knowledge k WHERE k.levelnumber = :levelnumber")
    , @NamedQuery(name = "Knowledge.findByC1", query = "SELECT k FROM Knowledge k WHERE k.c1 = :c1")
    , @NamedQuery(name = "Knowledge.findByC2", query = "SELECT k FROM Knowledge k WHERE k.c2 = :c2")
    , @NamedQuery(name = "Knowledge.findByC3", query = "SELECT k FROM Knowledge k WHERE k.c3 = :c3")
    , @NamedQuery(name = "Knowledge.findBySubjectid", query = "SELECT k FROM Knowledge k WHERE k.subjectId = :subjectid")
})
public class Knowledge implements Serializable {

    @Size(max = 3000)
    @Column(name = "C5")
    private String c5;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", length = 32)
    private String name;
    @Column(name = "DETAILS", length = 10240)
    private String details;
    @Column(name = "C1", length = 3000)
    private String c1;
    @Column(name = "C2", length = 3000)
    private String c2;
    @Column(name = "C3", length = 500)
    private String c3;
    @Column(name = "LEVELNUMBER")
    private Integer levelnumber;
    @Column(name = "PRAISE_CNT")
    private Integer praiseCnt;
    @OneToMany(mappedBy = "knowledgeId")
    private Set<Question> questionSet;
    @OneToMany(mappedBy = "knowledgeId")
    private Set<Learningresource> learningresourceSet;
    @OneToMany(mappedBy = "knowledgeId")
    private Set<Praise> praiseSet;
    @OneToMany(mappedBy = "predecessornode")
    private Set<Edgeamongknowledge> edgeamongknowledgeSet;
    @OneToMany(mappedBy = "successornode")
    private Set<Edgeamongknowledge> edgeamongknowledgeSet1;
    @JoinColumn(name = "C4", referencedColumnName = "ID")
    @ManyToOne
    private Partofspeech c4;
    @OneToMany(mappedBy = "knowledgeid")
    private Set<Reexamination> reexaminationSet;
    @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Subject subjectId;

    public Knowledge() {
    }

    public Knowledge(Integer id) {
        this.id = id;
    }

    public int getDetailsLength() {
        return 10240;
    }

    public int getC1_C2Length() {
        return 3000;
    }

    public int getNameLength() {
        return 32;
    }

    public int getC3Length() {
        return 500;
    }

    public Knowledge(Integer id, String name, String details,
            String c1, String c2, String c3, Integer levelnumber, Integer praiseCnt,
            Subject subjectId) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.levelnumber = levelnumber;
        this.praiseCnt = praiseCnt;
        this.subjectId = subjectId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;

    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getLevelnumber() {
        return levelnumber;
    }

    public void setLevelnumber(Integer levelnumber) {
        this.levelnumber = levelnumber;
    }

    @XmlTransient
    public Set<Question> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Set<Question> questionSet) {
        this.questionSet = questionSet;
    }

    @XmlTransient
    public Set<Learningresource> getLearningresourceSet() {
        return learningresourceSet;
    }

    public void setLearningresourceSet(Set<Learningresource> learningresourceSet) {
        this.learningresourceSet = learningresourceSet;
    }

    @XmlTransient
    public Set<Edgeamongknowledge> getSuccessorknowledgeSet() {
        return edgeamongknowledgeSet;
    }

    public void setSuccessorknowledgeSet(Set<Edgeamongknowledge> edgeamongknowledgeSet) {
        this.edgeamongknowledgeSet = edgeamongknowledgeSet;
    }

    @XmlTransient
    public Set<Edgeamongknowledge> getPredcessorKnowledgeSet() {
        return edgeamongknowledgeSet1;
    }

    public void setPredcessorKnowledgeSet(Set<Edgeamongknowledge> edgeamongknowledgeSet1) {
        this.edgeamongknowledgeSet1 = edgeamongknowledgeSet1;
    }

    public Subject getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subject subjectId) {
        this.subjectId = subjectId;
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
        if (!(object instanceof Knowledge)) {
            return false;
        }
        Knowledge other = (Knowledge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public Partofspeech getC4() {
        return c4;
    }

    public void setC4(Partofspeech c4) {
        this.c4 = c4;
    }

    public Set<Praise> getPraiseSet() {
        return praiseSet;
    }

    public void setPraiseSet(Set<Praise> praiseSet) {
        this.praiseSet = praiseSet;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public Integer getPraiseCnt() {
        return praiseCnt;
    }

    public void setPraiseCnt(Integer praiseCnt) {
        this.praiseCnt = praiseCnt;
    }

    public String getC5() {
        return c5;
    }

    public void setC5(String c5) {
        this.c5 = c5;
    }

    public Set<Reexamination> getReexaminationSet() {
        return reexaminationSet;
    }

    public void setReexaminationSet(Set<Reexamination> reexaminationSet) {
        this.reexaminationSet = reexaminationSet;
    }

    public String getPreSuccNameString() {
        StringBuilder sb = new StringBuilder();
        Set<Edgeamongknowledge> preEdgeamongknowledges = this.getPredcessorKnowledgeSet();
        Iterator it = preEdgeamongknowledges.iterator();
        while (it.hasNext()) {
            Edgeamongknowledge edgeamongknowledge = (Edgeamongknowledge) it.next();
            sb.append(edgeamongknowledge.getPredecessornode().name).append(",");
        }
        sb.append("\n").append(name).append("\n");
        Set<Edgeamongknowledge> succEdgeamongknowledges = this.getSuccessorknowledgeSet();
        it = succEdgeamongknowledges.iterator();
        while (it.hasNext()) {
            Edgeamongknowledge edgeamongknowledge = (Edgeamongknowledge) it.next();
            sb.append(edgeamongknowledge.getSuccessornode().name).append(",");
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
