package tools.ga.Selection;

import java.util.Random;
import tools.ga.individual.Individual;

public class GeneralProportionSelection extends AbstractGASelection {

    private float[][] proUpLowNo;
    private int[] individualNo;

    public GeneralProportionSelection(Individual[] population) throws CloneNotSupportedException {
        populationAfterSelection = new Individual[population.length];
        this.proUpLowNo = new float[population.length][2];
        this.individualNo = new int[population.length];
        float temValue = 0, sumValue = 0, maxF = 0;
        for (Individual solution : population) {
            sumValue += solution.getFitness();
            if (solution.getFitness() > maxF) {
                maxF = solution.getFitness();
            }
        }
        float nMaxf = population.length * maxF;
        for (int i = 0; i < population.length; i++) { //calculate the probabiltiysum
            proUpLowNo[i][0] = temValue;
            proUpLowNo[i][1] = temValue + ((maxF - population[i].getFitness()) / (nMaxf - (sumValue)));
            temValue = proUpLowNo[i][1];
        }
        float temd;
        Random r = new Random();
        for (int i = 0; i < population.length; i++) {
            temd = r.nextFloat();
            for (int j = 0; j < population.length; j++) {
                if (temd <= proUpLowNo[j][1] && temd >= proUpLowNo[j][0]) {
                    individualNo[i] = j;
                    //基于上限的切断式采样：计算机工程与应用：遗传选择算子的比较与研究，2007，43（15）：59－62＋65
                    proUpLowNo = proportion(proUpLowNo, j);
                    //如果注释掉上面这句话，那么就是最普通的轮盘选择，其容易带来早熟 
                    break;
                }
            }
        }
        for (int i = 0; i < population.length; i++) {//clone the selected individual
            populationAfterSelection[i] = population[individualNo[i]].clone();
        }
    }

    @Override
    public Individual[] getPopulationAfterSelection() {
        return populationAfterSelection;
    }

    private float[][] proportion(float[][] srcPro, int selected) {
        //First, pre proceed the srcPro; Secondly, deal with the result
        //First
        //No. selecte should be cutted
        float proportion = 1.0f / srcPro.length;
        float cutted = srcPro[selected][1] - srcPro[selected][0];
        float sumValue = 0f;
        if (cutted > proportion) {//
            srcPro[selected][1] = srcPro[selected][0] + cutted - proportion;
            sumValue = 1 - proportion;
        } else {
            srcPro[selected][1] = srcPro[selected][0];
            sumValue = 1 - cutted;
        }
        //Secondly
        float result[][] = new float[srcPro.length][2];
        result[0][0] = 0;
        result[0][1] = srcPro[0][1];
        for (int i = 1; i < srcPro.length; i++) { //calculate the probabiltiysum
            result[i][0] = result[i - 1][1];
            result[i][1] = result[i][0] + ((srcPro[i][1] - srcPro[i][0]) / sumValue);
        }
        result[result.length - 1][1] = 1;
        return result;
    }
}
