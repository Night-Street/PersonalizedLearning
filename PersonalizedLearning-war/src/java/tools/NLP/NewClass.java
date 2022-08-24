/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.NLP;

import com.hankcs.hanlp.HanLP;

/**
 *
 * @author hgs
 */
public class NewClass {
    public static void main(String[] args) {
        System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！"));
 
    }
}
