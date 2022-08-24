/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelController.viewerController;

import entities.Student;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author hgs
 */
public class StudentScore implements Serializable {

    private Student student;
    private List<Double> score;
    private float averageScore;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Double> getScore() {
        return score;
    }

    public void setScore(List<Double> score) {
        this.score = score;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

}
