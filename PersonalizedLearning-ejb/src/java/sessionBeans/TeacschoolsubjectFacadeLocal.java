/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.School;
import entities.Subject;
import entities.TeacherAdmin;
import entities.Teacschoolsubject;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface TeacschoolsubjectFacadeLocal {

    void create(Teacschoolsubject teacschoolsubject);

    void edit(Teacschoolsubject teacschoolsubject);

    void remove(Teacschoolsubject teacschoolsubject);

    Teacschoolsubject find(Object id);

    List<Teacschoolsubject> findAll();

    List<Teacschoolsubject> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    public List<Teacschoolsubject> getTeacschoolsubject4School(School school);

    public List<Teacschoolsubject> getTeacschoolsubject4Subject(Subject subject);
    
    public List<Teacschoolsubject> getTeacschoolsubject4Teacher(TeacherAdmin teacher);
}
