/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Leadpoint;
import entities.Student;
import entities.Subject;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hgs
 */
@Local
public interface LeadpointFacadeLocal {

    void create(Leadpoint leadpoint);

    void edit(Leadpoint leadpoint);

    void remove(Leadpoint leadpoint);

    Leadpoint find(Object id);

    List<Leadpoint> findAll();

    List<Leadpoint> findRange(int[] range);

    int count();

    List getQueryResultList(String sql);

    int executUpdate(String sql);

    List<Leadpoint> getLeadpoint4Subject(Subject subject);

    List<Leadpoint> getLeadpoint4Student(Student student);
    
    Leadpoint findByKnowledgeIds(String knowledgesId);
}
