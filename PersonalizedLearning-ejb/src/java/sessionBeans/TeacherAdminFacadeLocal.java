/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface TeacherAdminFacadeLocal {

    void create(TeacherAdmin teacherAdmin);

    void edit(TeacherAdmin teacherAdmin);

    void remove(TeacherAdmin teacherAdmin);

    TeacherAdmin find(Object id);

    List<TeacherAdmin> findAll();

    List<TeacherAdmin> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    TeacherAdmin findByName(String name);
}
