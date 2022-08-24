/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Administrator
 */
@Entity
@Table(name = "APPOINTMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appointment.findAll", query = "SELECT a FROM Appointment a")
    , @NamedQuery(name = "Appointment.findById", query = "SELECT a FROM Appointment a WHERE a.id = :id")
    , @NamedQuery(name = "Appointment.findByStarttime", query = "SELECT a FROM Appointment a WHERE a.starttime = :starttime")
    , @NamedQuery(name = "Appointment.findByEndtime", query = "SELECT a FROM Appointment a WHERE a.endtime = :endtime")})
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "STARTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "ENDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @JoinColumn(name = "APPOINTMENTMESSAGE", referencedColumnName = "ID")
    @ManyToOne
    private Appointmentmessage appointmentmessage;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentid;

    public Appointment() {
    }

    public Appointment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Appointmentmessage getAppointmentmessage() {
        return appointmentmessage;
    }

    public void setAppointmentmessage(Appointmentmessage appointmentmessage) {
        this.appointmentmessage = appointmentmessage;
    }

    public Student getStudentid() {
        return studentid;
    }

    public void setStudentid(Student studentid) {
        this.studentid = studentid;
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
        if (!(object instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eintity.Appointment[ id=" + id + " ]";
    }
    
}
