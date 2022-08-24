package sessionBeans;

import entities.Knowledge;
import entities.Learningresource;
import entities.Praise;
import entities.Question;
import entities.Student;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author haogs
 */
@Local
public interface PraiseFacadeLocal {

    void create(Praise praise);

    void edit(Praise praise);

    void remove(Praise praise);

    Praise find(Object id);

    List<Praise> findAll();

    List<Praise> findRange(int[] range);

    int count();
    public  List getQueryResultList(String sql);

    int executUpdate(String sql);
    
    public List<Praise> getPraise4Student(Student student);

    public List<Praise> getPraise4Learningresource(Learningresource learingresource);
    
    public List<Praise> getPraise4Question(Question question);
    
    public List<Praise> getPraise4Knowledge(Knowledge knowledge);
}
