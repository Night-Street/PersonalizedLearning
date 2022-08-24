/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Registeruser;
import java.util.List;

/**
 *
 * @author Administrator
 */
@javax.ejb.Local
public interface RegisteruserFacadeLocal {

    void create(Registeruser registeruser);

    void edit(Registeruser registeruser);

    void remove(Registeruser registeruser);

    Registeruser find(Object id);

    List<Registeruser> findAll();

    List<Registeruser> findRange(int[] range);

    int count();
    
    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    Registeruser findByName(String name);
}
