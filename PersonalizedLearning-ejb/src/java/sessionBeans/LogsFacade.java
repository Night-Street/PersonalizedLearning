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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hgs
 */
@Stateless
public class LogsFacade extends AbstractFacade<Logs> implements LogsFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogsFacade() {
        super(Logs.class);
    }
    @Override
     public List<Logs> getLogs4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("Logs.findBystudentid");
        query.setParameter("studentid", student);
        return query.getResultList();
    }

    @Override
    public List<Logs> getLogs4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("Logs.findByTeacherid");
        query.setParameter("teacherid", teacher);
        return query.getResultList();
    }
    
    public List<Integer> getLogsStudentOrTeacher(String sqlString){
        return getEntityManager().createNativeQuery(sqlString, Integer.class).getResultList();
    }
}
