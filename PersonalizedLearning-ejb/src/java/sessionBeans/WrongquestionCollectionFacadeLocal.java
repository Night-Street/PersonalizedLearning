/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Student;
import entities.WrongquestionCollection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface WrongquestionCollectionFacadeLocal {

    void create(WrongquestionCollection wrongquestionCollection);

    void edit(WrongquestionCollection wrongquestionCollection);

    void remove(WrongquestionCollection wrongquestionCollection);

    WrongquestionCollection find(Object id);

    List<WrongquestionCollection> findAll();

    List<WrongquestionCollection> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

     public List<WrongquestionCollection> getWrongquestionCollection4Student(Student student);
}
