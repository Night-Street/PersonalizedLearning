/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.NLP;

import com.hankcs.hanlp.HanLP;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author hgs
 */
public class WordsSegment {

    private Map<Integer, String> keyContents;

    private boolean newComing = true;
    Map<String, Set<Integer>> wordId = new HashMap<>();//倒排索引
    private Map<String, Integer> wordCount = new HashMap<>();//词频统计 //

    private synchronized void calculate() {
        if (newComing) {
            wordId.clear();
            wordCount.clear();
            if (keyContents != null) {
                for (Entry<Integer, String> entry : keyContents.entrySet()) {
                    String tem = entry.getValue();
                    String[] result = HanLP.segment(tem).toString().split(",");
                    for (String rs : result) {
                        if (rs.contains("/")
                                && !rs.contains("/en")
                                && rs.trim().length() > 0
                                && !rs.contains("\\")
                                && !rs.contains(">")
                                && !rs.contains("<")
                                && !rs.contains("&")
                                && !rs.contains("null")
                                && !tools.Tool.isEnglish(rs)//排除了纯英文和符号，从而把代码的部分内容排除了，参考https://wenwen.sogou.com/z/q862579359.htm
                                && !rs.contains("#")) {//否则是其它字符 
                            String tem1 = rs.substring(0, rs.indexOf("/")).trim();
                            if (tem1.trim().length() > 2) {//字符串长度应该大于2
                                //倒排
                                if (!wordId.containsKey(tem1)) {
                                    wordId.put(tem1, new HashSet<>());
                                }
                                wordId.get(tem1).add(entry.getKey());
                                //词频
                                if (!wordCount.containsKey(tem1)) {
                                    wordCount.put(tem1, 1);
                                } else {
                                    wordCount.put(tem1, wordCount.get(tem1) + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        newComing = false;
    }

    public void setKeyContents(Map<Integer, String> keyContents) {
        if (!keyContents.equals(this.keyContents)) {
            this.keyContents = keyContents;
            newComing = true;
        }
    }

    public Map<String, Integer> getWordCount() {
        calculate();
        return wordCount;
    }

    public Map<String, Set<Integer>> getWordId() {
        calculate();
        return wordId;
    }
}
