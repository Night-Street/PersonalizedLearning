package entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "SCHEDULECLASS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scheduleclass.findAll", query = "SELECT s FROM Scheduleclass s")
    , @NamedQuery(name = "Scheduleclass.findById", query = "SELECT s FROM Scheduleclass s WHERE s.id = :id")
    , @NamedQuery(name = "Scheduleclass.findByName", query = "SELECT s FROM Scheduleclass s WHERE s.name = :name")
    , @NamedQuery(name = "Scheduleclass.findByDonothingbeforefinish", query = "SELECT s FROM Scheduleclass s WHERE s.donothingbeforefinish = :donothingbeforefinish")})
public class Scheduleclass  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", length =20)
    private String name;
    @Column(name = "DONOTHINGBEFOREFINISH")
    private Boolean donothingbeforefinish;
    @OneToMany(mappedBy = "belongclassid")
    private Set<Teacherschedule> teacherscheduleSet;
    @OneToMany(mappedBy = "belongclassid")
    private Set<Studentschedule> studentscheduleSet;

    public Scheduleclass() {
    }

    public Scheduleclass(Integer id) {
        this.id = id;
    }
    public int getNameLength(){
        return 20;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDonothingbeforefinish() {
        return donothingbeforefinish;
    }

    public void setDonothingbeforefinish(Boolean donothingbeforefinish) {
        this.donothingbeforefinish = donothingbeforefinish;
    }

    @XmlTransient
    public Set<Teacherschedule> getTeacherscheduleSet() {
        return teacherscheduleSet;
    }

    public void setTeacherscheduleSet(Set<Teacherschedule> teacherscheduleSet) {
        this.teacherscheduleSet = teacherscheduleSet;
    }

    @XmlTransient
    public Set<Studentschedule> getStudentscheduleSet() {
        return studentscheduleSet;
    }

    public void setStudentscheduleSet(Set<Studentschedule> studentscheduleSet) {
        this.studentscheduleSet = studentscheduleSet;
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
        if (!(object instanceof Scheduleclass)) {
            return false;
        }
        Scheduleclass other = (Scheduleclass) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
}
