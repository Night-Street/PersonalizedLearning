package tools.ga;

import tools.ga.individual.Individual;
import java.util.Random;

public class OnePointCross {

    private Random r = new Random();
    private Individual[] populationAfterCrossover;

    public OnePointCross(Individual[] population, float crossProbability) throws CloneNotSupportedException {
        boolean isOdd;
        int CrossNumber;
        populationAfterCrossover = new Individual[population.length];
        CrossNumber = population.length / 2;
        if (population.length % 2 == 0) {
            isOdd = false;
        } else {
            isOdd = true;
        }
        Individual[] temPopulation = null;
        for (int i = 0; i < CrossNumber; i++) {
            int tem1 = (int) (r.nextFloat() * population.length);
            int tem2 = (int) (r.nextFloat() * population.length);
            if (r.nextFloat() <= crossProbability) {//Should crossover
                temPopulation = population[tem1].crossover(population[tem2]);
            } else {
                temPopulation = new Individual[]{population[tem1], population[tem2]};
            }
            System.arraycopy(temPopulation, 0, populationAfterCrossover, 2 * i, temPopulation.length);
        }
        if (isOdd) {
            int tem1 = (int) (r.nextFloat() * population.length);
            int tem2 = (int) (r.nextFloat() * population.length);
            temPopulation = population[tem1].crossover(population[tem2]);
            populationAfterCrossover[population.length - 1] = temPopulation[0];
        }
    }
    public Individual[] getPopulationAfterCrossover() {
        return populationAfterCrossover;
    }
}
