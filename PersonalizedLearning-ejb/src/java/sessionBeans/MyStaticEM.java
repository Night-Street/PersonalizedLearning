/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hgs
 */
@Stateless
public class MyStaticEM {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    public static EntityManager em;
}
