/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Scheduleclass;
import entities.TeacherAdmin;
import entities.Teacherschedule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface TeacherscheduleFacadeLocal {

    void create(Teacherschedule teacherschedule);

    void edit(Teacherschedule teacherschedule);

    void remove(Teacherschedule teacherschedule);

    Teacherschedule find(Object id);

    List<Teacherschedule> findAll();

    List<Teacherschedule> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    public List<Teacherschedule> getTeacherschedule4Teacher(TeacherAdmin teacher);

    public List<Teacherschedule> getTeacherschedule4Scheduleclass(Scheduleclass scheduleclass);
}
