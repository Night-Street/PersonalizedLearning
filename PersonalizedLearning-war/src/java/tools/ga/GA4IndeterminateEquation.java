package tools.ga;

import java.util.Arrays;
import java.util.Random;
import tools.ga.individual.Individual;
import tools.ga.Selection.GeneralProportionSelection;
import tools.ga.individual.IntOneDimArrayIndividual;
import tools.ga.problem.OneDimArrayIntindeterminateEquation;

/**
 *
 * @author hgs
 */
public class GA4IndeterminateEquation {

    private int[] candidateItem;
    private int target;
    private int[] scoreType;
    private int[][] constraints;

    private Individual[] population = new Individual[50];
    private final int mutationPointNum = 20, maxGeneration = 500;
    private final float crossoverProbability = 0.9f;
    private final float mutationProbability = 0.1f;
    Random random = new Random();
    Individual bestIndividual;
    OneDimArrayIntindeterminateEquation odaie;

    public int[] getCandidateSolutions(int targetScore) throws CloneNotSupportedException {
        int[] result = new int[constraints.length];
        switch (this.isOutsideRange(constraints, scoreType, targetScore)) {
            case LESS://No need for EA
                for (int i = 0; i < constraints.length; i++) {
                    result[i] = constraints[i][0];
                }
                break;
            case GREAT://No need for EA
                for (int i = 0; i < constraints.length; i++) {
                    result[i] = constraints[i][1];
                }
                break;
            case IN://Need EA
                break;
        }

        for (int i = 0; i < population.length; i++) {
            population[i] = this.odaie.initIndividual();
        }
        //
        evaluatePopulation();
        // System.out.println(bestIndividual.getFitness());// + "\t" + distanceSum/population.length);
        //Carry out evolution
        //while (population[0].getFitness() > 1.0) {//
        for (int i = 0; i < maxGeneration; i++) {
            Individual[] populationBeforeCrossover = new GeneralProportionSelection(population).getPopulationAfterSelection();
            //crossover
            Individual[] population4Mutation = new OnePointCross(populationBeforeCrossover, crossoverProbability).getPopulationAfterCrossover();
            //mutation
            for (Individual ind4Mutate : population4Mutation) {
                if (random.nextFloat() < mutationProbability) {
                    ind4Mutate.mutate(mutationPointNum);
                }
            }
            this.population = population4Mutation;
            population[population.length - 1] = bestIndividual;//replace with the best individual
            evaluatePopulation();
            if (population[0].getFitness() == 0) {//找到最优个体了
                break;
            }
        }
        IntOneDimArrayIndividual best = (IntOneDimArrayIndividual) population[0];
        result = best.getX();
        return result;
    }

    private void evaluatePopulation() throws CloneNotSupportedException {
        for (Individual individual : population) {//reevaluate the fitness
            individual.setFitness(-1);//force reevaluate the fitness
            individual.getFitness();
        }
        Arrays.sort(population);
        bestIndividual = population[0].clone();//best inidividual
        //Calculate the distance sum from the best individual for the diversity
        int distanceSum = 0;
        for (int i = 1; i < population.length; i++) {
            distanceSum += bestIndividual.getDistace(population[i]);
        }
        //System.out.println(bestIndividual.getFitness());// + "\t" + distanceSum/population.length);
    }

    /**
     * @return the candidateItem
     */
    public int[] getCandidateItem() {
        return candidateItem;
    }

    /**
     * @param candidateItem the candidateItem to set
     */
    public void setCandidateItem(int[] candidateItem) {
        this.setCandidateItem(candidateItem);
    }

    /**
     * @return the target
     */
    public int getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(int target) {
        this.target = target;
    }

    /**
     * @return the weight
     */
    public int[] getWeight() {
        return scoreType;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int[] weight) {
        this.setWeight(weight);
    }

    /**
     * @return the constraints
     */
    public int[][] getConstraints() {
        return constraints;
    }

    /**
     * @param constraints the constraints to set
     */
    public void setConstraints(int[][] constraints) {
        this.constraints = constraints;
    }

    private rangeResult isOutsideRange(int[][] constraints, int[] scoeType, int targetScore) {
        rangeResult result = rangeResult.UNCERTAIN;
        int sumMin = 0, sumMax = 0;
        for (int i = 0; i < scoeType.length; i++) {
            sumMin += scoeType[i] * constraints[i][0];
            sumMax += scoeType[i] * constraints[i][1];
        }

        if (sumMin < targetScore && sumMax > targetScore) {//如果所有的题目加起来的分数不满足要求的分数（如100分），则返回所有的题目，接下来不再进行进化计算
            result = rangeResult.IN;
        } else if (sumMin < targetScore) {
            result = rangeResult.LESS;
        } else if (sumMax > targetScore) {
            result = rangeResult.GREAT;
        }
        return result;
    }

    private enum rangeResult {
        LESS, GREAT, IN, UNCERTAIN
    }
}
