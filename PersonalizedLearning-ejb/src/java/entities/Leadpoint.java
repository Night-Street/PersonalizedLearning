/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author hgs
 */
@Entity
@Table(name = "LEADPOINT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Leadpoint.findAll", query = "SELECT l FROM Leadpoint l")
    , @NamedQuery(name = "Leadpoint.findById", query = "SELECT l FROM Leadpoint l WHERE l.id = :id")
    , @NamedQuery(name = "Leadpoint.findByKnowledgeId", query = "SELECT l FROM Leadpoint l WHERE l.knowledgeId = :knowledgeId")
    , @NamedQuery(name = "Leadpoint.findByCreateDate", query = "SELECT l FROM Leadpoint l WHERE l.createDate = :createDate")
    , @NamedQuery(name = "Leadpoint.findByStudent", query = "SELECT l FROM Leadpoint l WHERE l.studentId = :studentId")
    , @NamedQuery(name = "Leadpoint.findBySubject", query = "SELECT l FROM Leadpoint l WHERE l.subjectid = :subjectid")        
})
public class Leadpoint   implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "KNOWLEDGE_ID", length = 10240)
    private String knowledgeId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @OneToMany(mappedBy = "leadpointId")
    private Set<Studenttestpaper> studenttestpaperSet;
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "SUBJECTID", referencedColumnName = "ID")
    @ManyToOne
    private Subject subjectid;

    public Leadpoint() {
    }

    public int getKnowledgeidLength(){
        return 10240;
    }
    public Leadpoint(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @XmlTransient
    public Set<Studenttestpaper> getStudenttestpaperSet() {
        return studenttestpaperSet;
    }

    public void setStudenttestpaperSet(Set<Studenttestpaper> studenttestpaperSet) {
        this.studenttestpaperSet = studenttestpaperSet;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Subject getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(Subject subjectid) {
        this.subjectid = subjectid;
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
        if (!(object instanceof Leadpoint)) {
            return false;
        }
        Leadpoint other = (Leadpoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.studentId.getName()+"-"+this.getSubjectid().getName();
    }
    
}
