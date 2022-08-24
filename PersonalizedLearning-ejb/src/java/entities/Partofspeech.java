package entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "PARTOFSPEECH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partofspeech.findAll", query = "SELECT p FROM Partofspeech p")
    , @NamedQuery(name = "Partofspeech.findById", query = "SELECT p FROM Partofspeech p WHERE p.id = :id")
    , @NamedQuery(name = "Partofspeech.findByName", query = "SELECT p FROM Partofspeech p WHERE p.name = :name")
    , @NamedQuery(name = "Partofspeech.findByChinese", query = "SELECT p FROM Partofspeech p WHERE p.chinese = :chinese")
    , @NamedQuery(name = "Partofspeech.findByMemo", query = "SELECT p FROM Partofspeech p WHERE p.memo = :memo")})
public class Partofspeech   implements Serializable {

    @OneToMany(mappedBy = "c4")
    private Set<Knowledge> knowledgeSet;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", length = 20)
    private String name;
    @Column(name = "CHINESE", length = 30)
    private String chinese;
    @Column(name = "MEMO", length = 50)
    private String memo;

    public Partofspeech() {
    }

    public Partofspeech(Integer id) {
        this.id = id;
    }
    
    public int getNameLength(){
        return 20;
    }
    
    public int getChineseLength(){
        return 30;
    }
    
    public int getMemoLength(){
        return 50;
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

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
        if (!(object instanceof Partofspeech)) {
            return false;
        }
        Partofspeech other = (Partofspeech) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @XmlTransient
    public Set<Knowledge> getKnowledgeSet() {
        return knowledgeSet;
    }

    public void setKnowledgeSet(Set<Knowledge> knowledgeSet) {
        this.knowledgeSet = knowledgeSet;
    }
    
}
