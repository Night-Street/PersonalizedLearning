/*
 * 这部分内容存入applicationScopped的内容
 *尤其是对于一些在application中共享的内容，如多个用户可能会同时修改的，这里就需要相应的更新
 */
package tools;

import java.util.Calendar;
import java.util.LinkedHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Idea
 */
@Named
@ApplicationScoped
public class PublicFields implements java.io.Serializable {
    
    private final Calendar c = Calendar.getInstance();
    private final int year = c.get(Calendar.YEAR), currentMonth = c.get(Calendar.MONTH);
    private int month = c.get(Calendar.MONTH);
    private LinkedHashMap<Integer, Integer> yearMap;
    private LinkedHashMap<Integer, Integer> monthMap;
    private final LinkedHashMap<Integer, Integer> dayMap = new LinkedHashMap<>();

    /**
     * @return the yearMap
     */
    public LinkedHashMap<Integer, Integer> getYearMap() {
        if (null == yearMap) {
            yearMap = new LinkedHashMap<>();
            yearMap.put(c.get(Calendar.YEAR), c.get(Calendar.YEAR));
            yearMap.put(c.get(Calendar.YEAR) - 1, c.get(Calendar.YEAR) - 1);
        }
        return yearMap;
    }

    public LinkedHashMap<Integer, Integer> getMonthMap() {
        if (null == monthMap || monthMap.isEmpty()) {
            monthMap = new LinkedHashMap<>();
            for (int i = 0; i < 12; i++) {
                monthMap.put(i + 1, i);
            }
        }
        return monthMap;
    }

    public LinkedHashMap<Integer, Integer> getDayMap() {
        dayMap.clear();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.YEAR, year - c.get(Calendar.YEAR));
        c1.add(Calendar.MONTH, month - currentMonth + 1);
        for (int i = 0; i < c1.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayMap.put(i + 1, i + 1);
        }
        return dayMap;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String filterCharacter(String input) {
        String output = null;
        if (null != input) {
            output = input.replaceAll("<", "&lt;");
            output = output.replaceAll(">", "&gt;");
        }
        return output;
    }

    public String getDateExpression(Date date) {
        StringBuilder sb = new StringBuilder();
        if (null != date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            sb.append(calendar.get(Calendar.YEAR)).append("-")
                    .append(calendar.get(Calendar.MONTH)).append("-")
                    .append(calendar.get(Calendar.DAY_OF_MONTH));
        }
        return sb.toString();
    }

    public String getIcon() {
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = origRequest.getScheme()
                + "://"+ origRequest.getServerName()
                + ":"+ origRequest.getServerPort()
                + "/PersonalizedLearning-war/faces/javax.faces.resource/images/h.ico";
        //System.out.println(url+"ICON===================================");
        return url;
    }

    public String getFileRepository() {
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = origRequest.getScheme()
                + "://"+ origRequest.getServerName()
                + ":"+ origRequest.getServerPort()
                + "/fileRepository/";
        //System.out.println(url+"FileRepository================================");
        return url;
    }
}
