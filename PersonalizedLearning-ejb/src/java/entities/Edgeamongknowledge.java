/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hgs07
 */
@Entity
@Table(name = "EDGEAMONGKNOWLEDGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Edgeamongknowledge.findAll", query = "SELECT e FROM Edgeamongknowledge e")
    , @NamedQuery(name = "Edgeamongknowledge.findById", query = "SELECT e FROM Edgeamongknowledge e WHERE e.id = :id")
    , @NamedQuery(name = "Edgeamongknowledge.findByWeight", query = "SELECT e FROM Edgeamongknowledge e WHERE e.weight = :weight")
    , @NamedQuery(name = "Edgeamongknowledge.findByMemo", query = "SELECT e FROM Edgeamongknowledge e WHERE e.memo = :memo")})
public class Edgeamongknowledge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "WEIGHT")
    private Integer weight;
    @Column(name = "MEMO", length = 1280)
    private String memo;
    @OneToMany(mappedBy = "edgeamongKnowledgeId")
    private Set<Learningresource> learningresourceSet;
    @JoinColumn(name = "PREDECESSORNODE", referencedColumnName = "ID")
    @ManyToOne
    private Knowledge predecessornode;
    @JoinColumn(name = "SUCCESSORNODE", referencedColumnName = "ID")
    @ManyToOne
    private Knowledge successornode;
    @JoinColumn(name = "PREDICATE", referencedColumnName = "ID")
    @ManyToOne
    private Predicate predicate;

    public Edgeamongknowledge() {
    }

    public Edgeamongknowledge(Integer id) {
        this.id = id;
    }
    public int getMemoLength() {
        return 1280;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Knowledge getPredecessornode() {
        return predecessornode;
    }

    public void setPredecessornode(Knowledge predecessornode) {
        this.predecessornode = predecessornode;
    }

    public Knowledge getSuccessornode() {
        return successornode;
    }

    public void setSuccessornode(Knowledge successornode) {
        this.successornode = successornode;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
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
        if (!(object instanceof Edgeamongknowledge)) {
            return false;
        }
        Edgeamongknowledge other = (Edgeamongknowledge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.predecessornode.getSubjectId().getName() 
                + ": " +  this.successornode.getName()
                +"  -->  "+predicate.getPname()+"  -->  "
                + this.predecessornode.getName();
    }

    /**
     * @return the learningresourceSet
     */
    public Set<Learningresource> getLearningresourceSet() {
        return learningresourceSet;
    }

    /**
     * @param learningresourceSet the learningresourceSet to set
     */
    public void setLearningresourceSet(Set<Learningresource> learningresourceSet) {
        this.learningresourceSet = learningresourceSet;
    }
    
}
