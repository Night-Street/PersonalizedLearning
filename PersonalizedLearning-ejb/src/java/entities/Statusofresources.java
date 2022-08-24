package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "STATUSOFRESOURCES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Statusofresources.findAll", query = "SELECT s FROM Statusofresources s")
    , @NamedQuery(name = "Statusofresources.findById", query = "SELECT s FROM Statusofresources s WHERE s.id = :id")
    , @NamedQuery(name = "Statusofresources.findByMeaning", query = "SELECT s FROM Statusofresources s WHERE s.meaning = :meaning")})
public class Statusofresources implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 16)
    @Column(name = "MEANING")
    private String meaning;
    @OneToMany(mappedBy = "status2nd")
    private Collection<Reexamination> reexaminationCollection;
    @OneToMany(mappedBy = "status")
    private Set<Reexamination> reexaminationCollection1;

    public Statusofresources() {
    }
    public int getMeaningLength(){
        return 16;
    }
    public boolean isExaminPassed() {
        return id == 1;
    }

    public boolean isWaitingExamin() {
        return id == 0;
    }

    public boolean isExaminFailed() {
        return id == 2;
    }

    public boolean isSaved() {
        return id == 3;
    }
    public Statusofresources(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
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
        if (!(object instanceof Statusofresources)) {
            return false;
        }
        Statusofresources other = (Statusofresources) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "beans.Statusofresources[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Reexamination> getReexaminationCollection() {
        return reexaminationCollection;
    }

    public void setReexaminationCollection(Collection<Reexamination> reexaminationCollection) {
        this.reexaminationCollection = reexaminationCollection;
    }

    @XmlTransient
    public Set<Reexamination> getReexaminationCollection1() {
        return reexaminationCollection1;
    }

    public void setReexaminationCollection1(Set<Reexamination> reexaminationCollection1) {
        this.reexaminationCollection1 = reexaminationCollection1;
    }

}
