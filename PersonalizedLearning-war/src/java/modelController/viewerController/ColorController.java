package modelController.viewerController;

import entities.HslColor;
import entities.PageColor;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelController.sessionController.CommonModelController;
import modelController.sessionController.StudentController;
import sessionBeans.HslColorFacadeLocal;
import sessionBeans.PageColorFacadeLocal;

import tools.StaticFields;

/**
 *
 * @author duanx
 */
@Named("colorControllerView")
@ViewScoped
public class ColorController extends CommonModelController implements Serializable{

    @EJB
    private PageColorFacadeLocal pageColorFacade;
    @EJB
    private HslColorFacadeLocal hslColorFacade;
    private String fontColor;
    private String titleColor;
    private String backgroundColor;
    private String asideGroundColor;
    private String contentGroundColor;
    private int pageColorRate;
    private int fontColorRate;
    private int titleColorRate;
    private int backgroundColorRate;
    private int asideGroundColorRate;
    private int contentGroundColorRate;

    public String getContentGroundColor() {
        return contentGroundColor;
    }

    public void setContentGroundColor(String contentGroundColor) {
        this.contentGroundColor = contentGroundColor;
    }

    public int getContentGroundColorRate() {
        return contentGroundColorRate;
    }

    public void setContentGroundColorRate(int contentGroundColorRate) {
        this.contentGroundColorRate = contentGroundColorRate;
    }

    public StudentController getStudentController() {
        return studentController;
    }

