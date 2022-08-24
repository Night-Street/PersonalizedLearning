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
@Table(name = "TEACHER_ADMIN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeacherAdmin.findAll", query = "SELECT t FROM TeacherAdmin t ORDER BY t.name")
    , @NamedQuery(name = "TeacherAdmin.findById", query = "SELECT t FROM TeacherAdmin t WHERE t.id = :id")
    , @NamedQuery(name = "TeacherAdmin.findByName", query = "SELECT t FROM TeacherAdmin t WHERE t.name = :name")
    , @NamedQuery(name = "TeacherAdmin.findByThirdlogin", query = "SELECT t FROM TeacherAdmin t WHERE t.thirdlogin = :thirdlogin")
    , @NamedQuery(name = "TeacherAdmin.findByFirstname", query = "SELECT t FROM TeacherAdmin t WHERE t.firstname = :firstname")
    , @NamedQuery(name = "TeacherAdmin.findBySecondname", query = "SELECT t FROM TeacherAdmin t WHERE t.secondname = :secondname")
    , @NamedQuery(name = "TeacherAdmin.findByEmail", query = "SELECT t FROM TeacherAdmin t WHERE t.email = :email")
    , @NamedQuery(name = "TeacherAdmin.findByPhone", query = "SELECT t FROM TeacherAdmin t WHERE t.phone = :phone")})
public class TeacherAdmin extends User implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "PASSWORD")
    private String password;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "THIRDLOGIN", length = 128)
    private String thirdlogin;
    @Column(name = "FIRSTNAME", length = 32)
    private String firstname;
    @Column(name = "SECONDNAME", length = 32)
    private String secondname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "EMAIL", length = 30)
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Column(name = "PHONE", length = 32)
    private String phone;
    @OneToMany(mappedBy = "userid")
    private Set<Teacherschedule> teacherscheduleSet;
    @OneToMany(mappedBy = "teacherid")
    private Set<Logs> logsSet;
    @OneToMany(mappedBy = "teacherid")
    private Set<Teacschoolsubject> teacschoolsubjectSet;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Roleinfo roleId;
    @OneToMany(mappedBy = "teacherno")
    private Set<News> newsSet;
    @OneToMany(mappedBy = "createteacherid")
    private Set<Testpaper> testpaperSet;
    @OneToMany(mappedBy = "teacherid")
    private Set<Teachermajor> teachermajorSet;

    @OneToMany(mappedBy = "teacherId")
    private Set<Studentaccupoints> studentaccupointsSet;
    @OneToMany(mappedBy = "teacherId")
    private Set<Learningresource> learningresourceSet;
    @OneToMany(mappedBy = "teacherId")
    private Set<PageColor> pageColorSet;

    @OneToMany(mappedBy = "teacherid")
    private Set<Reexamination> reexaminationFromTeacher;
    @OneToMany(mappedBy = "toteacher")
    private Set<Reexamination> reexaminationToTeacher;
    @OneToMany(mappedBy = "teacherid")
    private Set<Appointmentmessage> appointmentmessageSet;
    
    public TeacherAdmin() {
    }

    public TeacherAdmin(Integer id) {
        this.id = id;
    }

    public TeacherAdmin(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getThirdlogin() {
        return thirdlogin;
    }

    public void setThirdlogin(String thirdlogin) {
        this.thirdlogin = thirdlogin;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlTransient
    public Set<Teacherschedule> getTeacherscheduleSet() {
        return teacherscheduleSet;
    }

    public void setTeacherscheduleSet(Set<Teacherschedule> teacherscheduleSet) {
        this.teacherscheduleSet = teacherscheduleSet;
    }

    @XmlTransient
    public Set<Logs> getLogsSet() {
        return logsSet;
    }

    public void setLogsSet(Set<Logs> logsSet) {
        this.logsSet = logsSet;
    }

    @XmlTransient
    public Set<Teacschoolsubject> getTeacschoolsubjectSet() {
        return teacschoolsubjectSet;
    }

    public void setTeacschoolsubjectSet(Set<Teacschoolsubject> teacschoolsubjectSet) {
        this.teacschoolsubjectSet = teacschoolsubjectSet;
    }

    @Override
    public Roleinfo getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(Roleinfo roleId) {
        this.roleId = roleId;
    }

    @XmlTransient
    public Set<News> getNewsSet() {
        return newsSet;
    }

    public void setNewsSet(Set<News> newsSet) {
        this.newsSet = newsSet;
    }

    @XmlTransient
    public Set<Testpaper> getTestpaperSet() {
        return testpaperSet;
    }

    public void setTestpaperSet(Set<Testpaper> testpaperSet) {
        this.testpaperSet = testpaperSet;
    }

    @XmlTransient
    public Set<Teachermajor> getTeachermajorSet() {
        return teachermajorSet;
    }

    public void setTeachermajorSet(Set<Teachermajor> teachermajorSet) {
        this.teachermajorSet = teachermajorSet;
    }

    public Set<PageColor> getPageColorSet() {
        return pageColorSet;
    }

    public void setPageColorSet(Set<PageColor> pageColorSet) {
        this.pageColorSet = pageColorSet;
    }

    public Set<Learningresource> getLearningresourceSet() {
        return learningresourceSet;
    }

    public void setLearningresourceSet(Set<Learningresource> learningresourceSet) {
        this.learningresourceSet = learningresourceSet;
    }

    public Set<Studentaccupoints> getStudentaccupointsSet() {
        return studentaccupointsSet;
    }

    public void setStudentaccupointsSet(Set<Studentaccupoints> studentaccupointsSet) {
        this.studentaccupointsSet = studentaccupointsSet;
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
        if (!(object instanceof TeacherAdmin)) {
            return false;
        }
        TeacherAdmin other = (TeacherAdmin) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return name;
    }

    @XmlTransient
    public Set<Reexamination> getReexaminationCollection() {
        return reexaminationFromTeacher;
    }

    public void setReexaminationCollection(Set<Reexamination> reexaminationCollection) {
        this.reexaminationFromTeacher = reexaminationCollection;
    }

    @XmlTransient
    public Set<Reexamination> getReexaminationCollection1() {
        return reexaminationToTeacher;
    }

    public void setReexaminationCollection1(Set<Reexamination> reexaminationCollection1) {
        this.reexaminationToTeacher = reexaminationCollection1;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    @XmlTransient
    public Set<Appointmentmessage> getAppointmentmessageSet() {
        return appointmentmessageSet;
    }

    public void setAppointmentmessageSet(Set<Appointmentmessage> appointmentmessageSet) {
        this.appointmentmessageSet = appointmentmessageSet;
    }
}
