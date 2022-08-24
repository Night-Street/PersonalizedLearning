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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "REALIZATIONOFMYDREAM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Realizationofmydream.findAll", query = "SELECT r FROM Realizationofmydream r")
    , @NamedQuery(name = "Realizationofmydream.findById", query = "SELECT r FROM Realizationofmydream r WHERE r.id = :id")
    , @NamedQuery(name = "Realizationofmydream.findByRecorderdate", query = "SELECT r FROM Realizationofmydream r WHERE r.recorderdate = :recorderdate")
    , @NamedQuery(name = "Realizationofmydream.findByMilestone", query = "SELECT r FROM Realizationofmydream r WHERE r.milestone = :milestone")})
public class Realizationofmydream implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "RECORDERDATE")
    @Temporal(TemporalType.DATE)
    private Date recorderdate;
    @Size(max = 1000)
    @Column(name = "MILESTONE")
    private String milestone;
    @JoinColumn(name = "DREAMID", referencedColumnName = "ID")
    @ManyToOne
    private Studentdream dreamid;

    public Realizationofmydream() {
    }

    public Realizationofmydream(Integer id) {
        this.id = id;
    }

    public int getMilestoneLength(){
        return 1000;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRecorderdate() {
        return recorderdate;
    }

    public void setRecorderdate(Date recorderdate) {
        this.recorderdate = recorderdate;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public Studentdream getDreamid() {
        return dreamid;
    }

    public void setDreamid(Studentdream dreamid) {
        this.dreamid = dreamid;
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
        if (!(object instanceof Realizationofmydream)) {
            return false;
        }
        Realizationofmydream other = (Realizationofmydream) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eintity.Realizationofmydream[ id=" + id + " ]";
    }
    
}
