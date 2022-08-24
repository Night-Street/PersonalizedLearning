package modelController.sessionController;

import entities.News;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import modelController.viewerController.MainXhtml;
import tools.StaticFields;

@Named("newsController")
@SessionScoped
public class NewsController extends CommonModelController<News> implements Serializable {

    private List<News> newsList = null;
    private final String tableName = "news", listpage = "", editpage = "", viewpage = "", createpage = "";
    @Inject
    private MainXhtml mainXhtml;
    @Inject
    private modelController.applicationController.NewsController applicationNewsController;
    @Inject
    private modelController.applicationController.StudentController applicationStudentController;
    @Inject
    private tools.UserMessagor userMessagor;
    @Inject
    private modelController.sessionController.CommonSession commonSession;
    protected News current;

    public News getSelected() {
        if (current == null) {
            selectedItemIndex = -1;
            current = new News();
        }
        return current;
    }
    public void setSelected(News news) {
        current = news;
    }

    public void setDataModelList() {
        pageOperation.setDataModelList(applicationNewsController.getNewsList());
    }

    public NewsController() {
    }

    public void create() {
        try {
            if (null == applicationNewsController.findByName(current.getNewstitle())) {
                applicationNewsController.create(current);
                //teacherAdminController.getLoginTeacherAdmin().getNewsSet().add(current);
                userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
                //2. Refresh the data
                //current = (News) (getFacade().getQueryResultList("select * from news where  Newstitle='" + current.getNewstitle().trim() + "'").get(0));
                // evictForeignKey();
                // getNewsList().add(current);
                pageOperation.refreshData(applicationNewsController.getNewsList());
                this.logs(current.getNewstitle(), tableName, StaticFields.OPERATIONINSERT);
                prepareCreate();
            } else {
                userMessagor.addMessage(current.getNewstitle() + ":" + commonSession.getResourceBound().getString("Already") + " " + commonSession.getResourceBound().getString("Exist"));
            }
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "controll news 1");
        }
    }

    public void prepareCreate() {
        setSelected( new News());
        selectedItemIndex = -1;
        mainXhtml.setPageName(this.createpage);
    }

    public void prepareEdit() {
        setSelected( (News) getItems().getRowData());
        selectedItemIndex = getPageOperation().getPagination().getPageFirstItem() + getItems().getRowIndex();
        mainXhtml.setPageName(editpage);
    }

    public String update() {
        try {
            applicationNewsController.edit(current);
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            this.logs(current.getNewstitle(), tableName, StaticFields.OPERATIONUPDATE);
            return null;
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "controll news 2");
            return null;
        }
    }

    public void destroy() {
        current = (News) getItems().getRowData();
        selectedItemIndex = pageOperation.getPagination().getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        setSelected(null);
        mainXhtml.setPageName(this.listpage);
    }

    private void performDestroy() {
        try {
            applicationNewsController.remove(current);
            //evictForeignKey();
            userMessagor.addMessage(commonSession.getResourceBound().getString("Succeed"));
            updateCurrentItem();
            pageOperation.refreshData(applicationNewsController.getNewsList());
            this.logs(current.getNewstitle(), tableName, StaticFields.OPERATIONDELETE);
        } catch (Exception e) {
            userMessagor.addMessage(commonSession.getResourceBound().getString("Failed") + "controll news 3");
        }
    }

    private void updateCurrentItem() {
        int count = applicationNewsController.count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (getPageOperation().getPagination().getPageFirstItem() >= count) {
                getPageOperation().getPagination().previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            setSelected( (News) applicationNewsController.findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0));
        }
    }

}
