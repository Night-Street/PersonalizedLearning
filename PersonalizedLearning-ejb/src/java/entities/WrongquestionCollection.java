package entities;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "WRONGQUESTION_COLLECTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WrongquestionCollection.findAll", query = "SELECT w FROM WrongquestionCollection w")
    , @NamedQuery(name = "WrongquestionCollection.findById", query = "SELECT w FROM WrongquestionCollection w WHERE w.id = :id")
    , @NamedQuery(name = "WrongquestionCollection.findBystudentId", query = "SELECT w FROM WrongquestionCollection w WHERE w.studentId = :studentId")
})
public class WrongquestionCollection implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID")
    @ManyToOne
    private Question questionId;
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "REASON_ID", referencedColumnName = "ID")
    @ManyToOne
    private WrongReason reasonId;

    public WrongquestionCollection() {
    }

    public WrongquestionCollection(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public WrongReason getReasonId() {
        return reasonId;
    }

    public void setReasonId(WrongReason reasonId) {
        this.reasonId = reasonId;
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
        if (!(object instanceof WrongquestionCollection)) {
            return false;
        }
        WrongquestionCollection other = (WrongquestionCollection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.WrongquestionCollection[ id=" + id + " ]";
    }

}
