package org.apdplat.qa.questiontypeanalysis.bayes;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import org.apdplat.qa.parser.WordParser;
import org.apdplat.qa.searcher.BestClassifierSearcher;
import org.apdplat.qa.util.Tools;
import org.apdplat.word.segmentation.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Util class to classify question via bayes.
 */
public class NaiveBayesClassifierPool {

    private static final Logger LOG = LoggerFactory.getLogger(BestClassifierSearcher.class);

    private static Classifier<String, String> bayes;

    public static void learn() {
        bayes = new BayesClassifier<String, String>();

        /*
         * The classifier can learn from classifications that are handed over to
         * the learn methods. Imagin a tokenized text as follows. The tokens are
         * the text's features. The category of the text will either be positive
         * or negative.
         */
        String file = "/org/apdplat/qa/questiontypeanalysis/AllTestQuestions.txt";
        Set<String> questions = Tools.getQuestions(file);
        LOG.info("从文件中加载" + questions.size() + "个问题：" + file);

        for (String questionWithType : questions) {
            String[] questionArr = questionWithType.split(":");
            if (questionArr != null && questionArr.length == 2) {
                String question = questionArr[0].trim();
                String questionTypeStr = questionArr[1].trim();
                bayes.learn(questionTypeStr, generateWordList(question));
            }
        }
    }

    private static List<String> generateWordList(String question) {
        List<String> wordStrList = new ArrayList<String>();
        List<Word> wordList = WordParser.parse(question);
        for (Word word : wordList) {
            wordStrList.add(word.getText());
        }
        return wordStrList;
    }

    public static String classify(String question) {
        if (bayes == null) {
            learn();
        }
        return bayes.classify(generateWordList(question)).getCategory();
    }

    public static void main(String[] args) {
        learn();

        String testStr = "清朝是什么时间灭亡的";
//        testStr = "哈工大是什么时候建校的";
//        testStr = "电灯是谁发现的";
//        testStr = "2014世界杯冠军";
//        testStr = "龙阳君是哪国人";
//        testStr = "你是哪里的";
        testStr = "中秋节在什么时候";
//        testStr = "曹操哪个朝代的";
        System.out.println(bayes.classify(generateWordList(testStr)).getCategory());
        System.out.println(bayes.classify(generateWordList(testStr)));
    }
}
