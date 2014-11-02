<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="home.title"/></title>
    <meta name="menu" content="Home"/>
</head>
<body class="home">

<h2><fmt:message key="home.heading"/> - <%=request.getRemoteUser()%> !</h2>
<p><fmt:message key="home.message"/></p>

<h4>User administration</h4>
<ul class="glassList">
    <li>
        <a href="<c:url value='/editProfile'/>"><fmt:message key="menu.user"/></a>
    </li>
    <li>
        <a href="<c:url value='/selectFile'/>"><fmt:message key="menu.selectFile"/></a>
    </li>
</ul>


<h4>Dynamic Form</h4>
<ul class="glassList">
    <li>
        <a href="<c:url value='/answers'/>"><fmt:message key="menu.answers"/></a>
    </li>
    <li>
        <a href="<c:url value='/questions'/>"><fmt:message key="menu.questions"/></a>
    </li>
    <li>
        <a href="<c:url value='/templates'/>"><fmt:message key="menu.templates"/></a>
    </li>
    <li>
        <a href="<c:url value='/filledForms'/>"><fmt:message key="menu.filledForms"/></a>
    </li>
</ul>
</body>