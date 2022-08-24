package sessionBeans;

import entities.Student;
import entities.Studentaccupoints;
import entities.TeacherAdmin;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author haogs
 */
@Stateless
public class StudentaccupointsFacade extends AbstractFacade<Studentaccupoints> implements StudentaccupointsFacadeLocal {

    @PersistenceContext(unitName = "PersonalizedLearning-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentaccupointsFacade() {
        super(Studentaccupoints.class);
    }

    @Override
    public List<Studentaccupoints> getStudentaccupoints4Student(Student student) {
        Query query = getEntityManager().createNamedQuery("Studentaccupoints.findBystudentId");
        query.setParameter("studentId", student);
        return query.getResultList();
    }

    @Override
    public List<Studentaccupoints> getStudentaccupoints4Teacher(TeacherAdmin teacher) {
        Query query = getEntityManager().createNamedQuery("Studentaccupoints.findByteacherId");
        query.setParameter("teacherId", teacher);
        return query.getResultList();
    }
}
