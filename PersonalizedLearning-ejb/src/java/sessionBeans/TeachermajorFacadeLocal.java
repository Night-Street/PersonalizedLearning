/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Major;
import entities.TeacherAdmin;
import entities.Teachermajor;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface TeachermajorFacadeLocal {

    void create(Teachermajor teachermajor);

    void edit(Teachermajor teachermajor);

    void remove(Teachermajor teachermajor);

    Teachermajor find(Object id);

    List<Teachermajor> findAll();

    List<Teachermajor> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    public List<Teachermajor> getTeachermajor4Teacher(TeacherAdmin teacher);

    public List<Teachermajor> getTeachermajor4Major(Major major);
}
