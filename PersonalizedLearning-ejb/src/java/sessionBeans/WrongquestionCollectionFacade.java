/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Student;
import entities.WrongquestionCollection;
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
public class WrongquestionCollectionFacade extends AbstractFacade<WrongquestionCollection> implements WrongquestionCollectionFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WrongquestionCollectionFacade() {
        super(WrongquestionCollection.class);
    }

    @Override
    public List<WrongquestionCollection> getWrongquestionCollection4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("WrongquestionCollection.findBystudentId");
        query.setParameter("studentId", student);
        return query.getResultList(); }
}
