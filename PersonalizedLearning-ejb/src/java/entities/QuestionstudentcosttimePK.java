/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author haogs
 */
@Embeddable
public class QuestionstudentcosttimePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "QUESTIONID")
    private int questionid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STUDENTID")
    private int studentid;

    public QuestionstudentcosttimePK() {
    }

    public QuestionstudentcosttimePK(int questionid, int studentid) {
        this.questionid = questionid;
        this.studentid = studentid;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) questionid;
        hash += (int) studentid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionstudentcosttimePK)) {
            return false;
        }
        QuestionstudentcosttimePK other = (QuestionstudentcosttimePK) object;
        if (this.questionid != other.questionid) {
            return false;
        }
        if (this.studentid != other.studentid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.QuestionstudentcosttimePK[ questionid=" + questionid + ", studentid=" + studentid + " ]";
    }
    
}
