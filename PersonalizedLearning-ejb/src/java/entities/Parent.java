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
@Table(name = "PARENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parent.findAll", query = "SELECT p FROM Parent p")
    , @NamedQuery(name = "Parent.findById", query = "SELECT p FROM Parent p WHERE p.id = :id")
    , @NamedQuery(name = "Parent.findByRoleId", query = "SELECT p FROM Parent p WHERE p.roleId = :roleId")
    , @NamedQuery(name = "Parent.findByName", query = "SELECT p FROM Parent p WHERE p.name = :name")
    , @NamedQuery(name = "Parent.findByThirdlogin", query = "SELECT p FROM Parent p WHERE p.thirdlogin = :thirdlogin")
    , @NamedQuery(name = "Parent.findByPassword", query = "SELECT p FROM Parent p WHERE p.password = :password")
    , @NamedQuery(name = "Parent.findByFirstname", query = "SELECT p FROM Parent p WHERE p.firstname = :firstname")
    , @NamedQuery(name = "Parent.findBySecondname", query = "SELECT p FROM Parent p WHERE p.secondname = :secondname")
    , @NamedQuery(name = "Parent.findByEmail", query = "SELECT p FROM Parent p WHERE p.email = :email")
    , @NamedQuery(name = "Parent.findByPhone", query = "SELECT p FROM Parent p WHERE p.phone = :phone")})
public class Parent extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;
    @Size(max = 100)
    @Column(name = "THIRDLOGIN")
    private String thirdlogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 32)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 32)
    @Column(name = "SECONDNAME")
    private String secondname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="电子邮件无效")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "EMAIL")
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="电话/传真格式无效, 应为 xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(name = "PHONE")
    private String phone;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Roleinfo roleId;
    @OneToMany(mappedBy = "parentid")
    private Set<Reexamination> reexaminationCollection;
    @OneToMany(mappedBy = "parentid")
    private Set<Student> studentCollection;

    public Parent() {

    }

    public Parent(Integer id) {
        this.id = id;
    }

    public Parent(Integer id, Roleinfo roleId, String name, String password) {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.password = password;
    }

    public String getThirdlogin() {
        return thirdlogin;
    }

    public void setThirdlogin(String thirdlogin) {
        this.thirdlogin = thirdlogin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Student> getStudents() {
        return studentCollection;
    }

    public void setStudentid(Set<Student> students) {
        this.studentCollection = students;
    }

    @XmlTransient
    public Set<Reexamination> getReexaminationCollection() {
        return reexaminationCollection;
    }

    public void setReexaminationCollection(Set<Reexamination> reexaminationCollection) {
        this.reexaminationCollection = reexaminationCollection;
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
        if (!(object instanceof Parent)) {
            return false;
        }
        Parent other = (Parent) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "enitty.Parent[ id=" + id + " ]";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.setPassword(password);
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getSecondname() {
        return secondname;
    }

    @Override
    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Roleinfo getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(Roleinfo roleId) {
        this.roleId = roleId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
