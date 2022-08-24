package entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author haogs
 */
@Entity
@Table(name = "LEARNINGRESOURCE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Learningresource.findAll", query = "SELECT l FROM Learningresource l")
    , @NamedQuery(name = "Learningresource.findById", query = "SELECT l FROM Learningresource l WHERE l.id = :id")
    , @NamedQuery(name = "Learningresource.findByType", query = "SELECT l FROM Learningresource l WHERE l.type = :type")
    , @NamedQuery(name = "Learningresource.findByValueinfo", query = "SELECT l FROM Learningresource l WHERE l.valueinfo = :valueinfo")
    , @NamedQuery(name = "Learningresource.findByIsPassed", query = "SELECT l FROM Learningresource l WHERE l.isPassed = :isPassed")
    , @NamedQuery(name = "Learningresource.findByCreatedDate", query = "SELECT l FROM Learningresource l WHERE l.createdDate = :createdDate")
    , @NamedQuery(name = "Learningresource.findByKnowledgeId", query = "SELECT l FROM Learningresource l WHERE l.knowledgeId = :knowledgeId")
    , @NamedQuery(name = "Learningresource.findByEdgeamongKnowledgeId", query = "SELECT l FROM Learningresource l WHERE l.edgeamongKnowledgeId = :edgeamongKnowledgeId")
    , @NamedQuery(name = "Learningresource.findByStudentId", query = "SELECT l FROM Learningresource l WHERE l.studentId = :studentId")
    , @NamedQuery(name = "Learningresource.findByTeacherId", query = "SELECT l FROM Learningresource l WHERE l.teacherId = :teacherId")        
})
public class Learningresource   implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TYPE")
    private Integer type;
    @Column(name = "VALUEINFO", length = 1024)
    private String valueinfo;
    @Column(name = "IS_PASSED")
    private Boolean isPassed;
       @Column(name = "PRAISE_CNT")
    private Integer praiseCnt;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @JoinColumn(name = "KNOWLEDGE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Knowledge knowledgeId;
     @JoinColumn(name = "EDGEAMONGKNOWLEDGE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Edgeamongknowledge edgeamongKnowledgeId;
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "TEACHER_ID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherId;
    @OneToMany(mappedBy = "learningresourceId")
    private Set<Praise> praiseSet;

    public Learningresource() {
    }

    public Learningresource(Integer id) {
        this.id = id;
    }

    public int getValueInfo(){
        return 1024;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValueinfo() {
        return valueinfo;
    }

    public void setValueinfo(String valueinfo) {
        this.valueinfo = valueinfo;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Knowledge getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Knowledge knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public TeacherAdmin getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(TeacherAdmin teacherId) {
        this.teacherId = teacherId;
    }

    @XmlTransient
    public Set<Praise> getPraiseSet() {
        return praiseSet;
    }

    public void setPraiseSet(Set<Praise> praiseSet) {
        this.praiseSet = praiseSet;
    }

    public Integer getPraiseCnt() {
        return praiseCnt;
    }

    public void setPraiseCnt(Integer praiseCnt) {
        this.praiseCnt = praiseCnt;
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
        if (!(object instanceof Learningresource)) {
            return false;
        }
        Learningresource other = (Learningresource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Learningresource[ id=" + id + " ]";
    }

    public Edgeamongknowledge getEdgeamongKnowledgeId() {
        return edgeamongKnowledgeId;
    }

    public void setEdgeamongKnowledgeId(Edgeamongknowledge edgeamongKnowledgeId) {
        this.edgeamongKnowledgeId = edgeamongKnowledgeId;
    }
    
}
