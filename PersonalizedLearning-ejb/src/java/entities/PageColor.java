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
 * @author haogs
 */
@Entity
@Table(name = "PAGE_COLOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PageColor.findAll", query = "SELECT p FROM PageColor p")
    , @NamedQuery(name = "PageColor.findById", query = "SELECT p FROM PageColor p WHERE p.id = :id")
    , @NamedQuery(name = "PageColor.findByRate", query = "SELECT p FROM PageColor p WHERE p.rate = :rate")
    , @NamedQuery(name = "PageColor.findByteacherId", query = "SELECT p FROM PageColor p WHERE p.teacherId = :teacherId")
    , @NamedQuery(name = "PageColor.findByuserId", query = "SELECT p FROM PageColor p WHERE p.userId = :userId")        
})
public class PageColor   implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "RATE")
    private Integer rate;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private Student userId;
    @JoinColumn(name = "TEACHER_ID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherId;

    public PageColor() {
    }

    public PageColor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Student getUserId() {
        return userId;
    }

    public void setUserId(Student userId) {
        this.userId = userId;
    }

    public TeacherAdmin getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(TeacherAdmin teacherId) {
        this.teacherId = teacherId;
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
        if (!(object instanceof PageColor)) {
            return false;
        }
        PageColor other = (PageColor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PageColor[ id=" + id + " ]";
    }
    
}
