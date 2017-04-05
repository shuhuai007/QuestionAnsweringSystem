package org.apdplat.qa.questiontypeanalysis.bayes;

import org.apdplat.qa.model.Question;
import org.apdplat.qa.questiontypeanalysis.AbstractQuestionClassifier;
import org.apdplat.qa.questiontypeanalysis.QuestionClassifier;
import org.apdplat.qa.questiontypeanalysis.QuestionTypeTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Naive bayes question classifier.
 */
public class NaiveBayesQuestionClassifier extends AbstractQuestionClassifier {

    private static final Logger LOG = LoggerFactory.getLogger(NaiveBayesQuestionClassifier.class);

    public NaiveBayesQuestionClassifier() {
        NaiveBayesClassifierPool.learn();
    }

    @Override
    public Question classify(Question question) {
        String category = NaiveBayesClassifierPool.classify(question.getQuestion());
        question.setQuestionType(QuestionTypeTransformer.transform(category));
        return question;
    }

    @Override
    public Question classify(String questionStr) {
        Question question = new Question();
        question.setQuestion(questionStr);
        return this.classify(question);
    }

    public static void main(String[] args) {

        QuestionClassifier questionClassifier = new NaiveBayesQuestionClassifier();

        Question question = questionClassifier.classify("中秋节在什么时候");

        if (question != null) {
            LOG.info("问题【" + question.getQuestion() + "】的类型为：" + question.getQuestionType()
                    + " 候选类型为：" + question.getCandidateQuestionTypes());
        }
    }
}