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
@Table(name = "WRONG_REASON")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WrongReason.findAll", query = "SELECT w FROM WrongReason w ORDER BY w.name")
    , @NamedQuery(name = "WrongReason.findById", query = "SELECT w FROM WrongReason w WHERE w.id = :id")
    , @NamedQuery(name = "WrongReason.findByName", query = "SELECT w FROM WrongReason w WHERE w.name = :name")})
public class WrongReason   implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", length = 10)
    private String name;
    @OneToMany(mappedBy = "reasonId")
    private Set<WrongquestionCollection> wrongquestionCollectionSet;

    public WrongReason() {
    }

    public WrongReason(Integer id) {
        this.id = id;
    }

    public int getNameLength(){
        return 10;
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
    public Set<WrongquestionCollection> getWrongquestionCollectionSet() {
        return wrongquestionCollectionSet;
    }

    public void setWrongquestionCollectionSet(Set<WrongquestionCollection> wrongquestionCollectionSet) {
        this.wrongquestionCollectionSet = wrongquestionCollectionSet;
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
        if (!(object instanceof WrongReason)) {
            return false;
        }
        WrongReason other = (WrongReason) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
