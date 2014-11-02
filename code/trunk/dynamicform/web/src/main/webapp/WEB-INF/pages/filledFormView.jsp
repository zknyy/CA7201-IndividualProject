<%@ page import="uk.ac.le.student.zl91.model.Answer" %>
<%@ page import="uk.ac.le.student.zl91.model.AnswerType" %>
<%@ page import="uk.ac.le.student.zl91.model.Question" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%--
  User: Zhipeng Liang
  Date: 2014/4/1
  Time: 21:58
  this jsp file is the dynamic form (the view of filled form).
--%>
<%@ include file="/common/taglibs.jsp" %>
<head>
    <title><fmt:message key="filledFormDetail.title"/></title>
    <meta name="menu" content="FilledFormMenu"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="filledFormList.filledForm"/></c:set>
<script type="text/javascript">var msgDelConfirm =
        "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>


<%!
    private String voidNull(String s) {
        return (s == null || "null".equalsIgnoreCase(s)) ? "" : s;
    }
        /**
         * out put an answer on page.
         * The id of answer should be Q_[qid]_A_[aid] for example Q_2_A_4
         * @param answer
         * @return
         */
    private String outputAnswer(Answer answer) {
        String as = new String();
        String type = answer.getType().toString();
        String qid_s= "Q_" + answer.getQuestion().getQid();
        String qid_s_group= answer.getType()+"|"+qid_s+"-GROUP";
        String aid_s= "A_" + answer.getAid();
        String id = answer.getType()+"|"+qid_s +"-" + aid_s;


        if (AnswerType.RADIO == answer.getType()) {

            as = as
                    + "<div class=" + type + ">\n"
//                    + "  <label>\n"
                    + "    <input type=\"" + type + "\" name=\""+qid_s_group+"\""
                    +"  value=\""+id+"\" "
                    + this.voidNull(answer.getContent())+ ">\n"
//                    +answer.getAid() + ":"
                    + answer.getTitle()
//                    + "</label>\n"
                    + "</div>"
            ;

        } else if (AnswerType.TEXT == answer.getType()) {
            String lable =
                    (answer.getTitle() != null && answer.getTitle().trim().length() != 0) ?
                            "<div class=\"col-sm-4 text-right control-label\">" + answer.getTitle() + "</div>\n" :
                            "";
            String hintContent = (answer.getHint()==null||answer.getHint().trim().length()==0) ? "" : answer.getHint();
            String hint =  " placeholder=\""+hintContent+"\"";
            String value = (answer.getContent()==null||
            "null".equalsIgnoreCase(answer.getContent())) ? "":answer.getContent();
            as = as
                    + "<div class=\"" + type + "\">\n"
                    + lable
                    + "  <input type=\"" + type + "\" " +" name=\""+id+"\""
                    + " value=\""+value+"\""
                    + " class=\"form-control\" "
                    + hint
                    + ">\n"
                    + "  </input>\n"
                    + "</div>\n"
            ;
        } else if (AnswerType.CHECKBOX == answer.getType()) {
            as = as
                    + "<div class=\"" + type + "\">\n"
                    + "  <div>\n"
                    + "    <input type=\"" + type  + "\" name=\""+id+"\" value=\""+id+"\""
                    +  this.voidNull(answer.getContent())+"/>\n"
                    + answer.getTitle()
                    + "  </div>\n"
                    + "</div>\n"
            ;
        } else if (AnswerType.TEXTAREA == answer.getType()) {
            String hintContent = (answer.getHint()==null||answer.getHint().trim().length()==0) ? "" : answer.getHint();
            as = as
                    +"  <"+ type
                    + " placeholder=\""+hintContent+"\""
                    + " name=\""+id+"\" class=\"form-control\" rows=\"3\">"
                    +  this.voidNull(answer.getContent())
                    +"</textarea>\n"
            ;
        } else if (AnswerType.FILE == answer.getType()) {
            String downloadButton=
                    (this.voidNull(answer.getContent())=="")?"":
                    "  <div class=\"col-sm-4\">\n"
                    +"    <a class=\"btn btn-success\" href=\"/download?rid="+answer.getRid()+"\" role=\"button\">Download "+  this.voidNull(answer.getContent())+"</a>"
                    +"  </div>\n";
            as = as
                    +"<div class=\"row\">\n"
                    +"  <div class=\"col-sm-4\">\n"
                    +"    <input class=\"btn btn-info\" type=\"" + type+ "\" name=\"upload\""
                    +" id=\""+qid_s +"-" + aid_s+"\"></input>\n"
                    +"  </div>\n"
                    + downloadButton
                    +"</div>\n"
            ;
        } else if (AnswerType.LABEL == answer.getType()) {
            as = as
//            control-label
                    + "<div class=\"text-right\">"
                    + this.voidNull(answer.getTitle())
                    + "</div>"
            ;
        }

        return as;
    }
    private String outputAnswerInLine(Answer answer) {
        String as = new String();
        as = as +"<div class=\"row form-group\">";
//        as = as + outputAnswer(answer);
        Answer tempa = answer;
        while (tempa!= null) {
            as = as +"<div class=\"col-sm-"+tempa.getWidth()+"\">";
            as = as + outputAnswer(tempa);
            as = as +"</div>";
            tempa = tempa.getReferredAnswer();
        }
        as = as +"</div>";
        return as;
    }

    private String outputQuestion(Question question) {

        boolean isBlankQuestion = false;
        String qs = new String();

        String title  = question.getTitle();
        //the question without title means for grouping the radio answers
        if ((title == null || "null".equalsIgnoreCase(title)
        || title.trim().length()==0) ) {
            isBlankQuestion = true;
        }

        if (!isBlankQuestion){
            qs = qs + "<div class=\"form-group\">"
                    + "<label class=\"control-label\">"+question.getTitle() + "</label>"
            ;
        }

//         The Answers belong to the current question
        for (Iterator<Answer> it = question.getAnswerSet().iterator(); it.hasNext(); ) {
            Answer answer = it.next();
            qs = qs + outputAnswerInLine(answer);
        }

        if (!isBlankQuestion) {
            qs = qs + "</div>";
        }

        if (question.getQuestionSet().size()>0){
            System.out.println("ParentQid:"+question.getQid());
            List<Question> questionList = new ArrayList<Question>(question.getQuestionSet());
            Collections.sort(questionList);
            qs = qs + "<div class=\"row\">"
                    + "  <div class=\"col-sm-1\"></div>"
                    + "  <div class=\"col-sm-11\">";
            for (int i = 0; i < questionList.size(); i++) {
                System.out.println("ChildQid:"+questionList.get(i).getQid());

                qs = qs + outputQuestion(questionList.get(i));
            }
            qs = qs+ "  </div>"
                    + "</div>";

        }




        return qs;
    }
