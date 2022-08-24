/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hgs07
 */
@Entity
@Table(name = "EDGEAMONGSUBJECT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Edgeamongsubject.findAll", query = "SELECT e FROM Edgeamongsubject e")
    , @NamedQuery(name = "Edgeamongsubject.findById", query = "SELECT e FROM Edgeamongsubject e WHERE e.id = :id")
    , @NamedQuery(name = "Edgeamongsubject.findByWeight", query = "SELECT e FROM Edgeamongsubject e WHERE e.weight = :weight")
    , @NamedQuery(name = "Edgeamongsubject.findByMemo", query = "SELECT e FROM Edgeamongsubject e WHERE e.memo = :memo")})
public class Edgeamongsubject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "WEIGHT")
    private Integer weight;
    @Column(name = "MEMO", length = 128)
    private String memo;
    @JoinColumn(name = "PREDICATE", referencedColumnName = "ID")
    @ManyToOne
    private Predicate predicate;
    @JoinColumn(name = "PREDECESSORNODE", referencedColumnName = "ID")
    @ManyToOne
    private Subject predecessornode;
    @JoinColumn(name = "SUCCESSORNODE", referencedColumnName = "ID")
    @ManyToOne
    private Subject successornode;

    public Edgeamongsubject() {
    }

    public Edgeamongsubject(Integer id) {
        this.id = id;
    }

    public int getMemoLength(){
        return 128;
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

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public Subject getPredecessornode() {
        return predecessornode;
    }

    public void setPredecessornode(Subject predecessornode) {
        this.predecessornode = predecessornode;
    }

    public Subject getSuccessornode() {
        return successornode;
    }

    public void setSuccessornode(Subject successornode) {
        this.successornode = successornode;
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
        if (!(object instanceof Edgeamongsubject)) {
            return false;
        }
        Edgeamongsubject other = (Edgeamongsubject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
       return this.predecessornode.getName() + "->" + this.successornode.getName();
    }
    
}
