/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.PageColor;
import entities.Student;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author haogs
 */
@Stateless
public class PageColorFacade extends AbstractFacade<PageColor> implements PageColorFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PageColorFacade() {
        super(PageColor.class);
    }

    @Override
    public List<PageColor> getPageColor4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("News.findByRole");
        query.setParameter("userId", student);
        return query.getResultList();
    }

    @Override
    public List<PageColor> getPageColor4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("PageColor.findByuserId");
        query.setParameter("teacherId", teacher);
        return query.getResultList();
    }

}
