/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Qwquestionwrong;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author haogs
 */
@Local
public interface QwquestionwrongFacadeLocal {

    void create(Qwquestionwrong qwquestionwrong);

    void edit(Qwquestionwrong qwquestionwrong);

    void remove(Qwquestionwrong qwquestionwrong);

    Qwquestionwrong find(Object id);

    List<Qwquestionwrong> findAll();

    List<Qwquestionwrong> findRange(int[] range);

    int count();
    List getQueryResultList(String sql);
}
