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
@Table(name = "STUDENTSCHEDULE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Studentschedule.findAll", query = "SELECT s FROM Studentschedule s")
    , @NamedQuery(name = "Studentschedule.findById", query = "SELECT s FROM Studentschedule s WHERE s.id = :id")
    , @NamedQuery(name = "Studentschedule.findByMyresult", query = "SELECT s FROM Studentschedule s WHERE s.myresult = :myresult")
    , @NamedQuery(name = "Studentschedule.findByMemo", query = "SELECT s FROM Studentschedule s WHERE s.memo = :memo")
    , @NamedQuery(name = "Studentschedule.findByStarttime", query = "SELECT s FROM Studentschedule s WHERE s.starttime = :starttime")
    , @NamedQuery(name = "Studentschedule.findByEndtime", query = "SELECT s FROM Studentschedule s WHERE s.endtime = :endtime")
    , @NamedQuery(name = "Studentschedule.findBystudent", query = "SELECT s FROM Studentschedule s WHERE s.userid = :student")
})
public class Studentschedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MYRESULT")
    private Boolean myresult;
    @Column(name = "MEMO", length = 1000)
    private String memo;
    @Column(name = "STARTTIME")
    @Temporal(TemporalType.DATE)
    private Date starttime;
    @Column(name = "ENDTIME")
    @Temporal(TemporalType.DATE)
    private Date endtime;
    @JoinColumn(name = "BELONGCLASSID", referencedColumnName = "ID")
    @ManyToOne
    private Scheduleclass belongclassid;
    @JoinColumn(name = "USERID", referencedColumnName = "ID")
    @ManyToOne
    private Student userid;

    public Studentschedule() {
    }

    public Studentschedule(Integer id) {
        this.id = id;
    }

    public int getMemoLength() {
        return 1000;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getMyresult() {
        return myresult;
    }

    public void setMyresult(Boolean myresult) {
        this.myresult = myresult;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Scheduleclass getBelongclassid() {
        return belongclassid;
    }

    public void setBelongclassid(Scheduleclass belongclassid) {
        this.belongclassid = belongclassid;
    }

    public Student getUserid() {
        return userid;
    }

    public void setUserid(Student userid) {
        this.userid = userid;
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
        if (!(object instanceof Studentschedule)) {
            return false;
        }
        Studentschedule other = (Studentschedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.userid.getName() + "-" + this.getBelongclassid().getName();
    }

}
