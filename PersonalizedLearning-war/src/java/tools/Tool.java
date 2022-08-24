package tools;

import entities.Knowledge;
import entities.Question;
import java.util.List;
import java.util.Random;

/**
 *
 * @author hgs
 */
public class Tool {

    static Random random = new Random();

    public static boolean isSumScoreOverTarget(List<Knowledge> knowledgesList, int targetScore) {
        boolean result = false;
        int maxScore = 0;
        for (Knowledge knowledge : knowledgesList) {
            int j = 0;
            for (Question question : knowledge.getQuestionSet()) {
                maxScore += question.getScore();
            }
        }
        if (maxScore >= targetScore) {//如果所有的题目加起来的分数不满足要求的分数（如100分），则返回所有的题目，接下来不再进行进化计算
            result = true;
        }
        return result;
    }
//等比例变换,已知上限，下限，和当前值，求映射的值

    public static float getScaledY(float x, float srcLowValue, float srcUpValue, float tgtLowValue, float tgtUpValue) {
        return tgtLowValue + ((x - srcLowValue) / (srcUpValue - srcLowValue)) * (tgtUpValue - tgtLowValue);
    }

    public static String getValue(String toBeAdd) {
        char[] temText = toBeAdd.toCharArray();
        char[] temResult = new char[temText.length];
        for (int i = 0; i < temText.length; i++) {
            char a = temText[i];
            temResult[i] = (char) (a - 2);
        }
        return String.valueOf(temResult);
    }

    public static int getRand(Object[] objects) {
        return random.nextInt(objects.length);
    }

    /*
     //英文占1byte，非英文（可认为是中文）占2byte，根据这个特性来判断字符
     */
    public static synchronized boolean isEnglish(String string) {
        char[] stringchar = string.toCharArray();
        int n = 0;
        for (char ch : stringchar) {
            if ((ch + "").getBytes().length == 1) {
                n++;
            }
        }
        return n == stringchar.length;
    }

    public static void main(String[] args) {
        String a = "good moring112+-";
        System.out.println(Tool.isEnglish(a));
    }

    public static synchronized String getExtension(String fileName) {
        int begin = fileName.lastIndexOf(".");
        return begin > 0 ? fileName.substring(begin + 1) : "";
    }
}
