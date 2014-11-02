<%--
  User: Zhipeng Liang
  Date: 2014/4/15
  Time: 16:06
  this jsp file is the report of a filled form.
--%>
<%@ page import="uk.ac.le.student.zl91.model.Answer" %>
<%@ page import="uk.ac.le.student.zl91.model.AnswerType" %>
<%@ page import="uk.ac.le.student.zl91.model.Question" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

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
//        System.out.println("----outputAnswer----");
        String as = new String();
        String type = answer.getType().toString();
        String qid_s= "Q_" + answer.getQuestion().getQid();
        String qid_s_group= answer.getType()+"|"+qid_s+"-GROUP";
        String aid_s= "A_" + answer.getAid();
        String id = answer.getType()+"|"+qid_s +"-" + aid_s;

//        System.out.println(">>>answer:"+answer);

        if (AnswerType.RADIO == answer.getType()
                &&answer.getContent()!=null
                &&answer.getContent().length()!=0) {

            as = as + answer.getTitle()+ " "
            ;

        } else if (AnswerType.TEXT == answer.getType()
                && answer.getContent()!=null
                && answer.getContent().trim().length()!=0) {
            String title =
                    (answer.getTitle() != null && answer.getTitle().trim().length() != 0) ?
                            answer.getTitle() : "";
            String hint = (answer.getHint()==null||answer.getHint().trim().length()==0) ? "" : answer.getHint();

            String value;
            if (title.length()>0){
                value = title+":"+answer.getContent()+" ";
            }
            else if (hint.length()>0){
                value = answer.getContent()+"("+answer.getHint()+") ";
            }
            else {
                value = answer.getContent()+" ";
            }
            as = as + value;

        } else if (AnswerType.CHECKBOX == answer.getType()
                && answer.getContent() != null
                && answer.getContent().trim().length()!=0) {
            as = as + answer.getTitle() + " ";
        } else if (AnswerType.TEXTAREA == answer.getType()
                && answer.getContent() != null
                && answer.getContent().trim().length()!=0) {
            as = as +  this.voidNull(answer.getContent())+" ";

        } else if (AnswerType.FILE == answer.getType()
                && answer.getContent() != null
                && answer.getContent().trim().length()!=0) {
//            String hiddenName = "<input type=\"hidden\" class=\"visible-print\" value=\""+answer.getContent()+"\">\n" ;
            String downloadButton=
                            "  <div class=\"col-sm-4\">\n"
                                    +"    <a class=\"btn btn-success\" href=\"/download?rid="
                                    + answer.getRid()+"\" role=\"button\">Download "
                                    + this.voidNull(answer.getContent())+"</a>"
                                    +"  </div>\n";

            as = as + downloadButton ;

        } else if (AnswerType.LABEL == answer.getType()) {
//            System.out.println("-----------AnswerType.LABEL");
            as = as
//                    + "<label class=\"control-label\">"
                    + this.voidNull(answer.getTitle())
//                    + "</label>"
            ;
        }

        return as;
    }
    private String outputAnswerInLine(Answer answer) {
//        System.out.println("----outputAnswerInLine----");
        String as = new String();
        as = as +"<div class=\"row\">";
//        as = as + outputAnswer(answer);
        Answer tempa = answer;
        while (
                (
                        tempa != null
                        && tempa.getContent() != null
                        && tempa.getContent().trim().length() != 0
                )||
                (
                        tempa != null
                        && tempa.getType() == AnswerType.LABEL
                )
                )
        {
            as = as +"<div class=\"col-sm-"+tempa.getWidth()+"\">";
            as = as + outputAnswer(tempa);
            as = as +"</div>";
            tempa = tempa.getReferredAnswer();
        }
        as = as +"</div>";
        return as;
    }

    private String outputQuestion(Question question) {
        String qs = new String();
        qs = qs + "<div class=\"form-group\">"
                + "<label class=\"control-label\">"+question.getTitle() + "</label>"

        ;

//         The Answer
        for (Iterator<Answer> it = question.getAnswerSet().iterator(); it.hasNext(); ) {
            Answer answer = it.next();
            qs = qs + outputAnswerInLine(answer);
        }

        qs = qs  + "</div>";

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


<div class="col-sm-12">
    <h2>${filledForm.template.name}</h2>
    <%--
    -- ${filledForm.ffid}
    ++ <s:property value="ffid"/>
    --%>


    <%List<Question> filledFormView = (List<Question>) request.getAttribute("filledFormView");%>
    <%--<%=filledFormView.size()%>--%>
    <s:form id="filledFormForm"
            action="saveFilledForm" method="post" enctype="multipart/form-data"
            cssClass="well" >
        <!-- validate="true" autocomplete="off"-->
        <s:hidden key="filledForm.ffid"/>
        <s:hidden key="filledForm.template.tid"/>
        <input type="hidden" name="FQMapping" id="FQMapping" value="">


        <%
            Collections.sort(filledFormView);
            for (int i = 0; i < filledFormView.size(); i++) {
                Question question = filledFormView.get(i);
                out.println(outputQuestion(question));
        %>

        <%
            }//end for Question
        %>

        <div id="actions" class="form-group form-actions hidden-print">
            <input id="btnPrint" type="button" class="btn btn-primary"
                   value="<fmt:message key="button.print"/>" onclick="javascript:window.print();" />

            <s:submit type="button" cssClass="btn btn-default" method="cancel"
                      key="button.cancel" theme="simple">
                <i class="icon-remove"></i>
                <fmt:message key="button.back"/>
            </s:submit>
        </div>
    </s:form>
</div>
