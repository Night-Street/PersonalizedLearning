/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Knowledge;
import entities.Question;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface QuestionFacadeLocal {

    void create(Question question);

    void edit(Question question);

    void remove(Question question);

    Question find(Object id);

    List<Question> findAll();

    List<Question> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    List getGeneralQueryList(String string);

    List<Question> getQuestion4Knowledge(Knowledge knowledge);
    
    Question findByName(String name);
}
