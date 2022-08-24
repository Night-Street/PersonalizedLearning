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
 * @author hadoop
 */
@Entity
@Table(name = "CONTRIBUTORS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contributors.findAll", query = "SELECT c FROM Contributors c")
    , @NamedQuery(name = "Contributors.findById", query = "SELECT c FROM Contributors c WHERE c.id = :id")
    , @NamedQuery(name = "Contributors.findByCName", query = "SELECT c FROM Contributors c WHERE c.cName = :cName")
    , @NamedQuery(name = "Contributors.findByCYear", query = "SELECT c FROM Contributors c WHERE c.cYear = :cYear")
    , @NamedQuery(name = "Contributors.findByCMonth", query = "SELECT c FROM Contributors c WHERE c.cMonth = :cMonth")
     , @NamedQuery(name = "Contributors.findByStage", query = "SELECT c FROM Contributors c WHERE c.stage = :stage")
    , @NamedQuery(name = "Contributors.findByCTask", query = "SELECT c FROM Contributors c WHERE c.cTask = :cTask")})
public class Contributors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "C_NAME", length = 20)
    private String cName;
    @Column(name = "C_YEAR")
    private Integer cYear;
    @Column(name = "C_MONTH")
    private Integer cMonth;
    @Column(name = "C_TASK", length =100)
    private String cTask;
    @JoinColumn(name = "STAGE", referencedColumnName = "ID")
    @ManyToOne
    private Edulevel stage;

    public Contributors() {
    }
    
    public int getCNameLength(){
        return 20;
    }
    
    public int getCTaskLength(){
        return 100;
    }

    public Contributors(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public Integer getCYear() {
        return cYear;
    }

    public void setCAge(Integer cYear) {
        this.cYear = cYear;
    }

    public Integer getCMonth() {
        return cMonth;
    }

    public void setCMonth(Integer cMonth) {
        this.cMonth = cMonth;
    }
    public String getCTask() {
        return cTask;
    }

    public void setCTask(String cTask) {
        this.cTask = cTask;
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
        if (!(object instanceof Contributors)) {
            return false;
        }
        Contributors other = (Contributors) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Contributors[ id=" + id + " ]";
    }

    public Edulevel getStage() {
        return stage;
    }

    public void setStage(Edulevel stage) {
        this.stage = stage;
    }
    
}