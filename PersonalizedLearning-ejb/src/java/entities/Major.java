package entities;

import java.io.Serializable;
import java.util.Set;
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
@Table(name = "MAJOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Major.findAll", query = "SELECT m FROM Major m ORDER BY m.name")
    , @NamedQuery(name = "Major.findById", query = "SELECT m FROM Major m WHERE m.id = :id")
    , @NamedQuery(name = "Major.findByName", query = "SELECT m FROM Major m WHERE m.name = :name")})
public class Major   implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "NAME")
    private String name;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @OneToMany(mappedBy = "majorid")
    private Set<School> schoolSet;
    @OneToMany(mappedBy = "parent")
    private Set<Major> majorSet;
    @JoinColumn(name = "PARENT", referencedColumnName = "ID")
    @ManyToOne
    private Major parent;
    @OneToMany(mappedBy = "majorid")
    private Set<Majorsubject> majorsubjectSet;
    @OneToMany(mappedBy = "majorid")
    private Set<Teachermajor> teachermajorSet;

    public Major() {
    }

    public Major(Integer id) {
        this.id = id;
    }

    public Major(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getNameLength(){
        return 128;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @XmlTransient
    public Set<School> getSchoolSet() {
        return schoolSet;
    }

    public void setSchoolSet(Set<School> schoolSet) {
        this.schoolSet = schoolSet;
    }

    @XmlTransient
    public Set<Major> getMajorSet() {
        return majorSet;
    }

    public void setMajorSet(Set<Major> majorSet) {
        this.majorSet = majorSet;
    }

    public Major getParent() {
        return parent;
    }

    public void setParent(Major parent) {
        this.parent = parent;
    }

    @XmlTransient
    public Set<Majorsubject> getMajorsubjectSet() {
        return majorsubjectSet;
    }

    public void setMajorsubjectSet(Set<Majorsubject> majorsubjectSet) {
        this.majorsubjectSet = majorsubjectSet;
    }

    @XmlTransient
    public Set<Teachermajor> getTeachermajorSet() {
        return teachermajorSet;
    }

    public void setTeachermajorSet(Set<Teachermajor> teachermajorSet) {
        this.teachermajorSet = teachermajorSet;
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
        if (!(object instanceof Major)) {
            return false;
        }
        Major other = (Major) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
