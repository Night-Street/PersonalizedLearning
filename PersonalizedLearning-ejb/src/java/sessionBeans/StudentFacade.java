package sessionBeans;

import entities.School;
import entities.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hgs
 */
@Stateless
public class StudentFacade extends AbstractFacade<Student> implements StudentFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentFacade() {
        super(Student.class);
    }

    @Override
    public List<Student> getStudent4School(School school) {
        Query query = getEntityManager().createNamedQuery("Student.findByschoolId");
        query.setParameter("schoolId", school);
        return query.getResultList();
    }

    @Override
    public Student findByName(String studentIdInSchool) {
        Query query = getEntityManager().createNamedQuery("Student.findByStudentidinschool");
        query.setParameter("studentidinschool", studentIdInSchool);
        List<Student> resultList = query.getResultList();
        if (resultList.size()>0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }
   
}
