package org.apdplat.qa.questiontypeanalysis;

import org.apdplat.qa.model.Question;
import org.apdplat.qa.model.QuestionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Naive bayes question classifier.
 */
public class CombinationQuestionClassifier extends AbstractQuestionClassifier {
    private static final Logger LOG = LoggerFactory.getLogger(CombinationQuestionClassifier.class);

    private List<QuestionClassifier> questionClassifierList = new ArrayList<QuestionClassifier>();

    public CombinationQuestionClassifier() {
    }

    public void addClassifier(QuestionClassifier classifier) {
        questionClassifierList.add(classifier);
    }

    @Override
    public Question classify(Question question) {
        for (QuestionClassifier classifier : questionClassifierList) {
            Question question1 = classifier.classify(question);
            if (question1.getQuestionType() != QuestionType.NULL) {
                return question1;
            }
            question = question1;
        }
        return question;
    }

    @Override
    public Question classify(String questionStr) {
        Question question = new Question();
        question.setQuestion(questionStr);
        return this.classify(question);
    }

    public static void main(String[] args) {

        QuestionClassifier questionClassifier = new CombinationQuestionClassifier();

        Question question = questionClassifier.classify("中秋节在什么时候");

        if (question != null) {
            LOG.info("问题【" + question.getQuestion() + "】的类型为：" + question.getQuestionType()
                    + " 候选类型为：" + question.getCandidateQuestionTypes());
        }
    }
}