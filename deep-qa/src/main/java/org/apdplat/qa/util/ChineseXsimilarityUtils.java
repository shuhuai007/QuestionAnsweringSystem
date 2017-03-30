package org.apdplat.qa.util;

import org.apdplat.qa.constants.QuestionAnswerConstants;
import org.apdplat.qa.model.QuestionSimilarity;
import ruc.irm.similarity.sentence.morphology.MorphoSimilarity;
import ruc.irm.similarity.sentence.morphology.SemanticSimilarity;

import java.util.List;

/**
 * Utils to compute similarity of two sentences.
 */
public class ChineseXsimilarityUtils {

    /**
     * Gets Similarity via semantic.
     *
     * @param sentence1 sentence1
     * @param sentence2 sentence2
     * @return double value of similarity
     */
    public static double getSentenceSimilarityViaSemantic(String sentence1, String sentence2) {
        double similarity = SemanticSimilarity.getInstance().getSimilarity(sentence1, sentence2);
        return similarity;
    }

    /**
     * Gets Similarity via morpho.
     *
     * @param sentence1 sentence1
     * @param sentence2 sentence2
     * @return double value of similarity
     */
    public static double getSentenceSimilarityViaMorpho(String sentence1, String sentence2) {
        double similarity = MorphoSimilarity.getInstance().getSimilarity(sentence1, sentence2);
        return similarity;
    }

    public static QuestionSimilarity getMaxSimilarity(List<String> questionList, String questionStr) {
        double maxSimilarity = 0;
        String questionWithMaxSimilarity = null;
        long startTime = System.currentTimeMillis();

        for (String question : questionList) {
            double similarity = getSentenceSimilarityViaSemantic(questionStr, question);
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                questionWithMaxSimilarity = question;
            }
            long currentTime = System.currentTimeMillis();
            if (maxSimilarity > QuestionAnswerConstants.MAX_SIMILARITY_THRESHOLD
                    || (currentTime - startTime) > QuestionAnswerConstants.MAX_COMPUTATION_TIME_THRESHOLD) {
                System.out.println("max similarity:" + maxSimilarity);
                break;
            }
        }

        QuestionSimilarity questionSimilarity = new QuestionSimilarity();
        questionSimilarity.setQuestion(questionWithMaxSimilarity);
        questionSimilarity.setSimilarity(maxSimilarity);

        return questionSimilarity;
    }

    public static void main(String[] args) {
        System.out.println(MorphoSimilarity.getInstance().getSimilarity("你是哪里人", "你家住在哪里"));
        System.out.println(MorphoSimilarity.getInstance().getSimilarity("你父母是哪里人", "你家住在哪里"));
        System.out.println(MorphoSimilarity.getInstance().getSimilarity("你来自哪个国家", "你家住在哪里"));

        System.out.println(ChineseXsimilarityUtils.getSentenceSimilarityViaSemantic("毛泽东？", "毛泽东"));
        System.out.println(ChineseXsimilarityUtils.getSentenceSimilarityViaMorpho("你是哪里人", "你家住在哪里"));

        String questionStr = "毛泽东是？";

        List<String> questionList = MySQLUtils.getQuestionList();
        System.out.println(getMaxSimilarity(questionList, questionStr));

    }

}
