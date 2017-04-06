package org.apdplat.qa.score.evidence;

import java.util.List;

import org.apdplat.qa.model.Evidence;
import org.apdplat.qa.model.Question;
import org.apdplat.qa.system.ScoreWeight;
import org.apdplat.qa.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 对证据进行评分 【TermMatch评分组件】
 * 不管语法关系或词序，直接对问题和证据的词进行匹配
 * 对于问题中的词，在title中出现一次记2/idf分
 * 对于问题中的词，在snippet中出现一次记1/idf分
 *
 * @author 杨尚川
 */
public class TermMatchEvidenceScore implements EvidenceScore {

    private static final Logger LOG = LoggerFactory.getLogger(TermMatchEvidenceScore.class);
    private ScoreWeight scoreWeight = new ScoreWeight();

    @Override
    public void setScoreWeight(ScoreWeight scoreWeight) {
        this.scoreWeight = scoreWeight;
    }

    @Override
    public void score(Question question, Evidence evidence) {
        LOG.debug("*************************");
        LOG.debug("Evidence TermMatch评分开始");
        //1、对问题进行分词
        List<String> questionTerms = question.getWords();
        LOG.debug("questionTerms:" + questionTerms);
        //2、对证据进行分词
        List<String> titleTerms = evidence.getTitleWords();
        List<String> snippetTerms = evidence.getSnippetWords();
        LOG.debug("titleTerms:" + titleTerms);
        LOG.debug("snippetTerms:" + snippetTerms);
        //3、不管语法关系或词序，直接对问题和证据的词进行匹配
        //对于问题中的词，在evidence中出现一次记一分
        double score = 0;
        for (String questionTerm : questionTerms) {
            //忽略问题中长度为1的词
            if (questionTerm.length() < 2) {
                LOG.debug("忽略问题中长度为1的词:" + questionTerm);
                continue;
            }
            int idf = Tools.getIDF(questionTerm);
            if (idf > 0) {
                idf = 1 / idf;
            } else {
                idf = 1;
            }
            for (String titleTerm : titleTerms) {
                if (questionTerm.equals(titleTerm)) {
                    LOG.debug("title match: " + questionTerm + " " + titleTerm);
                    score += idf * 2;
                }
            }
            for (String snippetTerm : snippetTerms) {
                if (questionTerm.equals(snippetTerm)) {
                    LOG.debug("snippet match: " + questionTerm + " " + snippetTerm);
                    score += idf;
                }
            }
        }
        score *= scoreWeight.getTermMatchEvidenceScoreWeight();
        LOG.debug("Evidence TermMatch评分:" + score);
        evidence.addScore(score);
        LOG.debug("Evidence TermMatch评分结束");
        LOG.debug("*************************");
    }
}