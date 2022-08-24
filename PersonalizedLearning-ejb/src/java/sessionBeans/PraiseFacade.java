/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Knowledge;
import entities.Learningresource;
import entities.Praise;
import entities.Question;
import entities.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author haogs
 */
@Stateless
public class PraiseFacade extends AbstractFacade<Praise> implements PraiseFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PraiseFacade() {
        super(Praise.class);
    }

    @Override
    public List<Praise> getPraise4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("Praise.findBystudentId");
        query.setParameter("studentId", student);
        return query.getResultList();
    }

    @Override
    public List<Praise> getPraise4Question(Question question) {
        Query query = getEntityManager().createNamedQuery("Praise.findByquestionId");
        query.setParameter("questionId", question);
        return query.getResultList();
    }

    @Override
    public List<Praise> getPraise4Knowledge(Knowledge knowledge) {
        Query query = getEntityManager().createNamedQuery("Praise.findByKnowledgeId");
        query.setParameter("knowledgeId", knowledge);
        return query.getResultList();
    }

    @Override
    public List<Praise> getPraise4Learningresource(Learningresource learingresource) {
        Query query = getEntityManager().createNamedQuery("Praise.findBylearningresourceId");
        query.setParameter("learningresourceId", learingresource);
        return query.getResultList();
    }

}
