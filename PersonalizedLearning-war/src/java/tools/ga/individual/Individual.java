package tools.ga.individual;

import java.util.Random;
import tools.ga.problem.Problem;

/**
 *
 * @author asus
 */
public abstract class Individual implements Cloneable, Comparable<Individual> {

    protected Problem problem;
    private float fitness = -1;
    Random random = new Random();

    public Individual(Problem problem) {
        this.problem = problem;
    }

    public float getFitness() {
        this.fitness = (fitness <= 0 ? problem.evaluate(this) : fitness);
        return fitness;
    }

    public void setFitness(float fx) {
        this.fitness = fx;
    }

    @Override
    public int compareTo(Individual t) {
        int result;
        if (this.getFitness() == t.getFitness()) {
            result = 0;
        } else if (this.getFitness() > t.getFitness()) {
            result = 1;
        } else {
            result = -1;
        }
        return result;

    }

    @Override
    public Individual clone() throws CloneNotSupportedException {
        super.clone();
        return null;
    }

    protected int getMutateNum(int length) {
        int tem = length / 5;
        if (tem <= 1) {
            tem = 2;
        }
        return tem;
    }
    //

    public abstract int getDistace(Individual another);

    public abstract Individual[] crossover(Individual anotherIndividual) throws CloneNotSupportedException;

    public abstract void mutate(int muatateNum) throws CloneNotSupportedException;
}
