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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "REGISTERUSER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registeruser.findAll", query = "SELECT r FROM Registeruser r")
    , @NamedQuery(name = "Registeruser.findById", query = "SELECT r FROM Registeruser r WHERE r.id = :id")
    , @NamedQuery(name = "Registeruser.findByName", query = "SELECT r FROM Registeruser r WHERE r.name = :name")
    , @NamedQuery(name = "Registeruser.findByPassword", query = "SELECT r FROM Registeruser r WHERE r.password = :password")
    , @NamedQuery(name = "Registeruser.findByFirstname", query = "SELECT r FROM Registeruser r WHERE r.firstname = :firstname")
    , @NamedQuery(name = "Registeruser.findBySecondname", query = "SELECT r FROM Registeruser r WHERE r.secondname = :secondname")
    , @NamedQuery(name = "Registeruser.findByEmail", query = "SELECT r FROM Registeruser r WHERE r.email = :email")})
public class Registeruser extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 32)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 32)
    @Column(name = "SECONDNAME")
    private String secondname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 30)
    @Column(name = "EMAIL")
    private String email;
    @JoinColumn(name = "ROLEID", referencedColumnName = "ID")
    @ManyToOne
    private Roleinfo roleid;

    public Registeruser() {
    }

    public Registeruser(Integer id) {
        this.id = id;
    }

    public Registeruser(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roleinfo getRoleid() {
        return roleid;
    }

    public void setRoleid(Roleinfo roleid) {
        this.roleid = roleid;
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
        if (!(object instanceof Registeruser)) {
            return false;
        }
        Registeruser other = (Registeruser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eintity.Registeruser[ id=" + id + " ]";
    }
    
}
