/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Student;
import entities.Studentschedule;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hgs
 */
@Stateless
public class StudentscheduleFacade extends AbstractFacade<Studentschedule> implements StudentscheduleFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentscheduleFacade() {
        super(Studentschedule.class);
    }

    @Override
    public List<Studentschedule> getStudentschedule4Student(Student Student) {
        Query query = getEntityManager().createNamedQuery("Studentschedule.findBystudent");
        query.setParameter("student", Student);
        return query.getResultList();
    }
}
