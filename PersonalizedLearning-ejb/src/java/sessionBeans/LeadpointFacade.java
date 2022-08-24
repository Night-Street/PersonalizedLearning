/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Leadpoint;
import entities.Student;
import entities.Subject;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hgs
 */
@Stateless
public class LeadpointFacade extends AbstractFacade<Leadpoint> implements LeadpointFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LeadpointFacade() {
        super(Leadpoint.class);
    }

    @Override
    public List<Leadpoint> getLeadpoint4Subject(Subject subject) {
        Query query = getEntityManager().createNamedQuery("Leadpoint.findBySubject");
        query.setParameter("subjectid", subject);
        return query.getResultList();
    }

    @Override
    public List<Leadpoint> getLeadpoint4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("Leadpoint.findByStudent");
        query.setParameter("studentId", student);
        return query.getResultList();
    }

    @Override
    public Leadpoint findByKnowledgeIds(String knowledgesId) {
        Query query = getEntityManager().createNamedQuery("Leadpoint.findByKnowledgeId");
        query.setParameter("knowledgeId", knowledgesId);
        List<Leadpoint> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }
}
