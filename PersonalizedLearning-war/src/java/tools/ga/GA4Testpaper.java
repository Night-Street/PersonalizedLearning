package tools.ga;

import tools.Tool;
import tools.ga.individual.Individual;
import tools.ga.Selection.GeneralProportionSelection;
import entities.Knowledge;
import entities.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import tools.ga.individual.BooleanTowDimArrayIndividual;
import tools.ga.problem.TestPaperProblem;

/**
 *
 * @author hgs 选择算子：
 * 1）在类GeneralProportionSelection中求随机数的范围的循环,"proUpLowNo=proportion(proUpLowNo,
 * j);"，的目的是 重新计算个体被选择的概率，如果不重新计算就是传统的轮盘赌，否则就是切断式选择算子 2）发现
 */
public class GA4Testpaper {

    private TestPaperProblem searchSpace = new TestPaperProblem();
    private BooleanTowDimArrayIndividual[] population = new BooleanTowDimArrayIndividual[50];
    private final int mutationPointNum = 20, maxGeneration = 500;
    private final float crossoverProbability = 0.9f;
    private final float mutationProbability = 0.1f;
    Random random = new Random();
    BooleanTowDimArrayIndividual bestIndividual;

    public List<Question> getCandidateQuestions(List<Knowledge> knowledgess, int targetScore, int hardIndex) throws CloneNotSupportedException {
        if (null == knowledgess || knowledgess.isEmpty()) {
            return new ArrayList<>();
        } else { 
            searchSpace.init(knowledgess, targetScore, hardIndex);
            if (!Tool.isSumScoreOverTarget(knowledgess, targetScore)) {

                //问题的搜索空间初始化发现题目的总分比要求的分数还要低，所以直接返回可用的题目
                List<Question> earlyResult = new LinkedList<>();
                for (HashMap<Integer, Question> questionMap : searchSpace.getQuestionMap()) {
                    earlyResult.addAll(questionMap.values());
                }
                return earlyResult;
            }
            //Init the population
            for (int i = 0; i < population.length; i++) {
                population[i] = this.searchSpace.initIndividual();
            }
            //
            evaluatePopulation();
            // System.out.println(bestIndividual.getFitness());// + "\t" + distanceSum/population.length);
            //Carry out evolution
            //while (population[0].getFitness() > 1.0) {//
            for (int i = 0; i < maxGeneration; i++) {
                Individual[] populationBeforeCrossover = new GeneralProportionSelection(population).getPopulationAfterSelection();
                //crossover
                Individual[] beforeMutationTem = new OnePointCross(populationBeforeCrossover, crossoverProbability).getPopulationAfterCrossover();

                BooleanTowDimArrayIndividual[] population4Mutation = new BooleanTowDimArrayIndividual[beforeMutationTem.length];
                System.arraycopy(beforeMutationTem, 0, population4Mutation, 0, beforeMutationTem.length);
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
            return searchSpace.getQuestions(population[0]);
        }
    }

    private void evaluatePopulation() throws CloneNotSupportedException {
        for (Individual individual : population) {//reevaluate the fitness
            individual.setFitness(-1);//force reevaluate the fitness
            individual.getFitness();
        }
        Arrays.sort(population);
        bestIndividual = population[0].clone();//best inidividual
//        //Calculate the distance sum from the best individual for the diversity
//        int distanceSum = 0;
//        for (int i = 1; i < population.length; i++) {
//            distanceSum += bestIndividual.getDistace(population[i]);
//        }
//        System.out.println(bestIndividual.getFitness());// + "\t" + distanceSum/population.length);
    }
}
