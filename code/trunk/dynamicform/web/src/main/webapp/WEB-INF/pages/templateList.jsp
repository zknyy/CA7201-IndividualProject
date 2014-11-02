<%--
  User: Zhipeng Liang
  User: admin
  Date: 2014/3/31
  Time: 14:28
  this jsp file is the list page of templates.
--%>
<%@ include file="/common/taglibs.jsp"%>


<head>
    <title><fmt:message key="templateList.title"/></title>
    <meta name="menu" content="DynamicMenu"/>
</head>

<div class="col-sm-12">
    <h2><fmt:message key="templateList.heading"/></h2>
    <div id="actions" class="btn-group">
        <a class="btn btn-primary" href="<c:url value="/editTemplate"/>" >
        <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
        </a>
        <a class="btn btn-default" href="<c:url value="/home"/>">
        <i class="icon-ok"></i> <fmt:message key="button.done"/>
        </a>
    </div>
    <display:table name="templates" class="table table-condensed table-striped table-hover" requestURI=""
                   id="template" export="true" pagesize="25" >
        <%--href="editTemplate"--%>
        <display:column property="tid" sortable="true" media="html"
                        paramId="tid" paramProperty="tid" titleKey="template.tid"/>
        <display:column property="tid" media="csv excel xml pdf" titleKey="template.tid"/>
        <%--<display:column property="status" sortable="true" titleKey="template.status"/>--%>
        <display:column property="name" sortable="true" titleKey="template.title"/>
        <display:column property="createTime" sortable="true" titleKey="template.createTime" />

<%--===================--%>
        <s:set name="status">
            ${(template ==null || template.status eq "EDITABLE")
        ?"EDITABLE":template.status}
        </s:set>
        <%--textcss is for representing the words of status--%>
        <s:set name="textcss">
            ${ (template ==null || template.status eq "EDITABLE")
        ? "text-primary"
        : (template.status eq "LIVING")?"text-success"
        : (template.status eq "DISCARDED")?"text-warning":""}
        </s:set>

        <display:column titleKey="template.status" sortable="true"
                        class="${textcss}  ">
            ${status}
        </display:column>

        <display:column titleKey="templateList.viewOrEdit" class="text-center">
            <a class="btn btn-${status eq "EDITABLE"?"primary":"success"}"  href="/editTemplate?tid=${template.tid}">
                <i class="icon-plus icon-white"></i>
                <s:if test= "#status eq 'EDITABLE'">
                    <fmt:message key="answerList.button.edit"/>
                </s:if>
                <s:else>
                    <fmt:message key="answerList.button.view"/>
                </s:else>
            </a>
        </display:column>
<%--====================--%>

        <%--<display:column>--%>
        <%--<a class="btn btn-primary ${(template.status=="EDITABLE")?"":"disabled"}" href="/editTemplate?tid=${template.tid}">--%>
            <%--<i class="icon-plus icon-white"></i> <fmt:message key="templateList.button.edit"/>--%>
        <%--</a>--%>
        <%--</display:column>--%>
        <%--<display:column>--%>
            <%--<a class="btn btn-success ${(template.status!="EDITABLE")?"":"disabled"}" href="/editTemplate?tid=${template.tid}">--%>
                <%--<i class="icon-plus icon-white"></i> <fmt:message key="templateList.button.view"/>--%>
            <%--</a>--%>
        <%--</display:column>--%>

        <display:column titleKey="templateList.peakOrFill" class="text-center">
            <s:if test="#status eq 'LIVING'">
                <a class="btn btn-info"
                   href="/editFilledForm?tid=${template.tid}">
                    <i class="icon-plus icon-white"></i> <fmt:message key="templateList.button.fill"/>
                </a>
            </s:if>

            <s:if test="#status eq 'EDITABLE' || #status eq 'DISCARDED'">
                <a class="btn btn-warning"
                   href="/editFilledForm?tid=${template.tid}">
                    <i class="icon-plus icon-white"></i> <fmt:message key="templateList.button.peek"/>
                </a>
            </s:if>
        </display:column>
        <display:column class="text-center">
            <s:if test="#textcss != 'text-warning'">
            <a class="btn btn-danger ${(template.status!="DISCARDED")?"":"disabled"}" href="/discardTemplate?tid=${template.tid}"
               onclick="return confirmDiscard('template')" theme="simple">
                <i class="icon-plus icon-white"></i> <fmt:message key="templateList.button.discard"/>
            </a>
            </s:if>
        </display:column>
        <%--============================--%>

        <%--<display:column>--%>
            <%--<a class="btn btn-danger ${(template.status!="DISCARDED")?"":"disabled"}" href="/discardTemplate?tid=${template.tid}"--%>
               <%--onclick="return confirmDiscard('template')" theme="simple">--%>
                <%--<i class="icon-plus icon-white"></i> <fmt:message key="templateList.button.discard"/>--%>
            <%--</a>--%>
        <%--</display:column>--%>
        <display:setProperty name="paging.banner.item_name"><fmt:message key="templateList.template"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="templateList.templates"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="templateList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="templateList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="templateList.title"/>.pdf</display:setProperty>
    </display:table>
</div>