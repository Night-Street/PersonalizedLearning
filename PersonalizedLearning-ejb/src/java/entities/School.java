package entities;

import java.io.Serializable;
import java.util.Objects;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "SCHOOL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "School.findAll", query = "SELECT s FROM School s ORDER BY s.name")
    , @NamedQuery(name = "School.findById", query = "SELECT s FROM School s WHERE s.id = :id")
    , @NamedQuery(name = "School.findByName", query = "SELECT s FROM School s WHERE s.name = :name")
    , @NamedQuery(name = "School.findBymajorid", query = "SELECT s FROM School s WHERE s.majorid = :majorid")
    , @NamedQuery(name = "School.findByschoolId", query = "SELECT s FROM School s WHERE s.parentid = :schoolId")
})
public class School implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", length = 64)
    private String name;
    @JoinColumn(name = "EDULEVEL", referencedColumnName = "ID")
    @ManyToOne
    private Edulevel edulevel;
    @JoinColumn(name = "MAJORID", referencedColumnName = "ID")
    @ManyToOne
    private Major majorid;
    @OneToMany(mappedBy = "parentid")
    private Set<School> schoolSet;
    @JoinColumn(name = "PARENTID", referencedColumnName = "ID")
    @ManyToOne
    private School parentid;
    @OneToMany(mappedBy = "schoolId")
    private Set<Student> studentSet;
    @OneToMany(mappedBy = "schoolid")
    private Set<Teacschoolsubject> teacschoolsubjectSet;
    @OneToMany(mappedBy = "schoolid")
    private Set<Testpaper> testpaperSet;

    public School() {
    }

    public School(Integer id) {
        this.id = id;
    }

    public int getNameLength(){
        return 64;
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

    public Edulevel getEdulevel() {
        return edulevel;
    }

    public void setEdulevel(Edulevel edulevel) {
        this.edulevel = edulevel;
    }

    public Major getMajorid() {
        return majorid;
    }

    public void setMajorid(Major majorid) {
        this.majorid = majorid;
    }

    @XmlTransient
    public Set<School> getSchoolSet() {
        return schoolSet;
    }

    public void setSchoolSet(Set<School> schoolSet) {
        this.schoolSet = schoolSet;
    }

    public School getParentid() {
        return parentid;
    }

    public void setParentid(School parentid) {
        this.parentid = parentid;
    }

    @XmlTransient
    public Set<Student> getStudentSet() {
        return studentSet;
    }

    public void setStudentSet(Set<Student> studentSet) {
        this.studentSet = studentSet;
    }

    @XmlTransient
    public Set<Teacschoolsubject> getTeacschoolsubjectSet() {
        return teacschoolsubjectSet;
    }

    public void setTeacschoolsubjectSet(Set<Teacschoolsubject> teacschoolsubjectSet) {
        this.teacschoolsubjectSet = teacschoolsubjectSet;
    }

    @XmlTransient
    public Set<Testpaper> getTestpaperSet() {
        return testpaperSet;
    }

    public void setTestpaperSet(Set<Testpaper> testpaperset) {
        this.testpaperSet = testpaperset;
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
        if (!(object instanceof School)) {
            return false;
        }
        School other = (School) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return (null != getParentid()) ? getParentid().getName() + "-" + getName() : name;
    }

}
