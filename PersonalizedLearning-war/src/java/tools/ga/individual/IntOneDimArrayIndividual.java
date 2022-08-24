package tools.ga.individual;

import tools.ga.problem.OneDimArrayIntindeterminateEquation;
import tools.ga.problem.Problem;

/**
 *
 * @author hgs
 */
public class IntOneDimArrayIndividual extends Individual {

    private int[] x;
    private float fitness = -1;

    public IntOneDimArrayIndividual(Problem problem) {
        super(problem);
    }

    @Override
    public Individual[] crossover(Individual anotherIndividual1) throws CloneNotSupportedException {
        IntOneDimArrayIndividual anotherIndividual = (IntOneDimArrayIndividual) anotherIndividual1;
        //Avoid the invalide crossover for the same individuals
        if (this.getDistace(anotherIndividual) == 0) {//两个体完全一样，所以首先进行变异操作，使得两个体不同，从而实现近亲交叉回避
            this.mutate(getMutateNum(this.x.length));
        }
        IntOneDimArrayIndividual[] result = {this.clone(), anotherIndividual.clone()};
        int rand1 = random.nextInt(this.x.length);
        int[] tem = new int[this.x.length - rand1];
        System.arraycopy(this.x, rand1, tem, 0, this.x.length - rand1);
        System.arraycopy(anotherIndividual.x, rand1, this.x, rand1, this.x.length - rand1);
        System.arraycopy(tem, 0, anotherIndividual.x, rand1, this.x.length - rand1);
        return result;
    }

    @Override
    public void mutate(int muatateNum) throws CloneNotSupportedException {
        OneDimArrayIntindeterminateEquation problem1=(OneDimArrayIntindeterminateEquation)this.problem;
        for (int i = 0; i < muatateNum; i++) {
            int j = random.nextInt(this.x.length);//mutate at this location
            int temValue = (int) (random.nextFloat() * (problem1.getConstraints()[j][1] - problem1.getConstraints()[j][0]) + problem1.getConstraints()[j][0]);
            this.x[j] = temValue;
        }
    }

    public int[] getX() {
        return x;
    }

    public void setX(int[] X) {
        this.x = X;
    }

    //
    @Override
    public int getDistace(Individual another1) {
        IntOneDimArrayIndividual another = (IntOneDimArrayIndividual) another1;
        int distance = 0;
        for (int i = 0; i < this.x.length; i++) {
            distance = this.x[i] == another.x[i] ? distance : distance + 1;
        }
        return distance;
    }

    @Override
    public IntOneDimArrayIndividual clone() throws CloneNotSupportedException {
        IntOneDimArrayIndividual result = (IntOneDimArrayIndividual) super.clone();
        int[] xnew = new int[x.length];
        for (int i = 0; i < xnew.length; i++) {
            System.arraycopy(x, 0, xnew, 0, x.length);
        }
        result.setX(xnew);
        return result;
    }
}
