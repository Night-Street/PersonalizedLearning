/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Contributors;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hadoop
 */
@Local
public interface ContributorsFacadeLocal{

    void create(Contributors contributors);

    void edit(Contributors contributors);

    void remove(Contributors contributors);

    Contributors find(Object id);

    List<Contributors> findAll();

    List<Contributors> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    List<Integer> getQueryIntList (String sql);
    
    Contributors findByName(String name);
}
