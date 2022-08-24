package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author haogs
 */
@Entity
@Table(name = "QUESTIONSTUDENTCOSTTIME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Questionstudentcosttime.findAll", query = "SELECT q FROM Questionstudentcosttime q")
    , @NamedQuery(name = "Questionstudentcosttime.findByQuestionid", query = "SELECT q FROM Questionstudentcosttime q WHERE q.questionstudentcosttimePK.questionid = :questionid")
    , @NamedQuery(name = "Questionstudentcosttime.findByStudentid", query = "SELECT q FROM Questionstudentcosttime q WHERE q.questionstudentcosttimePK.studentid = :studentid")
    , @NamedQuery(name = "Questionstudentcosttime.findByCosttime", query = "SELECT q FROM Questionstudentcosttime q WHERE q.costtime = :costtime")})
public class Questionstudentcosttime implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected QuestionstudentcosttimePK questionstudentcosttimePK;
    @Column(name = "COSTTIME")
    private Integer costtime;
    @JoinColumn(name = "QUESTIONID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Question question;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student;
    @Column(name = "COUNT")
    private Integer count;

    public Questionstudentcosttime() {
    }

    public Questionstudentcosttime(QuestionstudentcosttimePK questionstudentcosttimePK) {
        this.questionstudentcosttimePK = questionstudentcosttimePK;
    }

    public Questionstudentcosttime(int questionid, int studentid) {
        this.questionstudentcosttimePK = new QuestionstudentcosttimePK(questionid, studentid);
    }

    public QuestionstudentcosttimePK getQuestionstudentcosttimePK() {
        return questionstudentcosttimePK;
    }

    public void setQuestionstudentcosttimePK(QuestionstudentcosttimePK questionstudentcosttimePK) {
        this.questionstudentcosttimePK = questionstudentcosttimePK;
    }

    public Integer getCosttime() {
        return costtime;
    }

    public void setCosttime(Integer costtime) {
        this.costtime = costtime;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (questionstudentcosttimePK != null ? questionstudentcosttimePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Questionstudentcosttime)) {
            return false;
        }
        Questionstudentcosttime other = (Questionstudentcosttime) object;
        if ((this.questionstudentcosttimePK == null && other.questionstudentcosttimePK != null) || (this.questionstudentcosttimePK != null && !this.questionstudentcosttimePK.equals(other.questionstudentcosttimePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Questionstudentcosttime[ questionstudentcosttimePK=" + questionstudentcosttimePK + " ]";
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
}
