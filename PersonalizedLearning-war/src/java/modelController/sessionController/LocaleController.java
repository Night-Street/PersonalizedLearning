package modelController.sessionController;

import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author 郝国生
 */
@Named
@SessionScoped
public class LocaleController implements java.io.Serializable {

    @Inject
    CommonSession commonSession;
    private final String[] languageString = {"English", "中文"};
    private Locale locale;
    private int languageIndex = 0;

    public String getShownLanguage() {
        //在页面上显示要切换的目标语言，如当前页面是中文，则可以切换到目标语言
        if (getLocale()==Locale.ENGLISH) {
            languageIndex = 1;
        } else if (getLocale()==Locale.CHINESE) {
            languageIndex = 0;
        }
        return languageString[languageIndex];
    }

    public Locale getLocale() {
        if (null == locale) {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String switchLocale() {
        //切换到目标locale，如果当前是英文，则切换到中文
        locale = languageIndex == 0 ? Locale.ENGLISH: Locale.CHINESE ;
        commonSession.getResourceBound();//重置语言
        return null;
    }
}
