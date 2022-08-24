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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "LOGS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logs.findAll", query = "SELECT l FROM Logs l ORDER BY l.operationtime")
    , @NamedQuery(name = "Logs.findById", query = "SELECT l FROM Logs l WHERE l.id = :id")
    , @NamedQuery(name = "Logs.findByOperation", query = "SELECT l FROM Logs l WHERE l.operation = :operation")
    , @NamedQuery(name = "Logs.findByRecorderinfo", query = "SELECT l FROM Logs l WHERE l.recorderinfo = :recorderinfo")
    , @NamedQuery(name = "Logs.findByOperationtime", query = "SELECT l FROM Logs l WHERE l.operationtime = :operationtime")
    , @NamedQuery(name = "Logs.findByTablename", query = "SELECT l FROM Logs l WHERE l.tablename = :tablename")
    , @NamedQuery(name = "Logs.findBystudentid", query = "SELECT l FROM Logs l WHERE l.studentid = :studentid")
    , @NamedQuery(name = "Logs.findByTeacherid", query = "SELECT l FROM Logs l WHERE l.teacherid = :teacherid")
})
public class Logs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "OPERATION", length = 8)
    private String operation;
    @Column(name = "RECORDERINFO", length = 200)
    private String recorderinfo;
    @Column(name = "OPERATIONTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationtime;
    @Column(name = "TABLENAME", length = 20)
    private String tablename;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID")
    @ManyToOne
    private Student studentid;
    @JoinColumn(name = "TEACHERID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherid;

    public Logs() {
    }

    public Logs(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRecorderinfo() {
        return recorderinfo;
    }

    public void setRecorderinfo(String recorderinfo) {
        this.recorderinfo = recorderinfo;
    }

    public Date getOperationtime() {
        return operationtime;
    }

    public void setOperationtime(Date operationtime) {
        this.operationtime = operationtime;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Student getStudentid() {
        return studentid;
    }

    public void setStudentid(Student studentid) {
        this.studentid = studentid;
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
        if (!(object instanceof Logs)) {
            return false;
        }
        Logs other = (Logs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.getTeacherid().getName().equals("TNULL")) {
            sb.append(getTeacherid().getSecondname()).append(getTeacherid().getFirstname());
        }
        if (!this.getStudentid().getName().equals("SNULL")) {
            sb.append(getStudentid().getSecondname()).append(getStudentid().getFirstname());
        }
        sb.append(getTablename()).append("-").append(getRecorderinfo());
        return sb.toString();
    }

}
