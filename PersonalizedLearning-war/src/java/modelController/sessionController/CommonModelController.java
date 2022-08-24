package modelController.sessionController;

import javax.faces.model.DataModel;
import javax.inject.Inject;
import tools.PageOperation;
import tools.pagination.PaginationHelper;

/**
 *
 * @author hgs
 * @param <T>
 */
public abstract class CommonModelController<T>{

    protected int selectedItemIndex;
    protected final PageOperation pageOperation = new PageOperation();
    @Inject
    protected TeacherAdminController teacherAdminController;
    @Inject
    protected modelController.applicationController.LogsController applicationLogsController;
    @Inject
    protected modelController.sessionController.StudentController studentController;
    @Inject
    protected modelController.sessionController.ParentController parentController;

    public PaginationHelper getPagination() {
        return pageOperation.getPagination();
    }

    public PageOperation getPageOperation() {
        return this.pageOperation;
    }

    public DataModel getItems() {
        return pageOperation.getItems();
    }

    public void setSelectedItemIndex(int index) {
        selectedItemIndex = index;
    }

    protected void logs(String recorderinfo, String tableName, String operation) {
        if (null != teacherAdminController.getLogined()) {
            applicationLogsController.persist(teacherAdminController.getLogined(),
                    null, recorderinfo, tableName, operation);
        } else if (null != teacherAdminController.getLogined()) {
            applicationLogsController.persist(null,
                    studentController.getLogined(), recorderinfo, tableName, operation);
        }
    }
}
