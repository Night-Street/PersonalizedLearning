/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Learningresource;
import entities.Logs;
import entities.Student;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.Query;

/**
 *
 * @author hgs
 */
@Local
public interface LogsFacadeLocal {

    void create(Logs logs);

    void edit(Logs logs);

    void remove(Logs logs);

    Logs find(Object id);

    List<Logs> findAll();

    List<Logs> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    public List<Logs> getLogs4Student(Student student) ;

    public List<Logs> getLogs4Teacher(TeacherAdmin teacher) ;
    
    public List<Integer> getLogsStudentOrTeacher(String sqlString);
}
