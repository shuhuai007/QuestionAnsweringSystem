/**
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.apdplat.qa.api;

import edu.stanford.nlp.util.StringUtils;
import org.apdplat.qa.SharedQuestionAnsweringSystem;
import org.apdplat.qa.constants.QuestionAnswerConstants;
import org.apdplat.qa.model.CandidateAnswer;
import org.apdplat.qa.model.Question;
import org.apdplat.qa.util.ChineseCharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Json over HTTP 接口
 * @author 杨尚川
 */
@WebServlet(name = "ask", urlPatterns = {"/api/ask"})
public class AskServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AskServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String questionStr = request.getParameter("q");
        if (ChineseCharUtils.isChinese(questionStr)) {
            questionStr = ChineseCharUtils.trimAllBlankChar(questionStr);
        }

        String n = request.getParameter("n");

        if (!checkQuestion(questionStr)) {
            LOG.info("the input question is：" + questionStr);
            throw new ServletException("The question could not be blank");
        }

        if (!checkN(n)) {
            LOG.info("the input n is：" + n);
            throw new ServletException("The parameter n is illegal, please check!");
        }

        int topN = retrieveTopN(n);

        List<CandidateAnswer> candidateAnswers = getCandidateAnswers(questionStr);

        LOG.info("问题：" + questionStr);

        try (PrintWriter out = response.getWriter()) {
            String json = JsonGenerator.generate(candidateAnswers, topN);
            out.println(json);
            LOG.info("答案：" + json);
        }
    }

    private List<CandidateAnswer> getCandidateAnswers(String questionStr) {
        Question question = null;
        List<CandidateAnswer> candidateAnswers = null;
        if (checkQuestion(questionStr)) {
            question = SharedQuestionAnsweringSystem.getInstance().answerQuestion(questionStr);
            if (question != null) {
                candidateAnswers = question.getAllCandidateAnswer();
            }
        }
        return candidateAnswers;
    }

    private int retrieveTopN(String n) {
        int topN = -1;
        if (n != null && StringUtils.isNumeric(n)) {
            topN = Integer.parseInt(n);
        }
        return topN;
    }

    private boolean checkQuestion(String questionStr) {
        return questionStr != null
                && questionStr.trim().length() >= QuestionAnswerConstants.QUESTION_MIN_LENGTH_THRESHOLD;
    }

    private boolean checkN(String n) {
        return org.apache.commons.lang.StringUtils.isNotBlank(n) && StringUtils.isNumeric(n);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
