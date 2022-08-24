/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

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
public class TeacherAdminFacade extends AbstractFacade<TeacherAdmin> implements TeacherAdminFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeacherAdminFacade() {
        super(TeacherAdmin.class);
    }

    @Override
    public TeacherAdmin findByName(String name) {
        Query query = getEntityManager().createNamedQuery("TeacherAdmin.findByName");
        query.setParameter("name", name);
        List<TeacherAdmin> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

}
