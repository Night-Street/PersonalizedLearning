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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "REEXAMINATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reexamination.findAll", query = "SELECT r FROM Reexamination r")
    , @NamedQuery(name = "Reexamination.findById", query = "SELECT r FROM Reexamination r WHERE r.id = :id")
    , @NamedQuery(name = "Reexamination.findByCreatedDate", query = "SELECT r FROM Reexamination r WHERE r.createdDate = :createdDate")
    , @NamedQuery(name = "Reexamination.findByExaminDate", query = "SELECT r FROM Reexamination r WHERE r.examinDate = :examinDate")
    , @NamedQuery(name = "Reexamination.findByDetails", query = "SELECT r FROM Reexamination r WHERE r.details = :details")})
public class Reexamination implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Column(name = "EXAMIN_DATE")
    @Temporal(TemporalType.DATE)
    private Date examinDate;
    @Column(name = "DETAILS", length = 1000)
    private String details;
    @JoinColumn(name = "KNOWLEDGEID", referencedColumnName = "ID")
    @ManyToOne
    private Knowledge knowledgeid;
    @JoinColumn(name = "PARENTID", referencedColumnName = "ID")
    @ManyToOne
    private Parent parentid;
    @JoinColumn(name = "QUESTIONID", referencedColumnName = "ID")
    @ManyToOne
    private Question questionid;
    @JoinColumn(name = "STATUS", referencedColumnName = "ID")
    @ManyToOne
    private Statusofresources status;
    @JoinColumn(name = "STATUS2ND", referencedColumnName = "ID")
    @ManyToOne
    private Statusofresources status2nd;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentid;
    @JoinColumn(name = "TOTEACHER", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin toteacher;
    @JoinColumn(name = "TEACHERID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherid;
    @Column(name = "ISCREATE")
    private Boolean isCreate ;

    public Reexamination() {
    }

    public Reexamination(Integer id) {
        this.id = id;
    }
    
    public int getDetailsLength(){
        return 1000;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExaminDate() {
        return examinDate;
    }

    public void setExaminDate(Date examinDate) {
        this.examinDate = examinDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Knowledge getKnowledgeid() {
        return knowledgeid;
    }

    public void setKnowledgeid(Knowledge knowledgeid) {
        this.knowledgeid = knowledgeid;
    }

    public Parent getParentid() {
        return parentid;
    }

    public void setParentid(Parent parentid) {
        this.parentid = parentid;
    }

    public Question getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Question questionid) {
        this.questionid = questionid;
    }

    public Statusofresources getStatus() {
        return status;
    }

    public void setStatus(Statusofresources status) {
        this.status = status;
    }

    public Statusofresources getStatus2nd() {
        return status2nd;
    }

    public void setStatus2nd(Statusofresources status2nd) {
        this.status2nd = status2nd;
    }

    public Student getStudentid() {
        return studentid;
    }

    public void setStudentid(Student studentid) {
        this.studentid = studentid;
    }

    public TeacherAdmin getToteacher() {
        return toteacher;
    }

    public void setToteacher(TeacherAdmin toteacher) {
        this.toteacher = toteacher;
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
        if (!(object instanceof Reexamination)) {
            return false;
        }
        Reexamination other = (Reexamination) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "enitty.Reexamination[ id=" + id + " ]";
    }

    /**
     * @return the isCreate
     */
    public Boolean getIsCreate() {
        return isCreate;
    }

    /**
     * @param isCreate the isCreate to set
     */
    public void setIsCreate(Boolean isCreate) {
        this.isCreate = isCreate;
    }

}
