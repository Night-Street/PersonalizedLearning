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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author haogs
 */
@Entity
@Table(name = "QUESKNOWLGSUB")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quesknowlgsub.findAll", query = "SELECT q FROM Quesknowlgsub q")
    , @NamedQuery(name = "Quesknowlgsub.findByQid", query = "SELECT q FROM Quesknowlgsub q WHERE q.qid = :qid")
    , @NamedQuery(name = "Quesknowlgsub.findByQtype", query = "SELECT q FROM Quesknowlgsub q WHERE q.qtype = :qtype")
    , @NamedQuery(name = "Quesknowlgsub.findByQscore", query = "SELECT q FROM Quesknowlgsub q WHERE q.qscore = :qscore")
    , @NamedQuery(name = "Quesknowlgsub.findByQknowlege", query = "SELECT q FROM Quesknowlgsub q WHERE q.qknowlege = :qknowlege")
    , @NamedQuery(name = "Quesknowlgsub.findByQdegree", query = "SELECT q FROM Quesknowlgsub q WHERE q.qdegree = :qdegree")
    , @NamedQuery(name = "Quesknowlgsub.findByQanswer", query = "SELECT q FROM Quesknowlgsub q WHERE q.qanswer = :qanswer")
    , @NamedQuery(name = "Quesknowlgsub.findByQanalysis", query = "SELECT q FROM Quesknowlgsub q WHERE q.qanalysis = :qanalysis")
    , @NamedQuery(name = "Quesknowlgsub.findByQsecondcontent", query = "SELECT q FROM Quesknowlgsub q WHERE q.qsecondcontent = :qsecondcontent")
    , @NamedQuery(name = "Quesknowlgsub.findByQfigure", query = "SELECT q FROM Quesknowlgsub q WHERE q.qfigure = :qfigure")
    , @NamedQuery(name = "Quesknowlgsub.findByKid", query = "SELECT q FROM Quesknowlgsub q WHERE q.kid = :kid")
    , @NamedQuery(name = "Quesknowlgsub.findByKsubjectid", query = "SELECT q FROM Quesknowlgsub q WHERE q.ksubjectid = :ksubjectid")
    , @NamedQuery(name = "Quesknowlgsub.findByKname", query = "SELECT q FROM Quesknowlgsub q WHERE q.kname = :kname")
    , @NamedQuery(name = "Quesknowlgsub.findByKdetails", query = "SELECT q FROM Quesknowlgsub q WHERE q.kdetails = :kdetails")
    , @NamedQuery(name = "Quesknowlgsub.findBySid", query = "SELECT q FROM Quesknowlgsub q WHERE q.sid = :sid")
    , @NamedQuery(name = "Quesknowlgsub.findBySname", query = "SELECT q FROM Quesknowlgsub q WHERE q.sname = :sname")
    , @NamedQuery(name = "Quesknowlgsub.findBySmajor", query = "SELECT q FROM Quesknowlgsub q WHERE q.smajor = :smajor")})
public class Quesknowlgsub implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QID")
    @Id
    private int qid;
    @Column(name = "QTYPE")
    private Integer qtype;
    @Column(name = "QSCORE")
    private Integer qscore;
    @Column(name = "QKNOWLEGE")
    private Integer qknowlege;
    @Column(name = "QDEGREE")
    private Integer qdegree;
    @Size(max = 2048)
    @Column(name = "QANSWER")
    private String qanswer;
    @Size(max = 1000)
    @Column(name = "QANALYSIS")
    private String qanalysis;
    @Size(max = 1000)
    @Column(name = "QSECONDCONTENT")
    private String qsecondcontent;
    @Size(max = 100)
    @Column(name = "QFIGURE")
    private String qfigure;
    @Basic(optional = false)
    @NotNull
    @Column(name = "KID")
    private int kid;
    @Column(name = "KSUBJECTID")
    private Integer ksubjectid;
    @Size(max = 32)
    @Column(name = "KNAME")
    private String kname;
    @Size(max = 10240)
    @Column(name = "KDETAILS")
    private String kdetails;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SID")
    private int sid;
    @Size(max = 32)
    @Column(name = "SNAME")
    private String sname;
    @Column(name = "SMAJOR")
    private Integer smajor;

    public Quesknowlgsub() {
    }
    
    public int getQanswerLength(){
        return 2048;
    }
    
    public int getQAnalysisLength(){
        return 1000;
    }
    
    public int getQSecondcontentLength(){
        return 1000;
    }
  
    public int getNameLength(){
        return 32;
    }
    
    public int getKdetailsLength(){
        return 10240;
    }
   
    public int getSName(){
        return 32;
    }
   
    public int getId() {
        return qid;
    }

    public void setId(int qid) {
        this.qid = qid;
    }

    public Integer getQtype() {
        return qtype;
    }

    public void setQtype(Integer qtype) {
        this.qtype = qtype;
    }

    public Integer getQscore() {
        return qscore;
    }

    public void setQscore(Integer qscore) {
        this.qscore = qscore;
    }

    public Integer getQknowlege() {
        return qknowlege;
    }

    public void setQknowlege(Integer qknowlege) {
        this.qknowlege = qknowlege;
    }

    public Integer getQdegree() {
        return qdegree;
    }

    public void setQdegree(Integer qdegree) {
        this.qdegree = qdegree;
    }

    public String getQanswer() {
        return qanswer;
    }

    public void setQanswer(String qanswer) {
        this.qanswer = qanswer;
    }

    public String getQanalysis() {
        return qanalysis;
    }

    public void setQanalysis(String qanalysis) {
        this.qanalysis = qanalysis;
    }

    public String getQsecondcontent() {
        return qsecondcontent;
    }

    public void setQsecondcontent(String qsecondcontent) {
        this.qsecondcontent = qsecondcontent;
    }

    public String getQfigure() {
        return qfigure;
    }

    public void setQfigure(String qfigure) {
        this.qfigure = qfigure;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public Integer getKsubjectid() {
        return ksubjectid;
    }

    public void setKsubjectid(Integer ksubjectid) {
        this.ksubjectid = ksubjectid;
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname;
    }

    public String getKdetails() {
        return kdetails;
    }

    public void setKdetails(String kdetails) {
        this.kdetails = kdetails;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getSmajor() {
        return smajor;
    }

    public void setSmajor(Integer smajor) {
        this.smajor = smajor;
    }
    
}
