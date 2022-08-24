package modelController.sessionController;

import entities.Predicate;
import entities.Student;
import entities.TeacherAdmin;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;
import tools.pagination.JsfUtil;

@Named("predicateController")
@SessionScoped
public class PredicateController extends CommonModelController<Predicate> implements Serializable {

    @Inject
    private modelController.applicationController.PredicateController applicationPredicateController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.RoleinfoController applicationRoleinfoController;
    private final String tableName = "predicate", listpage = "predicate/List", editpage = "predicate/Edit",
            viewpage = "predicate/View", createpage = "predicate/Create";
    private String searchName;
    private final List<Predicate> predicateList = new LinkedList<>();

    public void setSelected(Predicate predicate) {
        current = predicate;
    }
    protected Predicate current;

    public Predicate getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Predicate();
        }
        return current;
    }
    public void setDataModelList() {
        pageOperation.setDataModelList(applicationPredicateController.getAllList());
    }

    public void create() {
        try {
            if (null == applicationPredicateController.findByName(current.getPname())) {
                applicationPredicateController.create(current);
                pageOperation.refreshData(applicationPredicateController.getAllList());
                this.logs(current.getPname(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getPname() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 1");
        }
    }

    public void prepareList() {
        pageOperation.refreshData(applicationPredicateController.getAllList());
        mainXhtml.setPageName(listpage);
    }

    public void prepareCreate() {
        setSelected( new Predicate());
        selectedItemIndex = -1;
        mainXhtml.setPageName(createpage);
    }

    public void prepareView() {
        setSelected( (Predicate) (getItems().getRowData()));
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(viewpage);
    }

    public void prepareEdit() {
        try {
            setSelected( (Predicate) getItems().getRowData());
            selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        } catch (Exception e) {

        }
        mainXhtml.setPageName(editpage);
    }

    public String update() {
        try {
            applicationPredicateController.edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getPname(), tableName, StaticFields.OPERATIONUPDATE);
            return null;
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 2");
            return null;
        }
    }

    public void destroy() {
        current = (Predicate) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(listpage);
    }

    private void performDestroy() {
        try {
            applicationPredicateController.remove(current);
            updateCurrentItem();
            pageOperation.refreshData(applicationPredicateController.getAllList());
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getPname(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + " Controller teacher 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationPredicateController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (Predicate) applicationPredicateController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationPredicateController.getAllList(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationPredicateController.getAllList(), true);
    }

    public List<Predicate> getPredicateList() {
        String conditonString = "";
        if (null != searchName && searchName.trim().length() > 0) {
            conditonString = "where  locate ('" + searchName.toLowerCase() + "',LOWER(pname))>0 or  locate ('" + searchName.toLowerCase() + "',LOWER(meaning))>0";
        }
        if (predicateList.isEmpty()) {
            List<Predicate> tem = applicationPredicateController.getQueryResultList("select * from Predicate " + conditonString + " order by pname");
            tem.forEach(e -> {
                predicateList.add((Predicate) e);
            });
        }
        return predicateList;
    }

    //-------------------------------For the search and viewall command button------------------
    public void search() {
        if (null != searchName && searchName.trim().length() > 0) {
            pageOperation.refreshData(getPredicateList());
        }
        mainXhtml.setPageName(this.listpage);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public boolean editable(Predicate predicate) {
        boolean result = false;
        if (null != predicate) {
            Student student = Optional.ofNullable(studentController.getLogined()).orElse(null);
            if (null != student) {//是学生
                result = false;
            }
            TeacherAdmin teacher = Optional.ofNullable(teacherAdminController.getLogined()).orElse(null);
            if (null != teacher) {
                result = true;
            }
        }
        return result;
    }

    public boolean isDeletable(Predicate predicate) {
        boolean result1 = false;
        if (null != predicate) {
            Student student = Optional.ofNullable(studentController.getLogined()).orElse(null);
            if (null != student) {
                result1 = false;
            }
            TeacherAdmin teacher = Optional.ofNullable(teacherAdminController.getLogined()).orElse(null);
            if (null != teacher) {
                if (applicationRoleinfoController.isAdminOrSecretary(teacher)) {
                    result1 = true;
                }
            }
        }
        return result1;
    }
}
