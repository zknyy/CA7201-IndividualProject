<%--
  User: Zhipeng Liang
  User: admin
  Date: 2014/3/31
  Time: 14:28
  this jsp file is the list page of questions.
--%>
<%@ include file="/common/taglibs.jsp"%>


<head>
    <title><fmt:message key="questionList.title"/></title>
    <meta name="menu" content="DynamicMenu"/>
</head>

<div class="col-sm-12">
    <h2><fmt:message key="questionList.heading"/></h2>
    <div id="actions" class="btn-group">
        <a class="btn btn-primary" href="<c:url value="/editQuestion"/>" >
        <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
        </a>
        <a class="btn btn-default" href="<c:url value="/home"/>">
        <i class="icon-ok"></i> <fmt:message key="button.done"/>
        </a>
    </div>
    <display:table name="questions" class="table table-condensed table-striped table-hover" requestURI=""
                   id="questionList" export="true" pagesize="25">
        <%--href="editQuestion"--%>
        <display:column property="qid" sortable="true"  media="html"
                        paramId="qid" paramProperty="qid" titleKey="question.qid"/>
        <display:column property="qid" media="csv excel xml pdf" titleKey="question.qid"/>

        <display:column property="orderNumber" sortable="true" titleKey="question.order"/>
        <display:column property="title" sortable="true" titleKey="question.title"/>
        <display:column property="template.tid" sortable="true" titleKey="question.templateDesc"/>

        <%--
        <display:column titleKey="question.referredQuestion" property="referredQuestion.aid" sortable="true" />

        <display:column titleKey="question.referredQuestion" property="referredQuestion.aid" sortable="true" />
        --%>

        <display:column property="createTime" titleKey="question.createTime" sortable="true" />
        <%--<display:column property="template.status" titleKey="question.editable" sortable="true" />--%>

        <%--<display:column titleKey="question.editable" sortable="true">--%>
        <%--${(questionList.template ==null)?"EDITABLE":questionList.template.status}--%>
        <%--</display:column>--%>

        <s:set name="editable">
            ${(questionList.template ==null || questionList.template.status eq "EDITABLE")
        ?true:questionList.template.status}
        </s:set>
        <s:set name="textcss">
            ${ (questionList.template ==null || questionList.template.status eq "EDITABLE")
        ? "text-primary"
        : (questionList.template.status eq "LIVING")?"text-success"
        : (questionList.template.status eq "DISCARDED")?"text-warning":""}
            <%--#editable?text-success:text-warning--%>
        </s:set>

        <display:column titleKey="question.editable" sortable="true"
                class="${textcss}">
            ${editable?"EDITABLE":editable}
        </display:column>

        <display:column class="text-right">
            <a class="btn btn-${editable eq true?"primary":"success"}" href="/editQuestion?qid=${questionList.qid}">
                <i class="icon-plus icon-white"></i>
                <s:if test= "#editable">
                    <fmt:message key="questionList.button.edit"/>
                </s:if>
                <s:else>
                    <fmt:message key="questionList.button.view"/>
                </s:else>
            </a>
        </display:column>

        <%--<display:column>--%>
            <%--<a class="btn btn-success ${(questionList.template.status eq "LIVING"||questionList.template.status eq "DISCARDED")?"":"disabled"}" href="/editQuestion?qid=${questionList.qid}">--%>
                <%--<i class="icon-plus icon-white"></i> <fmt:message key="questionList.button.view"/>--%>
            <%--</a>--%>
        <%--</display:column>--%>

        <%--<display:column>--%>
            <%--<a class="btn btn-primary ${(questionList.template.status != "LIVING"&&questionList.template.status != "DISCARDED")?"":"disabled"}" href="/editQuestion?qid=${questionList.qid}">--%>
                <%--<i class="icon-plus icon-white"></i> <fmt:message key="questionList.button.edit"/>--%>
            <%--</a>--%>
        <%--</display:column>--%>


        <display:setProperty name="paging.banner.item_name"><fmt:message key="questionList.question"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="questionList.questions"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="questionList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="questionList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="questionList.title"/>.pdf</display:setProperty>
    </display:table>
</div>