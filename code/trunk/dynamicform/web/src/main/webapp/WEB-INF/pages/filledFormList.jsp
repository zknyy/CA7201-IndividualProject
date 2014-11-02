<%@ page import="org.appfuse.model.User" %>
<%@ page import="org.springframework.security.core.context.SecurityContext" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@ page import="org.springframework.security.access.AccessDeniedException" %>
<%--
  User: Zhipeng Liang
  Date: 2014/4/2
  Time: 11:36
  this jsp is the list page of filled forms.
--%>

<%@ include file="/common/taglibs.jsp"%>


<head>
    <title><fmt:message key="filledFormList.title"/></title>
    <meta name="menu" content="DynamicMenu"/>
</head>

<div class="col-sm-12">
    <h2><fmt:message key="filledFormList.heading"/></h2>
    <div id="actions" class="btn-group">
        <a class="btn btn-primary" href="<c:url value="/templates"/>" >
            <i class="icon-plus icon-white"></i> <fmt:message key="filledFormList.button.fill"/>
        </a>
        <a class="btn btn-default" href="<c:url value="/home"/>">
            <i class="icon-ok"></i> <fmt:message key="button.done"/>
        </a>
    </div>

    <display:table name="filledForms" class="table table-condensed table-striped table-hover" requestURI="" id="filledForm" export="true" pagesize="25">
        <%--href="editFilledForm"--%>
        <display:column property="ffid" sortable="true" media="html"
                        paramId="ffid" paramProperty="ffid" titleKey="filledForm.ffid"/>
        <display:column property="ffid" media="csv excel xml pdf" titleKey="filledForm.ffid"/>
        <display:column property="createTime" sortable="true" titleKey="filledForm.createTime" />
        <display:column property="updatedTime" sortable="true" titleKey="filledForm.updatedTime" />

        <display:column property="template.tid" sortable="true" titleKey="filledFormList.template.tid"/>
        <display:column property="template.name" sortable="true" titleKey="filledFormList.template.name"/>
        <display:column property="template.status" sortable="true" titleKey="filledFormList.template.status"/>
        <display:column property="user.username" sortable="true" titleKey="filledFormList.user.username"/>
        <%
            User currentUser = null;
            SecurityContext ctx = SecurityContextHolder.getContext();
            if (ctx.getAuthentication() != null) {
                Authentication auth = ctx.getAuthentication();
                if (auth.getPrincipal() instanceof UserDetails) {
                    currentUser = (User) auth.getPrincipal();
                } else if (auth.getDetails() instanceof UserDetails) {
                    currentUser = (User) auth.getDetails();
                } else {
                    throw new AccessDeniedException("User not properly authenticated.");
                }
            }
        %>
        <s:set name="currentUsername"><%=currentUser.getUsername()%></s:set>
        <display:column>
            <%--
                the present of update button is depended on two conditions:
                    1. the form is filled by the current user
                    2. the template of the filled form is in the status of "LIVING"
            --%>
            <a class="btn btn-primary
            ${(filledForm.user.username == currentUsername)
            &&(filledForm.template.status=="LIVING")?"":"disabled"}" href="/editFilledForm?ffid=${filledForm.ffid}">
                <i class="icon-plus icon-white"></i> <fmt:message key="filledFormList.button.update"/>
            </a>
        </display:column>

        <display:column>
            <a class="btn btn-success" href="/reportFilledForm?ffid=${filledForm.ffid}">
                <i class="icon-plus icon-white"></i> <fmt:message key="filledFormList.button.report"/>
            </a>
        </display:column>

        <display:setProperty name="paging.banner.item_name"><fmt:message key="filledFormList.filledForm"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="filledFormList.filledForms"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="filledFormList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="filledFormList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="filledFormList.title"/>.pdf</display:setProperty>
    </display:table>


</div>