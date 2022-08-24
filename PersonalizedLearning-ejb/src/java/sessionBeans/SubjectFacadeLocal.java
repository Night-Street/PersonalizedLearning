/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Subject;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface SubjectFacadeLocal {

    void create(Subject subject);

    void edit(Subject subject);

    void remove(Subject subject);

    Subject find(Object id);

    List<Subject> findAll();

    List<Subject> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    Subject findByName(String name);
}
