package org.apdplat.qa.model;

/**
 * Created by jiezhou on 30/03/2017.
 */
public class QuestionSimilarity {
    private String question;
    private double similarity;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return "QuestionSimilarity{" +
                "question='" + question + '\'' +
                ", similarity=" + similarity +
                '}';
    }
}
