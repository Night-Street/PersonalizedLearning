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
 * @author hgs
 */
@Entity
@Table(name = "MAJORSUBJECT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Majorsubject.findAll", query = "SELECT m FROM Majorsubject m")
    , @NamedQuery(name = "Majorsubject.findById", query = "SELECT m FROM Majorsubject m WHERE m.id = :id")
    , @NamedQuery(name = "Majorsubject.findByOrderinnet", query = "SELECT m FROM Majorsubject m WHERE m.orderinnet = :orderinnet")
    , @NamedQuery(name = "Majorsubject.findByMajorid", query = "SELECT m FROM Majorsubject m WHERE m.majorid = :majorid")
    , @NamedQuery(name = "Majorsubject.findBySubjectid", query = "SELECT m FROM Majorsubject m WHERE m.subjectid = :subjectid")
})
public class Majorsubject   implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ORDERINNET")
    private Integer orderinnet;
    @JoinColumn(name = "MAJORID", referencedColumnName = "ID")
    @ManyToOne
    private Major majorid;
    @JoinColumn(name = "SUBJECTID", referencedColumnName = "ID")
    @ManyToOne
    private Subject subjectid;

    public Majorsubject() {
    }

    public Majorsubject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderinnet() {
        return orderinnet;
    }

    public void setOrderinnet(Integer orderinnet) {
        this.orderinnet = orderinnet;
    }

    public Major getMajorid() {
        return majorid;
    }

    public void setMajorid(Major majorid) {
        this.majorid = majorid;
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
        if (!(object instanceof Majorsubject)) {
            return false;
        }
        Majorsubject other = (Majorsubject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.majorid.getName()+"-"+this.subjectid.getName();
    }
    
}
