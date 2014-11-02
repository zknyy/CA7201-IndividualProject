package uk.ac.le.student.zl91.webapp.action;

import org.appfuse.service.GenericManager;
import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.model.Question;
import uk.ac.le.student.zl91.model.TemplateStatus;
import uk.ac.le.student.zl91.service.AnswerManager;
import uk.ac.le.student.zl91.service.QuestionManager;
import uk.ac.le.student.zl91.model.Template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The class handles creating, updating, deleting and preparing data on
 * the pages of question.
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class QuestionAction extends BaseAction {
    private QuestionManager questionManager;
    private AnswerManager answerManager;
    private GenericManager<Template, Long> templateManager;

    //the list of question shown in the list page of questions.
    private List<Question> questions;

    public void setQuestionManager(QuestionManager questionManager) {
        this.questionManager = questionManager;
    }

    public void setAnswerManager(AnswerManager answerManager) {
        this.answerManager = answerManager;
    }

    public void setTemplateManager(GenericManager<Template, Long> templateManager) {
        this.templateManager = templateManager;
    }

    public List getQuestions() {
        return questions;
    }

    //executed before show the list page of questions.
    public String list() {
        questions = questionManager.getAll();
        log.debug("questions.size:" + questions.size());
        return SUCCESS;
    }


    private Question question;
    private Long qid;

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * delete a question
     * @return SUCCESS
     */
    public String delete() {

        //get all answers belong to the question
        this.question = this.questionManager.get(this.question.getQid());
        Set<Answer> answerSetTemp = this.question.getAnswerSet();

        //remove the binding on two sides
        for(Answer a:answerSetTemp){
            a.setQuestion(null);
            this.answerManager.save(a);
            log.debug("delete the answer, aid: "+a.getAid());
        }
        this.question.getAnswerSet().clear();
        log.debug("delete the answers in question, qid: "+question.getQid());
        this.questionManager.save(question);

        //remove the question
        questionManager.remove(question.getQid());
        saveMessage(getText("question.deleted"));

        return SUCCESS;
    }

    Set<Question> availableSubQuestionSet=new HashSet<Question>();

    Set<Answer> availableAnswerSet=new HashSet<Answer>();

    Set<Template> availableTemplateSet=new HashSet<Template>();

    public Set<Question> getAvailableSubQuestionSet() {
        return availableSubQuestionSet;
    }

    public Set<Answer> getAvailableAnswerSet() {
        return availableAnswerSet;
    }

    public Set<Template> getAvailableTemplateSet() {
        return availableTemplateSet;
    }

    //initialise the parameter for edit page of question
    private void initQuestionFormParameter(){
        this.availableSubQuestionSet.addAll(this.combineCurrentSubQuestions(
                this.questionManager.findAvailableSubQuestion(qid)));
        if (this.question.getQuestionSet()!=null){
            this.availableSubQuestionSet.addAll(this.question.getQuestionSet());
        }

        this.availableAnswerSet .addAll( this.answerManager.findAvailableAnswer(null));
        if (this.question.getAnswerSet()!=null){
            this.availableAnswerSet.addAll(this.question.getAnswerSet());
        }

        for (Template t :this.templateManager.getAll()){
            if (t.getStatus()== TemplateStatus.EDITABLE)
                this.availableTemplateSet.add(t);
        }
        if (this.question.getTemplate()!=null){
            this.availableTemplateSet.add(this.question.getTemplate());
        }

    }

    //executed when use edit, create a question.
    public String edit() {
        log.debug("qid:" + qid);

        if (qid != null) {
            question = questionManager.get(qid);
        } else {
            question = new Question();
        }

        this.initQuestionFormParameter();
        System.out.println("questionAction edit->question:"+ question);
        return SUCCESS;
    }

    /**
     * the save method handel three actions on the edit/create page of question:
     * 1. create/update the question into database
     * 2. delete the question
     * 3. cancel and go back to the list page of questions
     * @return:  CANCEL, delete, SUCCESS, INPUT
     * @throws Exception
     */
    public String save() throws Exception {
//        String tid = getRequest().getParameter("tid");
        String tid = getRequest().getParameter("template.tid");
        log.debug("tid="+tid);
//        log.debug("ttid="+ttid);


        if (cancel != null) {
            return CANCEL;
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = false;
        if(question.getQid() == null) {
            isNew=true;
        }else{
            //set the associated answers to the question
            this.question.setAnswerSet(
                    this.questionManager.get(this.question.getQid()).getAnswerSet()
            );
        }


        //set the object of template for the question
        if (question.getTemplate()==null||question.getTemplate().getTid()==null){
            question.setTemplate(null);
        }
        else {
            question.setTemplate(this.templateManager.get(question.getTemplate().getTid()));
        }

        //set the selected sub-questions and answers for the question
        String[] subQuestions = getRequest().getParameterValues("subQuestions");
        String[] answers = getRequest().getParameterValues("answers");
        this.question.setQuestionSet(new HashSet<Question>(0));
        this.question.setAnswerSet(new HashSet<Answer>(0));
        question = questionManager.save(question);
        log.debug("subQuestions:"+subQuestions);
        if (subQuestions != null && subQuestions.length !=0)
            for (String sq : subQuestions) {
                if (sq != null && sq.length() != 0){
                    log.debug("sq:"+sq);
                    Question subQuestion = this.questionManager.get(Long.parseLong(sq));
                    subQuestion.setTemplate(this.question.getTemplate());
                    this.question.getQuestionSet().add(subQuestion);
                }
            }

        //remove the relation between the question and all its answers
        for (Answer a : this.question.getAnswerSet()){
            a.setQuestion(null);
            this.answerManager.save(a);
        }
        this.question.getAnswerSet().clear();//for showing the result on page
        if (answers != null && answers.length !=0)
            for (String a : answers) {
                if (a != null && a.length() != 0) {
                    Answer answer = this.answerManager.get(Long.parseLong(a));
                    answer.setQuestion(this.question);
                    this.question.getAnswerSet().add(answer);
                }
            }
        question = questionManager.save(question);
        String key ;
        if(isNew){
            key ="question.added";
        }
        else{
            key="question.updated";
        }
        saveMessage(getText(key));

        this.initQuestionFormParameter();

        if (isNew) {
            return SUCCESS;//created a new, then go to List
        } else {
            return INPUT;//updated an existed one, keep staying on the page
        }
    }

    private List<Question> combineCurrentSubQuestions(List<Question> questionList){
//        if
        questionList.addAll(this.question.getQuestionSet());
        return questionList;
    }
}
