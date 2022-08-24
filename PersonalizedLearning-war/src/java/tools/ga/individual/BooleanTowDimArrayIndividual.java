package tools.ga.individual;

import java.util.Arrays;
import tools.ga.problem.Problem;

/**
 *
 * @author asus
 */
public class BooleanTowDimArrayIndividual extends Individual {

    private boolean[][] x;
    private float fitness = -1;
//    private int defalutMutateNumber = 5;

    public BooleanTowDimArrayIndividual(Problem problem) {
        super(problem);
    }

    public boolean[][] getX() {
        return x;
    }

    public void setX(boolean[][] x) {
        this.x = x;
    }

    @Override
    public void mutate(int muatateNum) throws CloneNotSupportedException {
        for (int i = 0; i < muatateNum; i++) {
            int j = random.nextInt(this.x.length);
            while (this.x[j].length == 0) {
                j = (j + 1) % this.x.length;
            }
            int r = random.nextInt(this.x[j].length);
            this.x[j][r] = !this.x[j][r];
        }
    }

    @Override
    public Individual[] crossover(Individual anotherIndividual1) throws CloneNotSupportedException {
        BooleanTowDimArrayIndividual anotherIndividual = (BooleanTowDimArrayIndividual) anotherIndividual1;
        //Avoid the invalide crossover for the same individuals
        if (this.getDistace(anotherIndividual) == 0) {//两个体完全一样，所以首先进行变异操作，使得两个体不同，从而实现近亲交叉回避
            int tem = this.x.length / 5;
            if (tem <= 1) {
                tem = 2;
            }
            this.mutate(tem);
        }
        BooleanTowDimArrayIndividual[] result = {this.clone(), anotherIndividual.clone()};
        int rand1 = random.nextInt(this.x.length);
        while (this.x[rand1].length == 0) {//如果这个知识点下有0个题目, then find another knowledge with questions
            rand1 = (rand1 + 1) % this.x.length;
        }
        switch (this.x[rand1].length) {
            case 1:
                //如果这个知识点下只有1个题目,mutate it
                if (result[0].x[rand1][0] == result[1].x[rand1][0]) {
                    result[0].x[rand1][0] = !result[0].x[rand1][0];
                    result[1].x[rand1][0] = !result[1].x[rand1][0];
                } else {
                    boolean tem = result[0].x[rand1][0];
                    result[0].x[rand1][0] = result[1].x[rand1][0];
                    result[1].x[rand1][0] = tem;
                }
                break;
            default:
                int temLength = this.x[rand1].length;
                if (temLength <= 0) {
                    temLength++;
                }
                //System.out.println("==========" + this.x[rand1].length);
                int rand2 = random.nextInt(temLength);
                // rand1 = (rand1 == 0 ? rand1 + 1 : rand1);
                rand2 = (rand2 == 0 ? rand2 + 1 : rand2);
                //处理第rand1行，交叉点在这一行,tem1要放到另一个个体中:-----3>1
                boolean[] tem1 = Arrays.copyOfRange(result[0].x[rand1], rand2, result[0].x[rand1].length);
                boolean[] tem2 = Arrays.copyOfRange(result[1].x[rand1], rand2, result[1].x[rand1].length);
                for (int i = 0; i < tem1.length; i++) {
                    result[0].x[rand1][rand2 + i] = tem2[i];
                    result[1].x[rand1][rand2 + i] = tem1[i];
                }
                break;
        }
        //再处理rand1后的内容，交换
        for (int i = rand1 + 1; i < result[0].x.length; i++) {
            boolean[] tem = Arrays.copyOf(result[0].x[i], result[0].x[i].length);
            result[0].x[i] = Arrays.copyOf(result[1].x[i], result[1].x[i].length);
            result[1].x[i] = Arrays.copyOf(tem, tem.length);
        }

        return result;
    }

    @Override
    public BooleanTowDimArrayIndividual clone() throws CloneNotSupportedException {
        BooleanTowDimArrayIndividual result = new BooleanTowDimArrayIndividual(problem);
        boolean[][] xnew = new boolean[x.length][];
        for (int i = 0; i < xnew.length; i++) {
            xnew[i] = new boolean[x[i].length];
            System.arraycopy(x[i], 0, xnew[i], 0, xnew[i].length);
        }
        result.setX(xnew);
        return result;
    }

    //
    public int getDistace(BooleanTowDimArrayIndividual another) {
        int distance = 0;
        for (int i = 0; i < this.x.length; i++) {
            for (int j = 0; j < this.x[i].length; j++) {
                distance = xor(this.x[i][j], another.x[i][j]) ? distance + 1 : distance;
            }
        }
        return distance;
    }

    private boolean xor(boolean b1, boolean b2) {
        if (b1 && b2 || !b1 && !b2) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getDistace(Individual another) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
