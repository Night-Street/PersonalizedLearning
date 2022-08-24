/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forProgramJudge.testFiles.Judge;

import java.io.File;
import java.util.List;

/**
 *
 * @author haogs
 */
public interface Judge {
    public void judge(File file0, String stuNo,List fileNames, List sbscore,StringBuilder sbmemo);
}
