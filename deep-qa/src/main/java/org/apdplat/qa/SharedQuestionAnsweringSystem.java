package org.apdplat.qa;

import org.apdplat.qa.datasource.BaiduDataSource;
import org.apdplat.qa.system.CommonQuestionAnsweringSystem;
import org.apdplat.qa.system.QuestionAnsweringSystem;
import org.apdplat.qa.util.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared QA answering system, using baidu as the data source default.
 */
public class SharedQuestionAnsweringSystem {
    private static final Logger LOG = LoggerFactory.getLogger(SharedQuestionAnsweringSystem.class);

    private static final QuestionAnsweringSystem QUESTION_ANSWERING_SYSTEM = new CommonQuestionAnsweringSystem();

    static{
        if (NetworkUtils.isConnected()) {
            QUESTION_ANSWERING_SYSTEM.setDataSource(new BaiduDataSource());
            LOG.info("Connect to Internet, will use baidu as the data source");
        } else {
            LOG.warn("Could not connect to Internet, will use cache data in the local database!");
        }
    }
    public static QuestionAnsweringSystem getInstance(){
        return QUESTION_ANSWERING_SYSTEM;
    }
    public static void main(String[] args){

    }
}
