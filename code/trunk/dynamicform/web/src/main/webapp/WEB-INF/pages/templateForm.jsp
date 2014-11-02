<%@ page import="uk.ac.le.student.zl91.model.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uk.ac.le.student.zl91.model.Template" %>
<%@ page import="uk.ac.le.student.zl91.model.TemplateStatus" %>
<%--
  User: Zhipeng Liang
  Date: 2014/3/31
  Time: 16:35
  this jsp file is the edit page of template.
--%>
<%@ include file="/common/taglibs.jsp" %>
<head>
    <title><fmt:message key="templateDetail.title"/></title>
    <meta name="menu" content="TemplateMenu"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="templateList.template"/></c:set>
<script type="text/javascript">var msgDelConfirm =
        "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<%--
<div class="col-sm-2">
    <h2><fmt:message key="templateProfile.heading"/></h2>
    <c:choose>
        <c:when test="${param.from == 'list'}">
            <p><fmt:message key="templateProfile.admin.message"/></p>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="templateProfile.message"/></p>
        </c:otherwise>
    </c:choose>
</div>
--%>

<div class="col-sm-3">
    <h2><fmt:message key="templateDetail.heading"/></h2>
</div>

<div class="col-sm-9">
    <s:form id="templateForm" action="saveTemplate" method="post" cssClass="well" validate="true" autocomplete="off">
        <s:hidden key="template.tid"/>
        <s:set name="template" value="template" scope="request"/>
        <% Template template= (request.getAttribute("template")==null)?null:(Template)request.getAttribute("template");
//            out.println("Answer:"+answer.getReferredAnswer().getAid());
        %>
        <div class="form-group">
                <s:textfield label="Name" key="template.name" cssClass="form-control"/>
        </div>
        <div class="form-group">
                <%--<s:textfield label="Status" key="template.status"  requiredLabel="true" required="true" cssClass="form-control"/>--%>
            <label class="control-label">Status*</label>
            <select name="template.status" id="templateForm_template_status"
                    class="form-control">
                <%
                    for (int i = 0; i < TemplateStatus.values().length; i++) {
                %>
                <option value="<%=TemplateStatus.values()[i].toString()%>"
                        <%=((template!=null)
                                && TemplateStatus.values()[i].equals(template.getStatus()))?"selected":""%>
                        >
                    <%=TemplateStatus.values()[i]%>
                </option>

                <%
                    }
                %>
            </select>
        </div>

        <s:set name="availableQuestionList" value="availableQuestionList" scope="request"/>

        <div class="form-group">
            <fieldset class="form-group">
                <label class="control-label">Questions:</label>
                <select name="questions" id="templateForm_template_questions"
                         multiple="true" class="form-control">
                    <%
                        List<Question> availableQuestionList =
                            ((List<Question>) request.getAttribute("availableQuestionList") == null) ? new ArrayList() : (List<Question>) request.getAttribute("availableQuestionList");

                        for (int i = 0; i < availableQuestionList.size(); i++) {
                    %>
                    <option value="<%=availableQuestionList.get(i).getQid()%>"
                            <%if((template!=null) && (template.getQuestionSet()!=null))
                            {
                                for(Question q : template.getQuestionSet()){
                                    if (q.getQid()==availableQuestionList.get(i).getQid()){
                                        out.print(" selected ");
                                    }
                                }
                            }
                            %>
                            >
                        <%=availableQuestionList.get(i).getQid()%>:<%=availableQuestionList.get(i).getTitle()%>
                    </option>
                    <%
                        }
                    %>
                </select>
            </fieldset>
        </div>


        <div id="actions" class="form-group form-actions">


            <button type="submit" id="templateForm_button_save" name="method:save" value="Save"
                    class="btn btn-primary ${(template.status=="EDITABLE")?"":"disabled"}">
                <i class="icon-ok icon-white"></i>
                Save
            </button>

            <%--<c:if test="${not empty template.tid}">--%>
                <%--<s:submit type="button" cssClass="btn btn-danger" method="delete" key="button.delete"--%>
                          <%--onclick="return confirmDelete('template')" theme="simple">--%>
                    <%--<i class="icon-trash"></i>--%>
                    <%--<fmt:message key="button.delete"/>--%>
                <%--</s:submit>--%>
            <%--</c:if>--%>
            <s:submit type="button" cssClass="btn btn-default" method="cancel" key="button.cancel" theme="simple">
                <i class="icon-remove"></i>
                <fmt:message key="button.cancel"/>
            </s:submit>
        </div>
    </s:form>
</div>