    public void setStudentController(StudentController studentController) {
        this.studentController = studentController;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getAsideGroundColor() {
        return asideGroundColor;
    }

    public void setAsideGroundColor(String asideGroundColor) {
        this.asideGroundColor = asideGroundColor;
    }

    public int getPageColorRate() {
        return pageColorRate;
    }

    public void setPageColorRate(int pageColorRate) {
        this.pageColorRate = pageColorRate;
    }

    public int getFontColorRate() {
        return fontColorRate;
    }

    public void setFontColorRate(int fontColorRate) {
        this.fontColorRate = fontColorRate;
    }

    public int getTitleColorRate() {
        return titleColorRate;
    }

    public void setTitleColorRate(int titleColorRate) {
        this.titleColorRate = titleColorRate;
    }

    public int getBackgroundColorRate() {
        return backgroundColorRate;
    }

    public void setBackgroundColorRate(int backgroundColorRate) {
        this.backgroundColorRate = backgroundColorRate;
    }

    public int getAsideGroundColorRate() {
        return asideGroundColorRate;
    }

    public void setAsideGroundColorRate(int asideGroundColorRate) {
        this.asideGroundColorRate = asideGroundColorRate;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void saveColor() {
        // 存储page_color
        PageColor pageColor = new PageColor();
        if (studentController.getLogined() != null) {
            pageColor.setUserId(studentController.getLogined());
        } else {
            pageColor.setTeacherId(teacherAdminController.getLogined());
        }
        pageColor.setRate(5);
        pageColorFacade.create(pageColor);
        // 存储font_color
        HslColor aColor = new HslColor();
        int[] fontColors = this.parseColor(this.fontColor);
        aColor.setHue(fontColors[0]);
        aColor.setSaturation(fontColors[1]);
        aColor.setLightness(fontColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(5);
        aColor.setType(StaticFields.FONT_COLOR);
        hslColorFacade.create(aColor);
        // 存储title_color
        int[] titleColors = this.parseColor(this.titleColor);
        aColor.setHue(titleColors[0]);
        aColor.setSaturation(titleColors[1]);
        aColor.setLightness(titleColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(5);
        aColor.setType(StaticFields.TITLE_COLOR);
        hslColorFacade.create(aColor);
        // 存储background_color
        int[] backgroundColors = this.parseColor(this.backgroundColor);
        aColor.setHue(backgroundColors[0]);
        aColor.setSaturation(backgroundColors[1]);
        aColor.setLightness(backgroundColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(5);
        aColor.setType(StaticFields.BACKGROUND_COLOR);
        hslColorFacade.create(aColor);
        // 存储asdie_background_color
        int[] asideGroundColors = this.parseColor(this.asideGroundColor);
        aColor.setHue(asideGroundColors[0]);
        aColor.setSaturation(asideGroundColors[1]);
        aColor.setLightness(asideGroundColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(5);
        aColor.setType(StaticFields.ASIDE_BACKROUND_COLOR);
        hslColorFacade.create(aColor);
        // 存储content_background_color
        int[] contentGroundColors = this.parseColor(this.contentGroundColor);
        aColor.setHue(contentGroundColors[0]);
        aColor.setSaturation(contentGroundColors[1]);
        aColor.setLightness(contentGroundColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(5);
        aColor.setType(StaticFields.CONTENT_BACKGROUND_COLOR);
        hslColorFacade.create(aColor);
    }

    public void gaSaveColor() {
        // 存储page_color
        PageColor pageColor = new PageColor();
        if (studentController.getLogined() != null) {
            pageColor.setUserId(studentController.getLogined());
        } else {
            pageColor.setTeacherId(teacherAdminController.getLogined());
        }
        pageColor.setRate(this.pageColorRate);
        pageColorFacade.create(pageColor);
        // 存储font_color
        HslColor aColor = new HslColor();
        int[] fontColors = this.parseColor(this.fontColor);
        aColor.setHue(fontColors[0]);
        aColor.setSaturation(fontColors[1]);
        aColor.setLightness(fontColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(this.fontColorRate);
        aColor.setType(StaticFields.FONT_COLOR);
        hslColorFacade.create(aColor);
        // 存储title_color
        int[] titleColors = this.parseColor(this.titleColor);
        aColor.setHue(titleColors[0]);
        aColor.setSaturation(titleColors[1]);
        aColor.setLightness(titleColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(this.titleColorRate);
        aColor.setType(StaticFields.TITLE_COLOR);
        hslColorFacade.create(aColor);
        // 存储background_color
        int[] backgroundColors = this.parseColor(this.backgroundColor);
        aColor.setHue(backgroundColors[0]);
        aColor.setSaturation(backgroundColors[1]);
        aColor.setLightness(backgroundColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(this.backgroundColorRate);
        aColor.setType(StaticFields.BACKGROUND_COLOR);
        hslColorFacade.create(aColor);
        // 存储asdie_background_color
        int[] asideGroundColors = this.parseColor(this.asideGroundColor);
        aColor.setHue(asideGroundColors[0]);
        aColor.setSaturation(asideGroundColors[1]);
        aColor.setLightness(asideGroundColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(this.asideGroundColorRate);
        aColor.setType(StaticFields.ASIDE_BACKROUND_COLOR);
        hslColorFacade.create(aColor);
        // 存储content_background_color
        int[] contentGroundColors = this.parseColor(this.contentGroundColor);
        aColor.setHue(contentGroundColors[0]);
        aColor.setSaturation(contentGroundColors[1]);
        aColor.setLightness(contentGroundColors[2]);
        aColor.setPageId(pageColor.getId());
        aColor.setRate(this.contentGroundColorRate);
        aColor.setType(StaticFields.CONTENT_BACKGROUND_COLOR);
        hslColorFacade.create(aColor);
    }

    public int[] parseColor(String color) {
        int[] colors = new int[3];
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(color);
        m.find();
        colors[0] = Integer.parseInt(m.group());
        m.find();
        colors[1] = Integer.parseInt(m.group());
        m.find();
        colors[2] = Integer.parseInt(m.group());
        return colors;
    }

    public String getPageColor() {//推荐排名前5的颜色搭配
        List<HslColor> fontColors = hslColorFacade.getQueryResultList(
                "select * from hsl_color where type = 1 and  page_id in (select id from page_color order by rate desc fetch first 5 rows only)");
        List<HslColor> titleColors = hslColorFacade.getQueryResultList(
                "select * from hsl_color where type = 2 and page_id in (select id from page_color order by rate desc fetch first 5 rows only)");
        List<HslColor> backgroundColors = hslColorFacade.getQueryResultList(
                "select * from hsl_color where type = 3 and page_id in (select id from page_color order by rate desc fetch first 5 rows only)");
        List<HslColor> asideBackgroundColors = hslColorFacade.getQueryResultList(
                "select * from hsl_color where type = 4 and page_id in (select id from page_color order by rate desc fetch first 5 rows only)");
        List<HslColor> contentBackgroundColors = hslColorFacade.getQueryResultList(
                "select * from hsl_color where type = 5 and page_id in (select id from page_color order by rate desc fetch first 5 rows only)");
        String result = "";
        String font_color = "\"font_color\":[";
        String title_color = "\"title_color\":[";
        String background_color = "\"background_color\":[";
        String aside_background_color = "\"aside_background_color\":[";
        String content_background_color = "\"aside_background_color\":[";
        for (int i = 0; i < fontColors.size(); i++) {
            if (i != fontColors.size() - 1) {
                font_color = font_color + fontColors.get(i).getValue() + ",";
                title_color = title_color + titleColors.get(i).getValue() + ",";
                background_color = background_color + backgroundColors.get(i).getValue() + ",";
                aside_background_color = aside_background_color + asideBackgroundColors.get(i).getValue() + ",";
                content_background_color = content_background_color + contentBackgroundColors.get(i).getValue() + ",";
            } else {
                font_color = font_color + fontColors.get(i).getValue();
                title_color = title_color + titleColors.get(i).getValue();
                background_color = background_color + backgroundColors.get(i).getValue();
                aside_background_color = aside_background_color + asideBackgroundColors.get(i).getValue();
                content_background_color = content_background_color + contentBackgroundColors.get(i).getValue();
            }
        }
        font_color = font_color + "]";
        title_color = title_color + "]";
        background_color = background_color + "]";
        aside_background_color = aside_background_color + "]";
        content_background_color = content_background_color + "]";
        return "{" + font_color + "," + title_color + "," + background_color + "," + aside_background_color + "," + content_background_color + "}";
    }

    public String getBestColor() {
        int studentId = 0, teacherId = 0;
        List<PageColor> pageColor=new LinkedList<>();
        if (studentController.getLogined() != null) {
            studentId = studentController.getLogined().getId();
            pageColor = pageColorFacade.getQueryResultList("select id from page_color where user_id = " + studentId
                    + " order by rate desc fetch first 1 rows only");
        } else if(teacherAdminController.getLogined()!=null){
            teacherId = teacherAdminController.getLogined().getId();
            pageColor = pageColorFacade.getQueryResultList("select id from page_color where teacher_id = " + teacherId
                    + " order by rate desc fetch first 1 rows only");
        }

        if (!pageColor.isEmpty()) {
            int pageId = pageColor.get(0).getId();
            List<HslColor> fcs = hslColorFacade
                    .getQueryResultList("select * from hsl_color where type = 1 and page_id =" + pageId);
            HslColor fc = fcs.get(0);
            List<HslColor> tcs = hslColorFacade
                    .getQueryResultList("select * from hsl_color where type = 2 and page_id =" + pageId);
            HslColor tc = tcs.get(0);
            List<HslColor> bcs = hslColorFacade
                    .getQueryResultList("select * from hsl_color where type = 3 and page_id =" + pageId);
            HslColor bc = bcs.get(0);
            List<HslColor> acs = hslColorFacade
                    .getQueryResultList("select * from hsl_color where type = 4 and page_id =" + pageId);
            HslColor ac = acs.get(0);
            List<HslColor> ccs = hslColorFacade
                    .getQueryResultList("select * from hsl_color where type = 5 and page_id =" + pageId);
            if (ccs.size() > 0) {
                HslColor cc = ccs.get(0);
                return "{\"font_color\":" + fc.getValue() + ",\"title_color\":" + tc.getValue() + ",\"background_color\":"
                        + bc.getValue() + ",\"content_background_color\":"
                        + cc.getValue() + ",\"aside_background_color\":" + ac.getValue() + "}";
            } else {
                return "0";
            }
        } else {
            return "0";
        }
    }
}