%>


<div >
    <h2>${filledForm.template.name}</h2>
    <%--
    -- ${filledForm.ffid}
    ++ <s:property value="ffid"/>
    --%>


    <s:form id="filledFormForm"
            action="saveFilledForm" method="post" enctype="multipart/form-data"
            cssClass="well" >
            <!-- validate="true" autocomplete="off"-->
        <s:hidden key="filledForm.ffid"/>
        <s:hidden key="filledForm.template.tid"/>
        <input type="hidden" name="FQMapping" id="FQMapping" value="">

        <%--receive the question list for building the dynamic form--%>
        <%List<Question> filledFormView = (List<Question>) request.getAttribute("filledFormView");%>
        <%  //generating the content of form question by question
            Collections.sort(filledFormView);
            for (int i = 0; i < filledFormView.size(); i++) {
                Question question = filledFormView.get(i);
                out.println(outputQuestion(question));
            }
        %>

        <div id="actions" class="form-group form-actions">
             <%--<s:set name="howSaveButtonShown">--%>
                 <%--${filledForm.template.status eq 'LIVING'?'':'disabled'}--%>
             <%--</s:set>--%>
            <button type="submit"
                    id="filledFormForm_button_save"
                    name="method:save" value="<fmt:message key="button.save"/>"
                    class="btn btn-primary ${filledForm.template.status eq 'LIVING'?'':'disabled'}"
                    onclick="fqMapping()">
                <i class="icon-ok icon-white"></i>
                Save
            </button>
            <%--<s:submit type="button"--%>
                      <%--cssClass="btn btn-primary"--%>
                      <%--method="save" key="button.save" theme="simple"--%>
                      <%--onclick="fqMapping()">--%>
                <%--<i class="icon-ok icon-white"></i>--%>
                <%--<fmt:message key="button.save"/>--%>
            <%--</s:submit>--%>
            <c:if test="${not empty filledForm.ffid}">
                <s:submit type="button" cssClass="btn btn-danger" method="delete" key="button.delete"
                          onclick="return confirmDelete('filledForm')" theme="simple">
                    <i class="icon-trash"></i>
                    <fmt:message key="button.delete"/>
                </s:submit>
            </c:if>
            <s:submit type="button" cssClass="btn btn-default" method="cancel" key="button.cancel" theme="simple">
                <i class="icon-remove"></i>
                <fmt:message key="button.cancel"/>
            </s:submit>


            <%--<button type="button" css="btn btn-primary" name="test"--%>
                      <%--onclick="fqMapping()">test</button>--%>
        </div>

    </s:form>
</div>


<script type="text/javascript">
    function fqMapping(){
        var uploads = document.getElementsByName("upload");
        var mapping = document.getElementById("FQMapping");
        mapping.value="";
        for (var i=0; i<uploads.length; i++ ){
            if ("" != uploads[i].value){
                mapping.value = mapping.value
                        + uploads[i].id
                        + "|"
                        + uploads[i].value
                        +"*";
            }
        }
        return true;
    }
</script>