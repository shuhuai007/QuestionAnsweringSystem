package org.apdplat.qa.util;

import ruc.irm.similarity.sentence.morphology.MorphoSimilarity;
import ruc.irm.similarity.sentence.morphology.SemanticSimilarity;

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

    public static void main(String[] args) {
//        System.out.println(MorphoSimilarity.getInstance().getSimilarity("你是哪里人", "你家住在哪里"));
//        System.out.println(MorphoSimilarity.getInstance().getSimilarity("你父母是哪里人", "你家住在哪里"));
//        System.out.println(MorphoSimilarity.getInstance().getSimilarity("你来自哪个国家", "你家住在哪里"));

        System.out.println(ChineseXsimilarityUtils.getSentenceSimilarityViaSemantic("你是哪里人", "你家住在哪里"));
        System.out.println(ChineseXsimilarityUtils.getSentenceSimilarityViaMorpho("你是哪里人", "你家住在哪里"));
    }
}
