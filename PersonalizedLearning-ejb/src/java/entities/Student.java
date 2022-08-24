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
@Table(name = "STUDENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s ORDER BY s.name")
    , @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id")
    , @NamedQuery(name = "Student.findByName", query = "SELECT s FROM Student s WHERE s.name = :name")
    , @NamedQuery(name = "Student.findByThirdlogin", query = "SELECT s FROM Student s WHERE s.thirdlogin = :thirdlogin")
    , @NamedQuery(name = "Student.findByFirstname", query = "SELECT s FROM Student s WHERE s.firstname = :firstname")
    , @NamedQuery(name = "Student.findBySecondname", query = "SELECT s FROM Student s WHERE s.secondname = :secondname")
    , @NamedQuery(name = "Student.findByEmail", query = "SELECT s FROM Student s WHERE s.email = :email")
    , @NamedQuery(name = "Student.findByPhone", query = "SELECT s FROM Student s WHERE s.phone = :phone")
    , @NamedQuery(name = "Student.findByStudentidinschool", query = "SELECT s FROM Student s WHERE s.studentidinschool = :studentidinschool")
    , @NamedQuery(name = "Student.findByschoolId", query = "SELECT s FROM Student s WHERE s.schoolId = :schoolId")
})
public class Student extends User implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "student")
    private Set<Questionstudentcosttime> questionstudentcosttimeSet;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "THIRDLOGIN", length = 100)
    private String thirdlogin;
    @Column(name = "FIRSTNAME", length = 32)
    private String firstname;
    @Column(name = "SECONDNAME", length = 32)
    private String secondname;
    //@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "EMAIL", length = 30)
    private String email;
    //@Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Column(name = "PHONE", length = 20)
    private String phone;
    @Column(name = "STUDENTIDINSCHOOL", length = 32)
    private String studentidinschool;
    @Column(name = "PASSWORD", length = 128)
    private String password;
    @OneToMany(mappedBy = "studentId")
    private Set<WrongquestionCollection> wrongquestionCollectionSet;
    @OneToMany(mappedBy = "studentid")
    private Set<Logs> logsSet;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Roleinfo roleId;
    @JoinColumn(name = "SCHOOL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private School schoolId;
    @OneToMany(mappedBy = "userid")
    private Set<Studentschedule> studentscheduleSet;
    @OneToMany(mappedBy = "studentId")
    private Set<Studenttestpaper> studenttestpaperSet;
    @OneToMany(mappedBy = "studentId")
    private Set<Leadpoint> leadpointSet;

    @OneToMany(mappedBy = "studentId")
    private Set<Studentaccupoints> studentaccupointsSet;
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    @OneToMany(mappedBy = "studentId")
    private Set<Learningresource> learningresourceSet;
    @JoinColumn(name = "PARENTID", referencedColumnName = "ID")
    @ManyToOne
    private Parent parentid;
    @OneToMany(mappedBy = "userId")
    private Set<PageColor> pageColorSet;
    @OneToMany(mappedBy = "studentId")
    private Set<Praise> praiseSet;
    @OneToMany(mappedBy = "studentid")
    private Set<Reexamination> reexaminationCollection;
    @OneToMany(mappedBy = "stuid")
    private Set<Studentdream> studentdreamCollection;
    @OneToMany(mappedBy = "studentid")
    private Set<Appointment> appointmentSet;

    public Student() {
    }

    public Student(Integer id) {
        this.id = id;
    }

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public String getStudentidinschool() {
        return studentidinschool;
    }

    public void setStudentidinschool(String studentidinschool) {
        this.studentidinschool = studentidinschool;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public Set<WrongquestionCollection> getWrongquestionCollectionSet() {
        return wrongquestionCollectionSet;
    }

    public void setWrongquestionCollectionSet(Set<WrongquestionCollection> wrongquestionCollectionSet) {
        this.wrongquestionCollectionSet = wrongquestionCollectionSet;
    }

    @XmlTransient
    public Set<Logs> getLogsSet() {
        return logsSet;
    }

    public void setLogsSet(Set<Logs> logsSet) {
        this.logsSet = logsSet;
    }

    @Override
    public Roleinfo getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(Roleinfo roleId) {
        this.roleId = roleId;
    }

    public School getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(School schoolId) {
        this.schoolId = schoolId;
    }

    @XmlTransient
    public Set<Studentschedule> getStudentscheduleSet() {
        return studentscheduleSet;
    }

    public void setStudentscheduleSet(Set<Studentschedule> studentscheduleSet) {
        this.studentscheduleSet = studentscheduleSet;
    }

    @XmlTransient
    public Set<Studenttestpaper> getStudenttestpaperSet() {
        return studenttestpaperSet;
    }

    public void setStudenttestpaperSet(Set<Studenttestpaper> studenttestpaperSet) {
        this.studenttestpaperSet = studenttestpaperSet;
    }

    @XmlTransient
    public Set<Leadpoint> getLeadpointSet() {
        return leadpointSet;
    }

    public void setLeadpointSet(Set<Leadpoint> leadpointSet) {
        this.leadpointSet = leadpointSet;
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
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public Set<Praise> getPraiseSet() {
        return praiseSet;
    }

    public void setPraiseSet(Set<Praise> praiseSet) {
        this.praiseSet = praiseSet;
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
    public String toString() {
        return secondname + firstname;
    }

    @XmlTransient
    public Set<Questionstudentcosttime> getQuestionstudentcosttimeSet() {
        return questionstudentcosttimeSet;
    }

    public void setQuestionstudentcosttimeSet(Set<Questionstudentcosttime> questionstudentcosttimeSet) {
        this.questionstudentcosttimeSet = questionstudentcosttimeSet;
    }

    @XmlTransient
    public Set<Studentdream> getStudentdreamSet() {
        return studentdreamCollection;
    }

    public void setStudentdreamCollection(Set<Studentdream> studentdreamCollection) {
        this.studentdreamCollection = studentdreamCollection;
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
    public Set<Appointment> getAppointmentSet() {
        return appointmentSet;
    }

    public void setAppointmentSet(Set<Appointment> appointmentSet) {
        this.appointmentSet = appointmentSet;
    }

    public Parent getParentid() {
        return parentid;
    }

    public void setParentid(Parent parentid) {
        this.parentid = parentid;
    }
}
