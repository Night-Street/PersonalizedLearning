
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "SUBJECT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subject.findAll", query = "SELECT s FROM Subject s ORDER BY s.name")
    , @NamedQuery(name = "Subject.findById", query = "SELECT s FROM Subject s WHERE s.id = :id")
    , @NamedQuery(name = "Subject.findByName", query = "SELECT s FROM Subject s WHERE s.name = :name")
})
public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", length = 32)
    private String name;
    @OneToMany(mappedBy = "predecessornode")
    private Set<Edgeamongsubject> edgeamongsubjectSet;
    @OneToMany(mappedBy = "successornode")
    private Set<Edgeamongsubject> edgeamongsubjectSet1;
    @OneToMany(mappedBy = "subjectid")
    private Set<Teacschoolsubject> teacschoolsubjectSet;
    @OneToMany(mappedBy = "subjectid")
    private Set<Leadpoint> leadpointSet;
    @OneToMany(mappedBy = "subjectId")
    private Set<Knowledge> knowledgeSet;
    @OneToMany(mappedBy = "subjectid")
    private Set<Majorsubject> majorsubjectSet;

    public Subject() {
    }

    public Subject(Integer id) {
        this.id = id;
    }
    
    public int getNameLength(){
        return 32;
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

    @XmlTransient
    public Set<Edgeamongsubject> getPrecessorsubjectSet() {
        return edgeamongsubjectSet;
    }

    public void setPrecessorsubjectSet(Set<Edgeamongsubject> edgeamongsubjectSet) {
        this.edgeamongsubjectSet = edgeamongsubjectSet;
    }

    @XmlTransient
    public Set<Edgeamongsubject> getSuccessorsubjectSet() {
        return edgeamongsubjectSet1;
    }

    public void setSuccessorsubjectSet(Set<Edgeamongsubject> edgeamongsubjectSet1) {
        this.edgeamongsubjectSet1 = edgeamongsubjectSet1;
    }

    @XmlTransient
    public Set<Teacschoolsubject> getTeacschoolsubjectSet() {
        return teacschoolsubjectSet;
    }

    public void setTeacschoolsubjectSet(Set<Teacschoolsubject> teacschoolsubjectSet) {
        this.teacschoolsubjectSet = teacschoolsubjectSet;
    }

    @XmlTransient
    public Set<Leadpoint> getLeadpointSet() {
        return leadpointSet;
    }

    public void setLeadpointSet(Set<Leadpoint> leadpointSet) {
        this.leadpointSet = leadpointSet;
    }

    @XmlTransient
    public Set<Knowledge> getKnowledgeSet() {
        return knowledgeSet;
    }

    public void setKnowledgeSet(Set<Knowledge> knowledgeSet) {
        this.knowledgeSet = knowledgeSet;
    }

    @XmlTransient
    public Set<Majorsubject> getMajorsubjectSet() {
        return majorsubjectSet;
    }

    public void setMajorsubjectSet(Set<Majorsubject> majorsubjectSet) {
        this.majorsubjectSet = majorsubjectSet;
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
        if (!(object instanceof Subject)) {
            return false;
        }
        Subject other = (Subject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return name;
    }
}
