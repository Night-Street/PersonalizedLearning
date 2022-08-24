package entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hgs
 */
@Entity
@Table(name = "TESTPAPER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Testpaper.findAll", query = "SELECT t FROM Testpaper t")
    , @NamedQuery(name = "Testpaper.findById", query = "SELECT t FROM Testpaper t WHERE t.id = :id")
    , @NamedQuery(name = "Testpaper.findByDegree", query = "SELECT t FROM Testpaper t WHERE t.degree = :degree")
    , @NamedQuery(name = "Testpaper.findByCreateDate", query = "SELECT t FROM Testpaper t WHERE t.createDate = :createDate")
    , @NamedQuery(name = "Testpaper.findBySpecifiedInterval", query = "SELECT t FROM Testpaper t WHERE t.specifiedInterval = :specifiedInterval")
    , @NamedQuery(name = "Testpaper.findByIstest", query = "SELECT t FROM Testpaper t WHERE t.istest = :istest")
    , @NamedQuery(name = "Testpaper.findByStart", query = "SELECT t FROM Testpaper t WHERE t.start = :start")
    , @NamedQuery(name = "Testpaper.findByEndtime", query = "SELECT t FROM Testpaper t WHERE t.endtime = :endtime")
    , @NamedQuery(name = "Testpaper.findBySpecifieddate", query = "SELECT t FROM Testpaper t WHERE t.specifieddate = :specifieddate")
    , @NamedQuery(name = "Testpaper.findByschoolid", query = "SELECT t FROM Testpaper t WHERE t.schoolid = :schoolid")
    , @NamedQuery(name = "Testpaper.findBysubjectids", query = "SELECT t FROM Testpaper t WHERE t.subjectids = :subjectids")
})
public class Testpaper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DEGREE")
    private Integer degree;
    @Column(name = "SCORE")
    private Integer score;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "SPECIFIED_INTERVAL")
    private Integer specifiedInterval;
    @Column(name = "ISTEST")
    private Boolean istest;
    @Column(name = "START")
    @Temporal(TemporalType.TIME)
    private Date start;
    @Column(name = "ENDTIME")
    @Temporal(TemporalType.TIME)
    private Date endtime;
    @Column(name = "SPECIFIEDDATE")
    @Temporal(TemporalType.DATE)
    private Date specifieddate;

    @Column(name = "SUBJECTIDS", length = 20)
    private String subjectids;

    @JoinColumn(name = "CREATETEACHERID", referencedColumnName = "ID")
    @ManyToOne
    private TeacherAdmin createteacherid;

    @OneToMany(mappedBy = "testpaperId")
    private Set<Studenttestpaper> studenttestpaperSet;
    @JoinColumn(name = "SCHOOLID", referencedColumnName = "ID")
    @ManyToOne
    private School schoolid;

    public Testpaper() {
    }

    public Testpaper(Integer id) {
        this.id = id;
    }

    public int getSubjectIdsLength(){
        return 20;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSpecifiedInterval() {
        return specifiedInterval;
    }

    public void setSpecifiedInterval(Integer specifiedInterval) {
        this.specifiedInterval = specifiedInterval;
    }

    public Boolean getIstest() {
        return istest;
    }

    public void setIstest(Boolean istest) {
        this.istest = istest;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getSpecifieddate() {
        return specifieddate;
    }

    public void setSpecifieddate(Date specifieddate) {
        this.specifieddate = specifieddate;
    }

    public TeacherAdmin getCreateteacherid() {
        return createteacherid;
    }

    public void setCreateteacherid(TeacherAdmin createteacherid) {
        this.createteacherid = createteacherid;
    }

    public School getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(School schoolid) {
        this.schoolid = schoolid;
    }

    @XmlTransient
    public Set<Studenttestpaper> getStudenttestpaperSet() {
        return studenttestpaperSet;
    }

    public void setStudenttestpaperSet(Set<Studenttestpaper> studenttestpaperSet) {
        this.studenttestpaperSet = studenttestpaperSet;
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
        if (!(object instanceof Testpaper)) {
            return false;
        }
        Testpaper other = (Testpaper) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.schoolid.getName() + this.getCreateteacherid().getName();
    }

    public String getSubjectids() {
        return subjectids;
    }

    public void setSubjectids(String subjectids) {
        this.subjectids = subjectids;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}
