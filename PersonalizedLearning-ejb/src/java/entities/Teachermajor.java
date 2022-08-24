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
@Table(name = "TEACHERMAJOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Teachermajor.findAll", query = "SELECT t FROM Teachermajor t")
    , @NamedQuery(name = "Teachermajor.findById", query = "SELECT t FROM Teachermajor t WHERE t.id = :id")
    , @NamedQuery(name = "Teachermajor.findBymajorid", query = "SELECT t FROM Teachermajor t WHERE t.majorid = :majorid")
    , @NamedQuery(name = "Teachermajor.findByteacherid", query = "SELECT t FROM Teachermajor t WHERE t.teacherid = :teacherid")
})
public class Teachermajor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "MAJORID", referencedColumnName = "ID")
    @ManyToOne
    private Major majorid;
    @JoinColumn(name = "TEACHERID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherid;

    public Teachermajor() {
    }

    public Teachermajor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Major getMajorid() {
        return majorid;
    }

    public void setMajorid(Major majorid) {
        this.majorid = majorid;
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
        if (!(object instanceof Teachermajor)) {
            return false;
        }
        Teachermajor other = (Teachermajor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.teacherid.getName() + ":" + this.majorid.getName();
    }

}
