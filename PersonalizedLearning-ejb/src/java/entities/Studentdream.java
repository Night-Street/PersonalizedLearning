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
 * @author hgs
 */
@Entity
@Table(name = "STUDENTDREAM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Studentdream.findAll", query = "SELECT s FROM Studentdream s")
    , @NamedQuery(name = "Studentdream.findById", query = "SELECT s FROM Studentdream s WHERE s.id = :id")
    , @NamedQuery(name = "Studentdream.findByMydream", query = "SELECT s FROM Studentdream s WHERE s.mydream = :mydream")
    , @NamedQuery(name = "Studentdream.findByMedia", query = "SELECT s FROM Studentdream s WHERE s.media = :media")
    , @NamedQuery(name = "Studentdream.findByKnowledges", query = "SELECT s FROM Studentdream s WHERE s.knowledges = :knowledges")})
public class Studentdream implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 10000)
    @Column(name = "MYDREAM")
    private String mydream;
    @Size(max = 30)
    private String title;
    @Size(max = 10000)
    private String plan;
    @Size(max = 10000)
    @Column(name = "MEDIA")
    private String media;
    @Size(max = 1000)
    @Column(name = "KNOWLEDGES")
    private String knowledges;
    @OneToMany(mappedBy = "dreamid")
    private Set<Realizationofmydream> realizationofmydreamCollection;
    @JoinColumn(name = "STUID", referencedColumnName = "ID")
    @ManyToOne
    private Student stuid;

    public Studentdream() {
    }

    public Studentdream(Integer id) {
        this.id = id;
    }

    public int getMydreamLength() {
        return 10000;
    }

    public int getTitleLength() {
        return 30;
    }

    public int getPlanLength() {
        return 10000;
    }

    public int getMediaLength() {
        return 10000;
    }

    public int getKnowledgesLength() {
        return 1000;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMydream() {
        return mydream;
    }

    public void setMydream(String mydream) {
        this.mydream = mydream;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(String knowledges) {
        this.knowledges = knowledges;
    }

    @XmlTransient
    public Set<Realizationofmydream> getRealizationofmydreamSet() {
        return realizationofmydreamCollection;
    }

    public void setRealizationofmydreamSet(Set<Realizationofmydream> realizationofmydreamCollection) {
        this.realizationofmydreamCollection = realizationofmydreamCollection;
    }

    public Student getStuid() {
        return stuid;
    }

    public void setStuid(Student stuid) {
        this.stuid = stuid;
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
        if (!(object instanceof Studentdream)) {
            return false;
        }
        Studentdream other = (Studentdream) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eintity.Studentdream[ id=" + id + " ]";
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the plan
     */
    public String getPlan() {
        return plan;
    }

    /**
     * @param plan the plan to set
     */
    public void setPlan(String plan) {
        this.plan = plan;
    }

}
