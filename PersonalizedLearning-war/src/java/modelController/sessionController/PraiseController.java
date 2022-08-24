package modelController.sessionController;

import entities.Knowledge;
import entities.Learningresource;
import entities.Praise;
import entities.Question;
import entities.Student;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import tools.pagination.JsfUtil;
import tools.pagination.PaginationHelper;

@Named("praiseController")
@SessionScoped
public class PraiseController extends CommonModelController<Praise> implements Serializable {

    private DataModel items = null;
    private PaginationHelper pagination;

    @Inject
    private modelController.applicationController.PraiseController applicationPraiseController;
    @Inject
    private modelController.applicationController.KnowledgeController applicationKnowledgeController;
    @Inject
    private modelController.applicationController.LearningresourceController applicationLearningresourceController;
    @Inject
    private modelController.applicationController.QuestionController applicationQuestionController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    modelController.sessionController.CommonSession commonSession;

    public PraiseController() {
    }
    protected Praise current;

    public Praise getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new Praise();
        }
        return current;
    }
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        setSelected( (Praise) getItems().getRowData());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Praise();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            applicationPraiseController.getFacade().create(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("PraiseCreated"));
            return prepareCreate();
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        setSelected( (Praise) getItems().getRowData());
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            applicationPraiseController.getFacade().edit(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("PraiseUpdated"));
            return "View";
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Praise) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        setSelected(null);
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            applicationPraiseController.getFacade().remove(current);
            userMessagor.addMessage(commonSession.getResourceBound().getString("PraiseDeleted"));
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = applicationPraiseController.getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( applicationPraiseController.getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(applicationPraiseController.getFacade().findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(applicationPraiseController.getFacade().findAll(), true);
    }

    public Praise getPraise(java.lang.Integer id) {
        return applicationPraiseController.getFacade().find(id);
    }

    String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSelected(Praise praise) {
        current = praise;
    }

    //学生点赞  false:吐槽  true:点赞
    public String praise(Object obj, boolean flag) {
        //当前学生
        if (studentController.getPraiseTotal() < 10) {
            msg = "";
            if (obj != null) {
                int praise = 0;
                if (flag) {
                    praise = 1;
                } else {
                    praise = -1;
                }
                Student stu = studentController.getLogined();
                Knowledge k = null;
                Learningresource l = null;
                Question q = null;
                //判断资源类型并写入新的赞个数
                if (obj instanceof Knowledge) {
                    k = (Knowledge) obj;
                    k.setPraiseCnt(Optional.ofNullable(k.getPraiseCnt()).orElse(0) + praise);
                    applicationKnowledgeController.edit(k);
                } else if (obj instanceof Learningresource) {
                    l = (Learningresource) obj;
                    l.setPraiseCnt(l.getPraiseCnt() + praise);
                    applicationLearningresourceController.edit(l);
                } else if (obj instanceof Question) {
                    q = (Question) obj;
                    q.setPraiseCnt(q.getPraiseCnt() + praise);
                    applicationQuestionController.edit(q);
                } else {

                }
                Praise p = new Praise();
                if (k != null) {
                    p.setKnowledgeId(k);
                }
                if (l != null) {
                    p.setLearningresourceId(l);
                }
                if (q != null) {
                    p.setQuestionId(q);
                }
                p.setPraiseDate(new Date());
                p.setStudentId(stu);
                applicationPraiseController.create(p);
                //数据写入数据库后使得当前学生点赞数加一
                studentController.setPraiseTotal(studentController.getPraiseTotal() + 1);
            }
        } else {
            setMsg("今日点赞达到最大数值，请明日在点赞！");
        }
        return null;
    }

}
