package org.apdplat.qa.datasource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apdplat.qa.model.Question;
import org.apdplat.qa.system.CommonQuestionAnsweringSystem;
import org.apdplat.qa.system.QuestionAnsweringSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 从控制台读取问题 依赖GoogleDataSource
 *
 * @author 杨尚川
 */
public class ConsoleDataSource implements DataSource {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleDataSource.class);
    private static final int QUESTION_MINI_LENGTH = 3;
    private final DataSource dataSource;

    public ConsoleDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Question> getQuestions() {
        return getAndAnswerQuestions(null);
    }

    @Override
    public Question getQuestion(String questionStr) {
        return null;
    }

    @Override
    public List<Question> getAndAnswerQuestions(QuestionAnsweringSystem questionAnsweringSystem) {
        List<Question> questions = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            Question question = null;
            LOG.info("输入问题然后回车，以exit命令结束问题输入：");
            String line = reader.readLine();
            while (line != null && line.trim().length() > QUESTION_MINI_LENGTH) {
                if (line.startsWith("exit")) {
                    break;
                }
                if (!line.startsWith("#")) {
                    //搜索问题的证据并回答问题
                    //边从控制台中获取问题边解答
                    String questionStr = null;
                    String expectAnswer = null;
                    String[] attrs = line.trim().split("[:|：]");
                    if (attrs == null) {
                        questionStr = line.trim();
                    }
                    if (attrs != null && attrs.length == 1) {
                        questionStr = attrs[0];
                    }
                    if (attrs != null && attrs.length == 2) {
                        questionStr = attrs[0];
                        expectAnswer = attrs[1];
                    }
                    LOG.info("Question:" + questionStr);
                    LOG.info("ExpectAnswer:" + expectAnswer);
                    //获取证据
                    //构造问题
                    question = dataSource.getQuestion(questionStr);
                    if (question == null) {
                        LOG.error("未成功检索到问题相关证据");
                    } else {
                        question.setExpectAnswer(expectAnswer);
                        LOG.info(question.toString());
                        //回答问题
                        if (questionAnsweringSystem != null && question != null) {
                            questionAnsweringSystem.answerQuestion(question);
                        }
                        questions.add(question);
                    }
                }
                LOG.info("输入问题然后回车，以exit命令结束问题输入：");
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return questions;
    }

    @Override
    public Question getAndAnswerQuestion(String questionStr, QuestionAnsweringSystem questionAnsweringSystem) {
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //Google数据源
        DataSource dataSource = new GoogleDataSource();
        //控制台数据源
        dataSource = new ConsoleDataSource(dataSource);
        //人名问答系统
        QuestionAnsweringSystem questionAnsweringSystem = new CommonQuestionAnsweringSystem();
        //指定控制台数据源
        questionAnsweringSystem.setDataSource(dataSource);
        //回答问题
        questionAnsweringSystem.answerQuestions();
        //输出统计信息
        questionAnsweringSystem.showPerfectQuestions();
        questionAnsweringSystem.showNotPerfectQuestions();
        questionAnsweringSystem.showWrongQuestions();
    }
}