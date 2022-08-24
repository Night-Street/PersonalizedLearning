/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Major;
import entities.Majorsubject;
import entities.Subject;
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
public class MajorsubjectFacade extends AbstractFacade<Majorsubject> implements MajorsubjectFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MajorsubjectFacade() {
        super(Majorsubject.class);
    }
      
 
    @Override
    public List<Majorsubject> getMajorsubject4Major(Major major){
        Query query = getEntityManager().createNamedQuery("Majorsubject.findByMajorid");
        query.setParameter("majorid", major);
        return query.getResultList();
    }

    @Override
    public List<Majorsubject> getMajorsubject4Subject(Subject subject){
        Query query = getEntityManager().createNamedQuery("Majorsubject.findBySubjectid");
        query.setParameter("subjectid", subject);
        return query.getResultList();
    }
}
