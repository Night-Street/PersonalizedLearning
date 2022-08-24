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
@Table(name = "QWQUESTIONWRONG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Qwquestionwrong.findAll", query = "SELECT q FROM Qwquestionwrong q")
    , @NamedQuery(name = "Qwquestionwrong.findByQid", query = "SELECT q FROM Qwquestionwrong q WHERE q.qid = :qid")
    , @NamedQuery(name = "Qwquestionwrong.findByQtype", query = "SELECT q FROM Qwquestionwrong q WHERE q.qtype = :qtype")
    , @NamedQuery(name = "Qwquestionwrong.findByQscore", query = "SELECT q FROM Qwquestionwrong q WHERE q.qscore = :qscore")
    , @NamedQuery(name = "Qwquestionwrong.findByQknowlege", query = "SELECT q FROM Qwquestionwrong q WHERE q.qknowlege = :qknowlege")
    , @NamedQuery(name = "Qwquestionwrong.findByQdegree", query = "SELECT q FROM Qwquestionwrong q WHERE q.qdegree = :qdegree")
    , @NamedQuery(name = "Qwquestionwrong.findByQanswer", query = "SELECT q FROM Qwquestionwrong q WHERE q.qanswer = :qanswer")
    , @NamedQuery(name = "Qwquestionwrong.findByQanalysis", query = "SELECT q FROM Qwquestionwrong q WHERE q.qanalysis = :qanalysis")
    , @NamedQuery(name = "Qwquestionwrong.findByQsecondcontent", query = "SELECT q FROM Qwquestionwrong q WHERE q.qsecondcontent = :qsecondcontent")
    , @NamedQuery(name = "Qwquestionwrong.findByQfigure", query = "SELECT q FROM Qwquestionwrong q WHERE q.qfigure = :qfigure")
    , @NamedQuery(name = "Qwquestionwrong.findByWid", query = "SELECT q FROM Qwquestionwrong q WHERE q.wid = :wid")
    , @NamedQuery(name = "Qwquestionwrong.findByWstuid", query = "SELECT q FROM Qwquestionwrong q WHERE q.wstuid = :wstuid")
    , @NamedQuery(name = "Qwquestionwrong.findByWqid", query = "SELECT q FROM Qwquestionwrong q WHERE q.wqid = :wqid")
    , @NamedQuery(name = "Qwquestionwrong.findByWreasonid", query = "SELECT q FROM Qwquestionwrong q WHERE q.wreasonid = :wreasonid")})
public class Qwquestionwrong implements Serializable {

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
    @Column(name = "WID")
    private int wid;
    @Column(name = "WSTUID")
    private Integer wstuid;
    @Column(name = "WQID")
    private Integer wqid;
    @Column(name = "WREASONID")
    private Integer wreasonid;
    @Size(max = 1000)
    @Column(name = "QVALUEINFO")
    private String qvalueinfo;

    public Qwquestionwrong() {
    }

    public int getQvalueinfoLength(){
        return 1000;
    }
    
    public int getQAnswerLength(){
        return 2048;
    }
   
    public int getQAnalysisLength(){
        return 1000;
    }
    
    public int getQSecondcontentLength(){
        return 1000;
    }
  
    public int getQFigureLength(){
        return 100;
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

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public Integer getWstuid() {
        return wstuid;
    }

    public void setWstuid(Integer wstuid) {
        this.wstuid = wstuid;
    }

    public Integer getWqid() {
        return wqid;
    }

    public void setWqid(Integer wqid) {
        this.wqid = wqid;
    }

    public Integer getWreasonid() {
        return wreasonid;
    }

    public void setWreasonid(Integer wreasonid) {
        this.wreasonid = wreasonid;
    }

    /**
     * @return the qvalueinfo
     */
    public String getQvalueinfo() {
        return qvalueinfo;
    }

    /**
     * @param qvalueinfo the qvalueinfo to set
     */
    public void setQvalueinfo(String qvalueinfo) {
        this.qvalueinfo = qvalueinfo;
    }
    
}
