package modelController.applicationController;

import java.io.Serializable;

/**
 *
 * @author haogs
 */
public class ApplicationCommonController implements Serializable {
    //数据库中数据是否更新    
    private boolean dataChanged = true;//一开始的时候，要获取数据

    public boolean isDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(boolean dataChanged) {
        if (this.dataChanged != dataChanged) {//读比写要快得多
            this.dataChanged = dataChanged;
        }
    }
}
