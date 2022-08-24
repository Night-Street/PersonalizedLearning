/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Knowledge;
import entities.Student;
import entities.Subject;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface KnowledgeFacadeLocal {

    void create(Knowledge knowledge);

    void edit(Knowledge knowledge);

    void remove(Knowledge knowledge);

    Knowledge find(Object id);

    List<Knowledge> findAll();

    List<Knowledge> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);
    
    List getQueryResultIDsList(String sql);

    int executUpdate(String sql);

    List<Knowledge> getKnowledge4Subject(Subject subject);
     
    List<Knowledge> findByName(String name);
}
