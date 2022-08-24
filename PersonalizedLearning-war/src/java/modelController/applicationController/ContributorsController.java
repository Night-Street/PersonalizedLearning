package modelController.applicationController;

import entities.Contributors;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import sessionBeans.ContributorsFacadeLocal;

@Named("contributorsControllerA")
@ApplicationScoped
public class ContributorsController extends ApplicationCommonController  {

    @EJB
    private ContributorsFacadeLocal cfl;

    public ContributorsFacadeLocal getFacade() {
        return cfl;
    }

    public void create(Contributors contributors) {
        getFacade().create(contributors);
        setDataChanged(true);
    }

    public void edit(Contributors e) {
        getFacade().edit(e);
        setDataChanged(true);
    }

    public void remove(Contributors e) {
        getFacade().remove(e);
        setDataChanged(true);
    }

    public List<Contributors> getQueryResultList(String sql) {
        return getFacade().getQueryResultList(sql);
    }

    public int count() {
        setDataChanged(false);
        return getFacade().count();
    }

    public void executUpdate(String updateString) {
        getFacade().executUpdate(updateString);
        setDataChanged(true);
    }

    public List<Contributors> findRange(int[] range) {
        setDataChanged(false);
        return getFacade().findRange(range);
    }

    public Contributors find(Integer id) {
        return getFacade().find(id);
    }

    public Set getAllSet() {
        return new HashSet(getFacade().findAll());
    }

    public List<Contributors> getAllList() {
        setDataChanged(false);
        return getFacade().findAll();
    }

    public List<Integer> getQueryIntList(String sqlString) {
        setDataChanged(false);
        return getFacade().getQueryIntList(sqlString);
    }
    public Contributors findByName(String name){
        setDataChanged(false);
        return getFacade().findByName(name);
    }
}
