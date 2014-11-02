<%--
  User: Zhipeng Liang
  User: admin
  Date: 2014/3/31
  Time: 14:28
  this jsp is the list page of answers.
--%>
<%@ include file="/common/taglibs.jsp"%>


<head>
    <title><fmt:message key="answerList.title"/></title>
    <meta name="menu" content="DynamicMenu"/>
</head>

<div class="col-sm-12">
    <h2><fmt:message key="answerList.heading"/></h2>
    <div id="actions" class="btn-group">
        <a class="btn btn-primary" href="<c:url value="/editAnswer"/>" >
        <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
        </a>
        <a class="btn btn-default" href="<c:url value="/home"/>">
        <i class="icon-ok"></i> <fmt:message key="button.done"/>
        </a>
    </div>
    <display:table name="answers" class="table table-condensed table-striped table-hover" requestURI=""
                   id="answer" export="true" pagesize="25">
        <%--href="editAnswer" --%>
        <display:column property="aid" sortable="true"
                        media="html" paramId="aid" paramProperty="aid" titleKey="answer.aid"/>
        <display:column property="aid" media="csv excel xml pdf" titleKey="answer.aid"/>
        <display:column property="type" sortable="true" titleKey="answer.type"/>
        <display:column property="orderNumber" sortable="true" titleKey="answer.order"/>
        <display:column property="title" sortable="true" titleKey="answer.title"/>
        <display:column property="question.template.tid" sortable="true" titleKey="answer.templateDesc"/>
        <display:column property="question.qid" sortable="true" titleKey="answer.questionDesc"/>

        <display:column titleKey="answer.referredAnswer" property="referredAnswer.aid" sortable="true" />

        <display:column titleKey="answer.referredQuestion" property="referredQuestion.qid" sortable="true" />

        <display:column titleKey="answer.defaultValue" property="defaultValue" sortable="true" />

        <display:column titleKey="answer.createTime" property="createTime" sortable="true" />

        <%--<display:column titleKey="answer.status" property="question.template.status" sortable="true" />--%>
        <s:set name="editable">
        ${(answer.question.template ==null || answer.question.template.status eq "EDITABLE")
        ?true:answer.question.template.status}
        </s:set>
        <s:set name="textcss">
            ${ (answer.question.template ==null || answer.question.template.status eq "EDITABLE")
        ? "text-primary"
        : (answer.question.template.status eq "LIVING")?"text-success"
        : (answer.question.template.status eq "DISCARDED")?"text-warning":""}
            <%--#editable?text-success:text-warning--%>
        </s:set>

        <display:column titleKey="answer.status" sortable="true"
                        class="${textcss}">
            ${editable?"EDITABLE":editable}
        </display:column>

        <display:column  class="text-right">
            <a class="btn btn-${editable eq true?"primary":"success"}" href="/editAnswer?aid=${answer.aid}">
                <i class="icon-plus icon-white"></i>
                <s:if test= "#editable">
                    <fmt:message key="answerList.button.edit"/>
                </s:if>
                <s:else>
                    <fmt:message key="answerList.button.view"/>
                </s:else>
            </a>
        </display:column>

        <%--<display:column>--%>
            <%--<a class="btn btn-success ${(answer.question.template.status eq "LIVING"||answer.question.template.status eq "DISCARDED")?"":"disabled"}" href="/editAnswer?aid=${answer.aid}">--%>
                <%--<i class="icon-plus icon-white"></i> <fmt:message key="answerList.button.view"/>--%>
            <%--</a>--%>
        <%--</display:column>--%>

        <%--<display:column>--%>
            <%--<a class="btn btn-primary ${(answer.question.template.status != "LIVING"&&answer.question.template.status != "DISCARDED")?"":"disabled"}" href="/editAnswer?aid=${answer.aid}">--%>
                <%--<i class="icon-plus icon-white"></i> <fmt:message key="answerList.button.edit"/>--%>
            <%--</a>--%>
        <%--</display:column>--%>

        <display:setProperty name="paging.banner.item_name"><fmt:message key="answerList.answer"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="answerList.answers"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="answerList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="answerList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="answerList.title"/>.pdf</display:setProperty>
    </display:table>
</div>