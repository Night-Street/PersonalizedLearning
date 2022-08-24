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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "RESOURCEINFO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resourceinfo.findAll", query = "SELECT r FROM Resourceinfo r ORDER BY r.menuorder")
    , @NamedQuery(name = "Resourceinfo.findById", query = "SELECT r FROM Resourceinfo r WHERE r.id = :id")
    , @NamedQuery(name = "Resourceinfo.findByName", query = "SELECT r FROM Resourceinfo r WHERE r.name = :name")
    , @NamedQuery(name = "Resourceinfo.findByValueinfo", query = "SELECT r FROM Resourceinfo r WHERE r.valueinfo = :valueinfo")
    , @NamedQuery(name = "Resourceinfo.findByMenuorder", query = "SELECT r FROM Resourceinfo r WHERE r.menuorder = :menuorder")})
public class Resourceinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", length = 16)
    private String name;
    @Column(name = "VALUEINFO", length = 100)
    private String valueinfo;
    @Column(name = "MENUORDER")
    private Integer menuorder;
    @OneToMany(mappedBy = "parentid")
    private Set<Resourceinfo> resourceinfoSet;
    @JoinColumn(name = "PARENTID", referencedColumnName = "ID")
    @ManyToOne
    private Resourceinfo parentid;

    public Resourceinfo() {
    }

    public Resourceinfo(Integer id) {
        this.id = id;
    }
    
    public int getNameLength(){
        return 16;
    }

    public int getValueinfoLength(){
        return 100;
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

    public String getValueinfo() {
        return valueinfo;
    }

    public void setValueinfo(String valueinfo) {
        this.valueinfo = valueinfo;
    }

    public Integer getMenuorder() {
        return menuorder;
    }

    public void setMenuorder(Integer menuorder) {
        this.menuorder = menuorder;
    }

    @XmlTransient
    public Set<Resourceinfo> getResourceinfoSet() {
        return resourceinfoSet;
    }

    public void setResourceinfoSet(Set<Resourceinfo> resourceinfoSet) {
        this.resourceinfoSet = resourceinfoSet;
    }

    public Resourceinfo getParentid() {
        return parentid;
    }

    public void setParentid(Resourceinfo parentid) {
        this.parentid = parentid;
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
        if (!(object instanceof Resourceinfo)) {
            return false;
        }
        Resourceinfo other = (Resourceinfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
