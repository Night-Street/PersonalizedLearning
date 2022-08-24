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
@Table(name = "TEACHERSCHEDULE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Teacherschedule.findAll", query = "SELECT t FROM Teacherschedule t")
    , @NamedQuery(name = "Teacherschedule.findById", query = "SELECT t FROM Teacherschedule t WHERE t.id = :id")
    , @NamedQuery(name = "Teacherschedule.findByStarttime", query = "SELECT t FROM Teacherschedule t WHERE t.starttime = :starttime")
    , @NamedQuery(name = "Teacherschedule.findByEndtime", query = "SELECT t FROM Teacherschedule t WHERE t.endtime = :endtime")
    , @NamedQuery(name = "Teacherschedule.findByMyresult", query = "SELECT t FROM Teacherschedule t WHERE t.myresult = :myresult")
    , @NamedQuery(name = "Teacherschedule.findByMemo", query = "SELECT t FROM Teacherschedule t WHERE t.memo = :memo")
 , @NamedQuery(name = "Teacherschedule.findByteacher", query = "SELECT t FROM Teacherschedule t WHERE t.userid = :teacher")
, @NamedQuery(name = "Teacherschedule.findByclassid", query = "SELECT t FROM Teacherschedule t WHERE t.belongclassid = :classid")
})
public class Teacherschedule  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "STARTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "ENDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @Column(name = "MYRESULT")
    private Boolean myresult;
    @Column(name = "MEMO", length = 1000)
    private String memo;
    @JoinColumn(name = "BELONGCLASSID", referencedColumnName = "ID")
    @ManyToOne
    private Scheduleclass belongclassid;
    @JoinColumn(name = "USERID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin userid;

    public Teacherschedule() {
    }

    public Teacherschedule(Integer id) {
        this.id = id;
    }

    public int getMemoLength(){
        return 1000;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Scheduleclass getBelongclassid() {
        return belongclassid;
    }

    public void setBelongclassid(Scheduleclass belongclassid) {
        this.belongclassid = belongclassid;
    }

    public TeacherAdmin getUserid() {
        return userid;
    }

    public void setUserid(TeacherAdmin userid) {
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
        if (!(object instanceof Teacherschedule)) {
            return false;
        }
        Teacherschedule other = (Teacherschedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getUserid().getName()+"-"+this.belongclassid.getName();
    }
    
}
