package tools.ga.problem;

import entities.Knowledge;
import entities.Question;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import tools.ga.individual.BooleanTowDimArrayIndividual;
import tools.ga.individual.Individual;

/**
 *
 * @author asus
 * @param <BooleanArrayIndividual>
 */
public class TestPaperProblem implements Problem<BooleanTowDimArrayIndividual> {

    private List<Knowledge> knowledgesList;
    List<Question> questionList;
    private HashMap<Integer, Question>[] questionMap;
    private int targetScore = 0, hardnessIndex = 0;
    //HashMap<Question, Float> probability4Initiate = new HashMap<>();
    float maxScore = 0, maxHardness = 0;
    Random random = new Random();
    float roughProbability = 0.5f;
    //boolean knowledgeHasQuestion[]=new boolean[knowledgesList.size()];

    public void init(List<Knowledge> knowledgesList, int targetScore, int hardnessIndex) {
        this.targetScore = targetScore;
        this.hardnessIndex = hardnessIndex;
        this.knowledgesList = knowledgesList;
        maxScore = 0;
        //Deal with the corresponding relationship between the boolean array and knowledge question array
        setQuestionMap((HashMap<Integer, Question>[]) new HashMap[knowledgesList.size()]);
        int i = 0;
        
        for (Knowledge knowledge : knowledgesList) {
            getQuestionMap()[i] = new HashMap<>();
            int j = 0;
            for (Question question : knowledge.getQuestionSet()) {
                getQuestionMap()[i].put(j++, question);
                maxScore += question.getScore();
            }
            //knowledgeHasQuestion[i]=(j>0);
            i++;
        }
        if (maxScore == 0) {//There is no questions
            roughProbability = 0;
        } else {
            //finish dealing
            roughProbability = targetScore / maxScore;//make use the knowledge
            maxScore = Math.abs(maxScore - targetScore);
        }
        maxHardness = Math.max(Math.abs(hardnessIndex - 1), Math.abs(6 - hardnessIndex));
    }

    public BooleanTowDimArrayIndividual initIndividual() {
        BooleanTowDimArrayIndividual individual = new BooleanTowDimArrayIndividual(this);
        boolean[][] temX = new boolean[knowledgesList.size()][];
        for (int i = 0; i < knowledgesList.size(); i++) {
            temX[i] = new boolean[getQuestionMap()[i].size()];
            for (int j = 0; j < getQuestionMap()[i].size(); j++) {
                // temX[i][j] = random.nextBoolean();//randomly inited without considering the knowledge of problem
                temX[i][j] = random.nextFloat() < this.roughProbability;//make the corase
            }
        }
        individual.setX(temX);
        return individual;
    }

    public List<Question> getQuestions(BooleanTowDimArrayIndividual individual) {
        List<Question> resutlList = new LinkedList<>();
        for (int i = 0; i < knowledgesList.size(); i++) {
            for (int j = 0; j < knowledgesList.get(i).getQuestionSet().size(); j++) {
                if (individual.getX()[i][j]) {
                    resutlList.add(getQuestionMap()[i].get(j));
                }
            }
        }
        if (resutlList.isEmpty()) {
            //         System.out.println("TerribleThingsHapped!==========================================================");
        }
        return resutlList;
    }

    public void status(Individual individual1) {
        BooleanTowDimArrayIndividual individual = (BooleanTowDimArrayIndividual) individual1;
        HashMap<Integer, Integer> scoreMap = new HashMap<>();//2分的题目共有90道，4分的题目共有4道／／2018－07－30，JavaDB题目分布情况 。
        for (int i = 0; i < individual.getX().length; i++) {
            for (int j = 0; j < getQuestionMap()[i].size(); j++) {
                //================
                int hardSum1 = getQuestionMap()[i].get(j).getScore();
                if (scoreMap.containsKey(hardSum1)) {
                    int newValue = scoreMap.get(hardSum1) + 1;
                    scoreMap.put(hardSum1, newValue);
                } else {
                    scoreMap.put(hardSum1, 1);
                }
                //===================
            }
        }

        scoreMap.forEach(((k, u) -> {
            //System.out.print(k + ":" + u);
            sum += k * u;
        }));
        //System.out.println("============================sum is:" + sum);
    }
    int sum = 0;//2018－07－30，JavaDB题目分布情况,共196分。

    public HashMap<Integer, Question>[] getQuestionMap() {
        return questionMap;
    }

    public void setQuestionMap(HashMap<Integer, Question>[] questionMap) {
        this.questionMap = questionMap;
    }

    @Override
    public float evaluate(BooleanTowDimArrayIndividual individual) {
        float sum1 = 0, hardSum = 0, count = 0;
        for (int i = 0; i < individual.getX().length; i++) {
            for (int j = 0; j < getQuestionMap()[i].size(); j++) {
                if (individual.getX()[i][j]) {
                    sum1 += getQuestionMap()[i].get(j).getScore();
                    hardSum += getQuestionMap()[i].get(j).getDegree();
                    count++;
                }
            }
        }
        return Math.abs((sum1 - targetScore));/// maxScore
    }

}
