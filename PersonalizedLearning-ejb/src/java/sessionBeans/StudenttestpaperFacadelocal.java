/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Leadpoint;
import entities.Student;
import entities.Studenttestpaper;
import entities.Testpaper;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface StudenttestpaperFacadelocal {

    void create(Studenttestpaper subject);

    void edit(Studenttestpaper subject);

    void remove(Studenttestpaper subject);

    Studenttestpaper find(Object id);

    List<Studenttestpaper> findAll();

    List<Studenttestpaper> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    public List<Studenttestpaper> getStudenttestpaper4Leadpoint(Leadpoint leadpoint);

    public List<Studenttestpaper> getStudenttestpaper4Testpaper(Testpaper testpaper);

    public List<Studenttestpaper> getStudenttestpaper4Student(Student student);
}
