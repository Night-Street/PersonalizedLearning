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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs07
 */
@Entity
@Table(name = "PREDICATE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Predicate.findAll", query = "SELECT p FROM Predicate p")
    , @NamedQuery(name = "Predicate.findById", query = "SELECT p FROM Predicate p WHERE p.id = :id")
    , @NamedQuery(name = "Predicate.findByPname", query = "SELECT p FROM Predicate p WHERE p.pname = :pname")
    , @NamedQuery(name = "Predicate.findByMeaning", query = "SELECT p FROM Predicate p WHERE p.meaning = :meaning")
    , @NamedQuery(name = "Predicate.findByPorder", query = "SELECT p FROM Predicate p WHERE p.porder = :porder")
    , @NamedQuery(name = "Predicate.findByReflexivity", query = "SELECT p FROM Predicate p WHERE p.reflexivity = :reflexivity")
    , @NamedQuery(name = "Predicate.findByTransitivity", query = "SELECT p FROM Predicate p WHERE p.transitivity = :transitivity")
    , @NamedQuery(name = "Predicate.findBySymmetry", query = "SELECT p FROM Predicate p WHERE p.symmetry = :symmetry")})
public class Predicate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 20)
    @Column(name = "PNAME")
    private String pname;
    @Size(max = 1000)
    @Column(name = "MEANING")
    private String meaning;
    @Size(max = 20)
    @Column(name = "MYALIAS")
    private String myalias;
    @Size(max = 1000)
    @Column(name = "MEMO")
    private String memo;
    @Size(max = 10)
    @Column(name = "COLORNAME")
    private String colorname;

    @Column(name = "PORDER")
    private Integer porder;
    @Column(name = "REFLEXIVITY")
    private Boolean reflexivity;
    @Column(name = "TRANSITIVITY")
    private Boolean transitivity;
    @Column(name = "SYMMETRY")
    private Boolean symmetry;
    @OneToMany(mappedBy = "predicate")
    private Set<Edgeamongsubject> edgeamongsubjectSet;
    @OneToMany(mappedBy = "predicate")
    private Set<Edgeamongknowledge> edgeamongknowledgeSet;

    public Predicate() {
    }

    public Predicate(Integer id) {
        this.id = id;
    }
    
    public int getNameLength(){
        return 20;
    }
  
    public int getMeaningLength(){
        return 1000;
    }
   
    public int getMyaliasLength(){
        return 20;
    }
    
    public int getMemoLength(){
        return 1000;
    }
    
    public int getColornameLength(){
        return 10;
    }
   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    public String getColorname() {
        return colorname;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Integer getPorder() {
        return porder;
    }

    public void setPorder(Integer porder) {
        this.porder = porder;
    }

    public Boolean getReflexivity() {
        return reflexivity;
    }

    public void setReflexivity(Boolean reflexivity) {
        this.reflexivity = reflexivity;
    }

    public Boolean getTransitivity() {
        return transitivity;
    }

    public void setTransitivity(Boolean transitivity) {
        this.transitivity = transitivity;
    }

    public Boolean getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(Boolean symmetry) {
        this.symmetry = symmetry;
    }

    @XmlTransient
    public Set<Edgeamongsubject> getEdgeamongsubjectSet() {
        return edgeamongsubjectSet;
    }

    public void setEdgeamongsubjectSet(Set<Edgeamongsubject> edgeamongsubjectSet) {
        this.edgeamongsubjectSet = edgeamongsubjectSet;
    }

    @XmlTransient
    public Set<Edgeamongknowledge> getEdgeamongknowledgeSet() {
        return edgeamongknowledgeSet;
    }

    public void setEdgeamongknowledgeSet(Set<Edgeamongknowledge> edgeamongknowledgeSet) {
        this.edgeamongknowledgeSet = edgeamongknowledgeSet;
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
        if (!(object instanceof Predicate)) {
            return false;
        }
        Predicate other = (Predicate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return pname;
    }

    /**
     * @return the myalias
     */
    public String getMyalias() {
        return myalias;
    }

    /**
     * @param myalias the myalias to set
     */
    public void setMyalias(String myalias) {
        this.myalias = myalias;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getPnameLength() {
        return 20;
    }

}
