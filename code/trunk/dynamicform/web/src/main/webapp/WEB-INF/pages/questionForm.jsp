<%@ page import="uk.ac.le.student.zl91.model.Answer" %>
<%@ page import="uk.ac.le.student.zl91.model.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="uk.ac.le.student.zl91.model.Template" %>
<%@ page import="java.util.Set" %>
<%--
  User: Zhipeng Liang
  Date: 2014/3/31
  Time: 16:35
  this jsp file is the edit page of a question.
--%>
<%@ include file="/common/taglibs.jsp" %>
<head>
    <title><fmt:message key="questionDetail.title"/></title>
    <meta name="menu" content="QuestionMenu"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="questionList.question"/></c:set>
<script type="text/javascript">var msgDelConfirm =
        "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>


<%--<div class="row">--%>
    <h2><fmt:message key="questionDetail.heading"/></h2>
<%--</div>--%>

<%--<div class="row">--%>
    <s:form id="questionForm" action="saveQuestion" method="post" cssClass="well" validate="true" autocomplete="off">
        <s:hidden key="question.qid"/>
        <s:set name="question" value="question" scope="request"/>
        <% Question question = (request.getAttribute("question")==null)?null:(Question)request.getAttribute("question");
        %>

        <s:textfield cssClass="form-control" key="question.title"/>
        <div class="row">
            <div class="col-sm-10">

                <fieldset class="form-group">
                        <%--<s:textfield label="Belong to Template" key="question.template.tid"--%>
                        <%--cssClass="form-control"/>--%>

                    <label class="control-label">Belong to Template:</label>
                    <select name="question.template.tid" id="questionForm_question_questionSet"
                            class="form-control">
                        <option value="" <%=(
                                (question==null)
                                        || (question.getTemplate()==null)
                                        || (question.getTemplate().getTid()==null)
                        )?"selected":""%>>--None--</option>
                        <%
                            Set<Template> availableTemplateSet = (Set<Template>) request.getAttribute("availableTemplateSet");
                            for (Template template: availableTemplateSet) {
                        %>
                        <option value="<%=template.getTid()%>"
                                <%=((question!=null)
                                        && (question.getTemplate()!=null)
                                        && template.getTid()==question.getTemplate().getTid())?"selected":""%>
                                >
                            <%=template.getTid()%>:<%=template.getName()%>
                        </option>

                        <%
                            }
                        %>
                    </select>
                </fieldset>
            </div>
            <div class="col-sm-2">
                <s:textfield label="Order" key="question.orderNumber"
                             requiredLabel="true" required="true" cssClass="form-control"/>
            </div>
        </div>



        <fieldset class="form-group">
            <s:set name="availableAnswerSet" value="availableAnswerSet" scope="request"/>
            <label class="control-label">Answers:</label>
            <select name="answers" id="questionForm_question_answerSet"
                    multiple="true" class="form-control">
                <option value="" <%=(question.getAnswerSet().size()==0)?"selected":""%>>--None--</option>
                <%
                    Set<Answer> availableAnswerSet = (Set<Answer>) request.getAttribute("availableAnswerSet");
                    for (Answer answer : availableAnswerSet) {
                %>
                <option value="<%=answer.getAid()%>"
                        <%
                            for (Answer a : question.getAnswerSet()){
                                if (a.getAid()==answer.getAid())
                                    out.print(" selected ");
                            }
                        %>
                        >
                    <%=answer.getAid()%>:<%=answer.getTitle()%>
                </option>

                <%
                    }
                %>
            </select>
        </fieldset>

        <fieldset class="form-group">
            <s:set name="availableSubQuestionSet" value="availableSubQuestionSet" scope="request"/>
            <label class="control-label">Sub Questions:</label>
            <select name="subQuestions" id="questionForm_question_questionSet"
                    multiple="true" class="form-control">
                <option value="" <%=(question.getQuestionSet().size()==0)?"selected":""%>>--None--</option>

                <%
                    Set<Question> availableSubQuestionSet = (Set<Question>) request.getAttribute("availableSubQuestionSet");
                    for (Question subQuestion: availableSubQuestionSet) {
                %>
                <option value="<%=subQuestion.getQid()%>"
                    <%
                        for (Question q : question.getQuestionSet()){
                            if (q.getQid()==subQuestion.getQid())
                                out.print(" selected ");
                        }
                        %>
                        >
                    <%=subQuestion.getQid()%>:<%=subQuestion.getTitle()%>
                </option>

                <%
                    }
                %>
            </select>
        </fieldset>


        <s:set var="editable">${(question.template==null||question.template.status=="EDITABLE")}</s:set>
        <%--<s:property value="editable"/>--%>
        <div id="actions" class="form-group form-actions">
            <button type="submit" id="questionForm_button_save" name="method:save" value="Save"
                    class="btn btn-primary ${editable?"":"disabled"}">
                <i class="icon-ok icon-white"></i>
                Save
            </button>
            <%--<s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">--%>
                <%--<i class="icon-ok icon-white"></i>--%>
                <%--<fmt:message key="button.save"/>--%>
            <%--</s:submit>--%>
            <c:if test="${not empty question.qid and editable}">
                <s:submit type="button" cssClass="btn btn-danger" method="delete" key="button.delete"
                          onclick="return confirmDelete('question')" theme="simple">
                    <i class="icon-trash"></i>
                    <fmt:message key="button.delete"/>
                </s:submit>
            </c:if>
            <s:submit type="button" cssClass="btn btn-default" method="cancel" key="button.cancel" theme="simple">
                <i class="icon-remove"></i>
                <fmt:message key="button.cancel"/>
            </s:submit>
        </div>
    </s:form>
<%--</div>--%>