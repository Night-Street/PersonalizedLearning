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
 * @author hgs
 */
@Entity
@Table(name = "TEACSCHOOLSUBJECT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Teacschoolsubject.findAll", query = "SELECT t FROM Teacschoolsubject t")
    , @NamedQuery(name = "Teacschoolsubject.findById", query = "SELECT t FROM Teacschoolsubject t WHERE t.id = :id")
    , @NamedQuery(name = "Teacschoolsubject.findByFromtime", query = "SELECT t FROM Teacschoolsubject t WHERE t.fromtime = :fromtime")
    , @NamedQuery(name = "Teacschoolsubject.findByTotime", query = "SELECT t FROM Teacschoolsubject t WHERE t.totime = :totime")
    , @NamedQuery(name = "Teacschoolsubject.findByschoolid", query = "SELECT t FROM Teacschoolsubject t WHERE t.schoolid = :schoolid")
    , @NamedQuery(name = "Teacschoolsubject.findBysubjectid", query = "SELECT t FROM Teacschoolsubject t WHERE t.subjectid = :subjectid")
    , @NamedQuery(name = "Teacschoolsubject.findByteacherid", query = "SELECT t FROM Teacschoolsubject t WHERE t.teacherid = :teacherid")
})
public class Teacschoolsubject  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FROMTIME")
    @Temporal(TemporalType.DATE)
    private Date fromtime;
    @Column(name = "TOTIME")
    @Temporal(TemporalType.DATE)
    private Date totime;
    @JoinColumn(name = "SCHOOLID", referencedColumnName = "ID")
    @ManyToOne
    private School schoolid;
    @JoinColumn(name = "SUBJECTID", referencedColumnName = "ID")
    @ManyToOne
    private Subject subjectid;
    @JoinColumn(name = "TEACHERID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherid;
    
    public Teacschoolsubject() {
    }

    public Teacschoolsubject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFromtime() {
        return fromtime;
    }

    public void setFromtime(Date fromtime) {
        this.fromtime = fromtime;
    }

    public Date getTotime() {
        return totime;
    }

    public void setTotime(Date totime) {
        this.totime = totime;
    }

    public School getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(School schoolid) {
        this.schoolid = schoolid;
    }

    public Subject getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Subject subjectid) {
        this.subjectid = subjectid;
    }

    public TeacherAdmin getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(TeacherAdmin teacherid) {
        this.teacherid = teacherid;
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
        if (!(object instanceof Teacschoolsubject)) {
            return false;
        }
        Teacschoolsubject other = (Teacschoolsubject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.teacherid.getName()+":"+this.getSchoolid().getName()+":"+this.getSubjectid().getName();
    }
    
}
