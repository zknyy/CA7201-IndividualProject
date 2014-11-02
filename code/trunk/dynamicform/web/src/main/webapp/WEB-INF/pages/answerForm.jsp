<%@ page import="uk.ac.le.student.zl91.model.AnswerType" %>
<%@ page import="uk.ac.le.student.zl91.model.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uk.ac.le.student.zl91.model.Answer" %>
<%@ page import="java.util.Set" %>
<%--
  User: Zhipeng Liang
  Date: 2014/3/31
  Time: 16:35
  this jsp is for edit/create an answer.
--%>
<%@ include file="/common/taglibs.jsp" %>
<head>
    <title><fmt:message key="answerDetail.title"/></title>
    <meta name="menu" content="AnswerMenu"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="answerList.answer"/></c:set>
<script type="text/javascript">var msgDelConfirm =
        "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<%--
<div class="col-sm-2">
    <h2><fmt:message key="answerProfile.heading"/></h2>
    <c:choose>
        <c:when test="${param.from == 'list'}">
            <p><fmt:message key="answerProfile.admin.message"/></p>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="answerProfile.message"/></p>
        </c:otherwise>
    </c:choose>
</div>
--%>
<%--<div class="row">--%>
    <h2><fmt:message key="answerDetail.heading"/></h2>
<%--</div>--%>
<%--<div class="row">--%>
    <s:form id="answerForm" action="saveAnswer" method="post" cssClass="well" validate="true" autocomplete="off">
        <s:hidden key="answer.aid"/>
        <s:set name="answer" value="answer" scope="request"/>
        <% Answer answer = (request.getAttribute("answer")==null)?null:(Answer)request.getAttribute("answer");
//            out.println("Answer:"+answer.getReferredAnswer().getAid());
        %>

        <s:textfield cssClass="form-control" key="answer.title"/>
        <div class="row">

            <div class="col-sm-6">
                <s:textfield label="Default Value" key="answer.defaultValue" cssClass="form-control"/>
            </div>
            <div class="col-sm-6">
                <s:textfield key="answer.hint" cssClass="form-control"/>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-4">
                <s:set name="type" value="answer.type.toString()" scope="request"/>
                <label class="control-label"><span class="required">*</span>Type:</label>
                <select  label="Type" name="answer.type" required="true" id="answerForm_answer_type" class="form-control">
                    <%
                        AnswerType[] types =   AnswerType.values();
                        for (int i = 0; i < types.length; i++) {
                    %>
                    <option value="<%=types[i].toString()%>" <%=(types[i].toString().equals(request.getAttribute("type"))) ? "selected" : ""%>><%=types[i].toString()%></option>

                    <%
                        }
                    %>
                </select>
            </div>
            <div class="col-sm-4">
                <s:textfield label="width (0~12)" key="answer.width"  requiredLabel="true" required="true" cssClass="form-control"/>
            </div>
            <div class="col-sm-4">
                <s:textfield label="Order" key="answer.orderNumber"  requiredLabel="true" required="true" cssClass="form-control"/>
            </div>

        </div>


        <div class="form-group">
            <s:set name="availableQuestionSet" value="availableQuestionSet" scope="request"/>
            <%
                Set<Question> availableQuestionSet =(Set<Question>) request.getAttribute("availableQuestionSet");
                System.out.println("availableQuestionSet.size():"+availableQuestionSet.size());
            %>
            <%--<s:textfield label="Belong to Question" key="answer.question.qid"--%>
                         <%--cssClass="form-control"/>--%>
            <fieldset class="form-group">
                <label class="control-label">Belong to Question</label>
                <select name="answer.question.qid" id="answerForm_answer_question"
                        class="form-control">
                    <option value="">--None--</option>
                    <%
                       for (Question question: availableQuestionSet) {
                    %>
                    <option value="<%=question.getQid()%>"
                            <%=((answer!=null)
                                    && (answer.getQuestion()!=null)
                                    && question.getQid()==answer.getQuestion().getQid())?"selected":""%>
                            >
                        <%=question.getQid()%>:<%=question.getTitle()%>
                    </option>

                    <%
                        }
                    %>
                </select>
            </fieldset>
        </div>


        <div class="form-group">
            <s:set name="availableReferredQuestionSet" value="availableReferredQuestionSet" scope="request"/>
            <%
                Set<Question> availableReferredQuestionSet =(Set<Question>) request.getAttribute("availableReferredQuestionSet");
            %>
            <fieldset class="form-group">
                <label class="control-label">Referred Question ID</label>
                <select name="answer.referredQuestion.qid" id="answerForm_answer_referredQuestion"
                        class="form-control">
                    <option value="">--None--</option>
                    <%
                        for (Question question:availableReferredQuestionSet) {
                    %>
                    <option value="<%=question.getQid()%>"
                            <%=((answer!=null)
                                    && (answer.getReferredQuestion()!=null)
                                    && question.getQid()==answer.getReferredQuestion().getQid())?"selected":""%>
                            >
                        <%=question.getQid()%>:<%=question.getTitle()%>
                    </option>

                    <%
                        }
                    %>
                </select>
            </fieldset>
        </div>


        <div class="form-group">
                <%--<s:textfield label="Referred Answer ID" key="answer.referredAnswer.aid" cssClass="form-control"/>--%>

            <fieldset class="form-group">
                <s:set name="availableReferredAnswerSet" value="availableReferredAnswerSet" scope="request"/>
                <label class="control-label">Referred Answer ID:</label>
                <select  name="answer.referredAnswer.aid" id="answerForm_answer_referredAnswer"
                         class="form-control" >
                    <option value="">--None--</option>
                    <%
                        Set<Answer> availableReferredAnswerSet= (Set<Answer>) request.getAttribute("availableReferredAnswerSet");
                        for (Answer a : availableReferredAnswerSet) {
                    %>
                    <option value="<%=a.getAid()%>"
                            <%=((answer!=null)
                                    && (answer.getReferredAnswer()!=null)
                                    && a.getAid()==answer.getReferredAnswer().getAid())?"selected":""%>
                            >
                        <%=a.getAid()%>:<%=a.getTitle()%></option>
                    <%
                        }
                    %>
                </select>
            </fieldset>
        </div>

        <s:set var="editable">${(answer.question==null
                    ||answer.question.template==null
                    ||answer.question.template.status=="EDITABLE")}</s:set>
        <%--<s:property value="editable"/>--%>
        <div id="actions" class="form-group form-actions">
            <%--<s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">--%>
                <%--<i class="icon-ok icon-white"></i>--%>
                <%--<fmt:message key="button.save"/>--%>
            <%--</s:submit>--%>

            <button type="submit" id="answerForm_button_save" name="method:save" value="Save"
                    class="btn btn-primary ${editable?"":"disabled"}">
                <i class="icon-ok icon-white"></i>
                Save
            </button>
            <c:if test="${not empty answer.aid and editable}">
                <s:submit type="button" cssClass="btn btn-danger" method="delete" key="button.delete"
                          onclick="return confirmDelete('answer')" theme="simple">
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
<%--<%System.out.println("---end of answerForm---");%>--%>