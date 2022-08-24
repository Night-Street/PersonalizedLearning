/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Scheduleclass;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface ScheduleclassFacadeLocal {

    void create(Scheduleclass scheduleclass);

    void edit(Scheduleclass scheduleclass);

    void remove(Scheduleclass scheduleclass);

    Scheduleclass find(Object id);

    List<Scheduleclass> findAll();

    List<Scheduleclass> findRange(int[] range);

    int count();
    List getQueryResultList(String sql) ; 
int executUpdate(String sql) ;
//public void evict(Class entityClass, Object primarykey, Scheduleclass scheduleclass);
}
