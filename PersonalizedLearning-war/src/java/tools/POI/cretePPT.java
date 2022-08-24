package tools.POI;

import entities.Knowledge;
import entities.Question;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextBox;

/**
 *
 * @author hgs
 */
public class cretePPT {    
    String related = "关联", detail = "阐述";
    
    public void creatPPT(List<Object> objectList, String pptName) {
        //可以是知识点，也可以习题
        try {
            //create a new empty slide show
            HSLFSlideShow ppt = new HSLFSlideShow();
            for (int i = 0; i < objectList.size(); i++) {
                HSLFSlide s1 = ppt.createSlide();
                if (objectList.get(i) instanceof Knowledge) {
                    Knowledge knowledge = (Knowledge) objectList.get(i);
                    HSLFTextBox title = s1.addTitle();
                    title.setText(knowledge.getName());
                    StringBuilder sb = new StringBuilder();
                    sb.append(related).append(":")
                            .append(knowledge.getPreSuccNameString())
                            .append("\n")
                            .append(detail).append(":")
                            .append(knowledge.getDetails());
                    addText(s1, sb.toString());
                } else if (objectList.get(i) instanceof Question) {
                    Question question = (Question) objectList.get(i);
                    HSLFTextBox title = s1.addTitle();
                    title.setText(question.getKnowledgeId().getName());
                    StringBuilder sb = new StringBuilder();
                    sb.append(question.getValueinfo()).append("\n");
                    if (question.getSecondContenItems().length() > 0) {
                        sb.append(question.getSecondContenItems()).append("\n");
                    }
                    if (question.getSecondcontent() != null) {
                        sb.append(question.getSecondcontent()).append("\n");
                    }
                    sb.append(question.getAnswer()).append("\n");
                    addText(s1, sb.toString());
                }
            }
            FileOutputStream out = new FileOutputStream(pptName);
            ppt.write(out);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(cretePPT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void addText(HSLFSlide s1, String content) {
        HSLFTextBox txt = new HSLFTextBox();
        txt.setText(content);
        txt.setAnchor(new java.awt.Rectangle(300, 100, 300, 50));
        s1.addShape(txt);
    }
    
}
