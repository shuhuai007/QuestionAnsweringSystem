<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.apdplat.qa.model.Question"%>
<%@page import="org.apdplat.qa.model.Evidence"%>
<%@page import="org.apdplat.qa.model.CandidateAnswer"%>
<%@page import="org.apdplat.qa.model.QuestionType"%>
<%@page import="org.apdplat.qa.SharedQuestionAnsweringSystem"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    request.setCharacterEncoding("UTF-8");
    String questionStr = request.getParameter("q");
    Question question = null;
    List<CandidateAnswer> candidateAnswers = null;
    if (questionStr != null && questionStr.trim().length() > 3) {
        question = SharedQuestionAnsweringSystem.getInstance().answerQuestion(questionStr);
        if (question != null) {
            candidateAnswers = question.getAllCandidateAnswer();
        }
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" media="screen" />
        <link rel="stylesheet" type="text/css" href="css/customize.css" media="screen" />
        <script src="js/wordcloud.js"></script>
        <title>人机问答系统演示</title>
        <script type="text/javascript">
            function answer(){
                var q = document.getElementById("q").value;
                if(q == ""){
                    return;
                }
                location.href = "index.jsp?q="+q;
            }

        </script>
    </head>
    <body>
        <center>
        <img src="images/logo.png" alt="Show Me" height="200px"><br/>
            <%
              if ((candidateAnswers == null || candidateAnswers.size() == 0) && questionStr != null) {
            %>
            <div class="alert alert-warning" role="alert">
                抱歉我没看懂你要做什么:(
            </div>
            <%
                }
                if (candidateAnswers != null && candidateAnswers.size() > 0) {
            %>
	   <span class="label label-primary"><%=questionStr%></span>
            <canvas id="answer_cloud" width="1000" height="600"></canvas>

                    <%
                        int i = 0;
                        String answer_content = "[";
                        for (CandidateAnswer candidateAnswer : candidateAnswers) {
                            answer_content += "['" + candidateAnswer.getAnswer() + "'," + String.valueOf((int)Math.round(candidateAnswer.getScore()*10)) + "]" + ",";

                        }
                        answer_content = answer_content.substring(0, answer_content.length() - 1) + "]";
                    %>

            <input id="answer_content" type="hidden" value="<%=answer_content%>">
      <a href="index.jsp" class="btn btn-info" role="button">Return</a>
        <%
        } else {
        %>
            <div class="row">
                <div class="col-lg-6 center">
                    <div class="input-group">
                        <input id="q" name="q" type="text" class="form-control" placeholder="Ask for...">
                        <span class="input-group-btn">
                            <button class="btn btn-danger" type="button" onclick="answer();">Go!</button>
                          </span>
                    </div>
                </div>
            </div>

        <%
            }
        %>
    </center>
    </body>

    <script type="text/javascript">
        function loadAnswer(){
           var list = JSON.parse(document.getElementById('answer_content').value.replace(/'/g,"\""));
            WordCloud(document.getElementById('answer_cloud'), {list: list, gridSize: 60, weightFactor: 6});
        }
        loadAnswer();
    </script>
</html>
