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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author haogs
 */
@Entity
@Table(name = "STUDENTACCUPOINTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Studentaccupoints.findAll", query = "SELECT s FROM Studentaccupoints s")
    , @NamedQuery(name = "Studentaccupoints.findById", query = "SELECT s FROM Studentaccupoints s WHERE s.id = :id")
    , @NamedQuery(name = "Studentaccupoints.findByPointsNum", query = "SELECT s FROM Studentaccupoints s WHERE s.pointsNum = :pointsNum")
    , @NamedQuery(name = "Studentaccupoints.findByModifyDate", query = "SELECT s FROM Studentaccupoints s WHERE s.modifyDate = :modifyDate")
    , @NamedQuery(name = "Studentaccupoints.findByReason", query = "SELECT s FROM Studentaccupoints s WHERE s.reason = :reason")
    , @NamedQuery(name = "Studentaccupoints.findBystudentId", query = "SELECT s FROM Studentaccupoints s WHERE s.studentId = :studentId")
    , @NamedQuery(name = "Studentaccupoints.findByteacherId", query = "SELECT s FROM Studentaccupoints s WHERE s.teacherId = :teacherId")
})
public class Studentaccupoints  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "POINTS_NUM")
    private double pointsNum;
    @Column(name = "MODIFY_DATE")
    @Temporal(TemporalType.DATE)
    private Date modifyDate;
    @Column(name = "REASON", length = 50)
    private String reason;
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "TEACHER_ID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherId;

    public Studentaccupoints() {
    }

    public Studentaccupoints(Integer id) {
        this.id = id;
    }
    
    public int getReasonLength(){
        return 50;
    }
    
    public Studentaccupoints(Integer id, double pointsNum) {
        this.id = id;
        this.pointsNum = pointsNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getPointsNum() {
        return pointsNum;
    }

    public void setPointsNum(double pointsNum) {
        this.pointsNum = pointsNum;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public TeacherAdmin getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(TeacherAdmin teacherId) {
        this.teacherId = teacherId;
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
        if (!(object instanceof Studentaccupoints)) {
            return false;
        }
        Studentaccupoints other = (Studentaccupoints) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Studentaccupoints[ id=" + id + " ]";
    }    
}