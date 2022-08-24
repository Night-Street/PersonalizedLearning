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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "APPOINTMENTMESSAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appointmentmessage.findAll", query = "SELECT a FROM Appointmentmessage a")
    , @NamedQuery(name = "Appointmentmessage.findById", query = "SELECT a FROM Appointmentmessage a WHERE a.id = :id")
    , @NamedQuery(name = "Appointmentmessage.findByMessage", query = "SELECT a FROM Appointmentmessage a WHERE a.message = :message")})
public class Appointmentmessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 1000)
    @Column(name = "MESSAGE")
    private String message;
    @OneToMany(mappedBy = "appointmentmessage")
    private Set<Appointment> appointmentSet;
    @JoinColumn(name = "TEACHERID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherid;

    public Appointmentmessage() {
    }

    public Appointmentmessage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlTransient
    public Set<Appointment> getAppointmentSet() {
        return appointmentSet;
    }

    public void setAppointmentSet(Set<Appointment> appointmentSet) {
        this.appointmentSet = appointmentSet;
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
        if (!(object instanceof Appointmentmessage)) {
            return false;
        }
        Appointmentmessage other = (Appointmentmessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eintity.Appointmentmessage[ id=" + id + " ]";
    }
    
}
