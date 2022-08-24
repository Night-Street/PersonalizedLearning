package entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author haogs
 */
@Entity
@Table(name = "PRAISE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Praise.findAll", query = "SELECT p FROM Praise p")
    , @NamedQuery(name = "Praise.findById", query = "SELECT p FROM Praise p WHERE p.id = :id")
    , @NamedQuery(name = "Praise.findByPraiseDate", query = "SELECT p FROM Praise p WHERE p.praiseDate = :praiseDate")
    , @NamedQuery(name = "Praise.findBylearningresourceId", query = "SELECT p FROM Praise p WHERE p.learningresourceId = :learningresourceId")
    , @NamedQuery(name = "Praise.findByquestionId", query = "SELECT p FROM Praise p WHERE p.questionId = :questionId")
    , @NamedQuery(name = "Praise.findBystudentId", query = "SELECT p FROM Praise p WHERE p.studentId = :studentId")
    , @NamedQuery(name = "Praise.findByKnowledgeId", query = "SELECT p FROM Praise p WHERE p.knowledgeId = :knowledgeId")
})
public class Praise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PRAISE_DATE")
    @Temporal(TemporalType.DATE)
    private Date praiseDate;
    @JoinColumn(name = "KNOWLEDGE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Knowledge knowledgeId;
    @JoinColumn(name = "LEARNINGRESOURCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Learningresource learningresourceId;
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID")
    @ManyToOne
    private Question questionId;
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Student studentId;

    public Praise() {
    }

    public Praise(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPraiseDate() {
        return praiseDate;
    }

    public void setPraiseDate(Date praiseDate) {
        this.praiseDate = praiseDate;
    }

    public Knowledge getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Knowledge knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Learningresource getLearningresourceId() {
        return learningresourceId;
    }

    public void setLearningresourceId(Learningresource learningresourceId) {
        this.learningresourceId = learningresourceId;
    }

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof Praise)) {
            return false;
        }
        Praise other = (Praise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Praise[ id=" + id + " ]";
    }

}
