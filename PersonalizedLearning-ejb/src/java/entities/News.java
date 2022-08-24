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
 * @author hgs
 */
@Entity
@Table(name = "NEWS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "News.findAll", query = "SELECT n FROM News n")
    , @NamedQuery(name = "News.findById", query = "SELECT n FROM News n WHERE n.id = :id")
    , @NamedQuery(name = "News.findByContent", query = "SELECT n FROM News n WHERE n.content = :content")
    , @NamedQuery(name = "News.findByInputdate", query = "SELECT n FROM News n WHERE n.inputdate = :inputdate")
    , @NamedQuery(name = "News.findByNewstitle", query = "SELECT n FROM News n WHERE n.newstitle = :newstitle")
    , @NamedQuery(name = "News.findByRole", query = "SELECT n FROM News n WHERE n.forRole = :role")
    , @NamedQuery(name = "News.findByTeacher", query = "SELECT n FROM News n WHERE n.teacherno = :teacher")
})
public class News implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CONTENT", length = 256)
    private String content;
    @Column(name = "INPUTDATE")
    @Temporal(TemporalType.DATE)
    private Date inputdate;
    @Column(name = "NEWSTITLE", length = 32)
    private String newstitle;
    @JoinColumn(name = "FOR_ROLE", referencedColumnName = "ID")
    @ManyToOne
    private Roleinfo forRole;
    @JoinColumn(name = "TEACHERNO", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin teacherno;

    public News() {
    }

    public News(Integer id) {
        this.id = id;
    }
    public int getContentLength(){
        return 256;
    }
    
    public int getNewTitle(){
        return 32;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getInputdate() {
        return inputdate;
    }

    public void setInputdate(Date inputdate) {
        this.inputdate = inputdate;
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public Roleinfo getForRole() {
        return forRole;
    }

    public void setForRole(Roleinfo forRole) {
        this.forRole = forRole;
    }

    public TeacherAdmin getTeacherno() {
        return teacherno;
    }

    public void setTeacherno(TeacherAdmin teacherno) {
        this.teacherno = teacherno;
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
        if (!(object instanceof News)) {
            return false;
        }
        News other = (News) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.newstitle;
    }

}
