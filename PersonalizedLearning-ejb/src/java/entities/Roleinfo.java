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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "ROLEINFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roleinfo.findAll", query = "SELECT r FROM Roleinfo r ORDER BY r.rolename")
    , @NamedQuery(name = "Roleinfo.findById", query = "SELECT r FROM Roleinfo r WHERE r.id = :id")
    , @NamedQuery(name = "Roleinfo.findByRolename", query = "SELECT r FROM Roleinfo r WHERE r.rolename = :rolename")
    , @NamedQuery(name = "Roleinfo.findByResources", query = "SELECT r FROM Roleinfo r WHERE r.resources = :resources")})
public class Roleinfo implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ROLENAME")
    private String rolename;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "RESOURCES", length = 100)
    private String resources;
    @OneToMany(mappedBy = "roleId")
    private Set<Student> studentSet;
    @OneToMany(mappedBy = "roleId")
    private Set<TeacherAdmin> teacherAdminSet;
    @OneToMany(mappedBy = "forRole")
    private Set<News> newsSet;
    @OneToMany(mappedBy = "roleid")
    private Set<Registeruser> registeruserSet;

    public Roleinfo() {
    }

    public Roleinfo(Integer id) {
        this.id = id;
    }

    public int getResourcesLength() {
        return 100;
    }

    public Roleinfo(Integer id, String rolename) {
        this.id = id;
        this.rolename = rolename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    @XmlTransient
    public Set<Student> getStudentSet() {
        return studentSet;
    }

    public void setStudentSet(Set<Student> studentSet) {
        this.studentSet = studentSet;
    }

    @XmlTransient
    public Set<TeacherAdmin> getTeacherAdminSet() {
        return teacherAdminSet;
    }

    public void setTeacherAdminSet(Set<TeacherAdmin> teacherAdminSet) {
        this.teacherAdminSet = teacherAdminSet;
    }

    @XmlTransient
    public Set<News> getNewsSet() {
        return newsSet;
    }

    public void setNewsSet(Set<News> newsSet) {
        this.newsSet = newsSet;
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
        if (!(object instanceof Roleinfo)) {
            return false;
        }
        Roleinfo other = (Roleinfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return rolename;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    /**
     * @return the registeruserSet
     */
    public Set<Registeruser> getRegisteruserSet() {
        return registeruserSet;
    }

    /**
     * @param registeruserSet the registeruserSet to set
     */
    public void setRegisteruserSet(Set<Registeruser> registeruserSet) {
        this.registeruserSet = registeruserSet;
    }

}
