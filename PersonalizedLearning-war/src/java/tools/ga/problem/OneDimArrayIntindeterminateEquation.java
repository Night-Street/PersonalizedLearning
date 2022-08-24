package tools.ga.problem;

import java.util.Random;
import tools.Tool;
import tools.ga.individual.IntOneDimArrayIndividual;

/**
 *
 * @author hgs
 */
public class OneDimArrayIntindeterminateEquation implements Problem<IntOneDimArrayIndividual> {

    private int targetScore;
    private int[] scoreType;
    private int[][] constraints;
    Random random=new Random();

    public OneDimArrayIntindeterminateEquation(int targetScore, int[] scoreType) {
        this.targetScore = targetScore;
        this.scoreType = scoreType;
    }

    public void init(int[][] constraints, int[] scoeType, int targetScore) {
        this.constraints = constraints;
        this.scoreType = scoeType;
        this.targetScore = targetScore;
    }

    public IntOneDimArrayIndividual initIndividual() {
        IntOneDimArrayIndividual individual = new IntOneDimArrayIndividual(this);
        int[] temX = new int[this.scoreType.length];
        for (int i = 0; i < temX.length; i++) {
            temX[i]=(int)Tool.getScaledY((float)(this.random.nextFloat()), 0,1,this.constraints[i][0], this.constraints[i][1]);
        }
        individual.setX(temX);
        return individual;
    }

    int sum = 0;//2018－07－30，JavaDB题目分布情况,共196分。

    @Override
    public float evaluate(IntOneDimArrayIndividual individual1) {
        IntOneDimArrayIndividual individual = (IntOneDimArrayIndividual) individual1;
        int sum = 0;
        for (int i = 0; i < scoreType.length; i++) {
            sum += scoreType[i] * individual.getX()[i];
        }
        return Math.abs(sum - targetScore);
    }

    public int[][] getConstraints() {
        return constraints;
    }

    public void setConstraints(int[][] constraints) {
        this.constraints = constraints;
    }
}
