/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.News;
import entities.Roleinfo;
import entities.TeacherAdmin;
import java.util.List;
import java.util.Optional;
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
public class NewsFacade extends AbstractFacade<News> implements NewsFacadeLocal {
    
    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public NewsFacade() {
        super(News.class);
    }
    
    @Override
    public List<News> getNews4Roleinfo(Roleinfo roleinfo) {
        Query query = getEntityManager().createNamedQuery("News.findByRole");
        query.setParameter("role", roleinfo);
        return query.getResultList();
    }
    
    @Override
    public List<News> getNews4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("News.findByTeacher");
        query.setParameter("teacher", teacher);
        return query.getResultList();
    }
    
    @Override
    public News findByName(String newsTitle) {
        Query query = getEntityManager().createNamedQuery("News.findByNewstitle");
        query.setParameter("newstitle", newsTitle);
        return Optional.ofNullable((News) query.getResultList().get(0)).orElse(null);
    }
    
}
