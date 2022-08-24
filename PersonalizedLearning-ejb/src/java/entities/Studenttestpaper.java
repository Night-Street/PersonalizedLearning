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
@Table(name = "STUDENTTESTPAPER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Studenttestpaper.findAll", query = "SELECT s FROM Studenttestpaper s")
    , @NamedQuery(name = "Studenttestpaper.findById", query = "SELECT s FROM Studenttestpaper s WHERE s.id = :id")
    , @NamedQuery(name = "Studenttestpaper.findByQuestionIds", query = "SELECT s FROM Studenttestpaper s WHERE s.questionIds = :questionIds")
    , @NamedQuery(name = "Studenttestpaper.findByTestscore", query = "SELECT s FROM Studenttestpaper s WHERE s.testscore = :testscore")
    , @NamedQuery(name = "Studenttestpaper.findByStudentAnswer", query = "SELECT s FROM Studenttestpaper s WHERE s.studentAnswer = :studentAnswer")
    , @NamedQuery(name = "Studenttestpaper.findByAnsweredInterval", query = "SELECT s FROM Studenttestpaper s WHERE s.answeredInterval = :answeredInterval")
    , @NamedQuery(name = "Studenttestpaper.findByFinished", query = "SELECT s FROM Studenttestpaper s WHERE s.finished = :finished")
    , @NamedQuery(name = "Studenttestpaper.findByFirstWrongquestionids", query = "SELECT s FROM Studenttestpaper s WHERE s.firstWrongquestionids = :firstWrongquestionids")
    , @NamedQuery(name = "Studenttestpaper.findByOtherwrongids", query = "SELECT s FROM Studenttestpaper s WHERE s.otherwrongids = :otherwrongids")
    , @NamedQuery(name = "Studenttestpaper.findByleadpointId", query = "SELECT s FROM Studenttestpaper s WHERE s.leadpointId = :leadpointId")
    , @NamedQuery(name = "Studenttestpaper.findBystudentId", query = "SELECT s FROM Studenttestpaper s WHERE s.studentId = :studentId")
    , @NamedQuery(name = "Studenttestpaper.findBytestpaperId", query = "SELECT s FROM Studenttestpaper s WHERE s.testpaperId = :testpaperId")
})
public class Studenttestpaper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUESTION_IDS", length = 5120)
    private String questionIds;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TESTSCORE")
    private Double testscore;
    @Column(name = "STUDENT_ANSWER", length = 1000)
    private String studentAnswer;
    @Column(name = "ANSWERED_INTERVAL")
    private Integer answeredInterval;
    @Column(name = "FINISHED")
    private Boolean finished;
    @Column(name = "FIRST_WRONGQUESTIONIDS", length = 5120)
    private String firstWrongquestionids;
    @Column(name = "OTHERWRONGIDS", length = 5120)
    private String otherwrongids;
    @JoinColumn(name = "LEADPOINT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Leadpoint leadpointId;
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "TESTPAPERID", referencedColumnName = "ID")
    @ManyToOne
    private Testpaper testpaperId;

    public Studenttestpaper() {
    }

    public Studenttestpaper(Integer id) {
        this.id = id;
    }

    public int getQuestionidsLeng() {
        return 5120;
    }

    public int getStudentAnswerLength() {
        return 1000;
    }

    public int getFirstWrongquestionidsLength() {
        return 5120;
    }

    public int getOotherwrongidsLength() {
        return 5120;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(String questionIds) {
        this.questionIds = questionIds;
    }

    public Double getTestscore() {
        return testscore;
    }

    public void setTestscore(Double testscore) {
        this.testscore = testscore;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public Integer getAnsweredInterval() {
        return answeredInterval;
    }

    public void setAnsweredInterval(Integer answeredInterval) {
        this.answeredInterval = answeredInterval;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public String getFirstWrongquestionids() {
        return firstWrongquestionids;
    }

    public void setFirstWrongquestionids(String firstWrongquestionids) {
        this.firstWrongquestionids = firstWrongquestionids;
    }

    public String getOtherwrongids() {
        return otherwrongids;
    }

    public void setOtherwrongids(String otherwrongids) {
        this.otherwrongids = otherwrongids;
    }

    public Leadpoint getLeadpointId() {
        return leadpointId;
    }

    public void setLeadpointId(Leadpoint leadpointId) {
        this.leadpointId = leadpointId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Testpaper getTestpaperId() {
        return testpaperId;
    }

    public void setTestpaperId(Testpaper testpaperId) {
        this.testpaperId = testpaperId;
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
        if (!(object instanceof Studenttestpaper)) {
            return false;
        }
        Studenttestpaper other = (Studenttestpaper) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getTestpaperId().getCreateteacherid().getSecondname() + this.getTestpaperId().getCreateteacherid().getFirstname();
    }

}
