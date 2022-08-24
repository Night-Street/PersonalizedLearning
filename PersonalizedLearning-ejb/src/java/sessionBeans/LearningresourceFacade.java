/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Knowledge;
import entities.Learningresource;
import entities.Student;
import entities.Subject;
import entities.TeacherAdmin;
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
public class LearningresourceFacade extends AbstractFacade<Learningresource> implements LearningresourceFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LearningresourceFacade() {
        super(Learningresource.class);
    }

    @Override
    public List<Learningresource> getLearningResources4Subject(Knowledge knowledge) {
        Query query = getEntityManager().createNamedQuery("Learningresource.findByKnowledgeId");
        query.setParameter("knowledgeId", knowledge);
        return query.getResultList();
    }

    @Override
    public List<Learningresource> getLearningResources4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("Learningresource.findByStudentId");
        query.setParameter("studentId", student);
        return query.getResultList();
    }

    @Override
    public List<Learningresource> getLearningResources4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("Learningresource.findByTeacherId");
        query.setParameter("teacherId", teacher);
        return query.getResultList();
    }

    @Override
    public Learningresource findByName(String name) {
        Query query = getEntityManager().createNamedQuery("Learningresource.findByValueinfo");
        query.setParameter("valueinfo", name);
        List<Learningresource> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

}
