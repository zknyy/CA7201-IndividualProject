package uk.ac.le.student.zl91.webapp.action;

/**
 * The class handles creating, updating, deleting and preparing data on
 * the pages of answer.
 *
 * <p/>
 * Created on 2014/3/31.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.model.Question;
import uk.ac.le.student.zl91.service.AnswerManager;
import uk.ac.le.student.zl91.service.QuestionManager;

import java.util.*;

public class AnswerAction extends BaseAction {
    private AnswerManager answerManager;
    public void setAnswerManager(AnswerManager answerManager) {
        this.answerManager = answerManager;
    }

    private QuestionManager questionManager;
    public void setQuestionManager(QuestionManager questionManager) {
        this.questionManager = questionManager;
    }

    //all of answers
    private List answers;
    public List getAnswers() {
        return answers;
    }

    public String list() {
        answers = answerManager.getAll();
        log.debug("answers.size:"+answers.size());
        return SUCCESS;
    }


    //the current object of answer on the page.
    private Answer answer;
    //the current id of answer on the page.
    private Long aid;

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    //executed when user delete the current answer
    public String delete() {
        answerManager.remove(answer.getAid());
        saveMessage(getText("answer.deleted"));

        return SUCCESS;
    }

    //for the list of available questions showed on the answer form
    Set<Question> availableQuestionSet =new HashSet<Question>(0);

    //the available questions shown in the list box on page.
    Set<Question> availableReferredQuestionSet =new HashSet<Question>(0);

    //the available answers shown in the list box on page.
    Set<Answer> availableReferredAnswerSet =new HashSet<Answer>(0);

    public Set<Question> getAvailableQuestionSet() {
        return availableQuestionSet;
    }

    public Set<Question> getAvailableReferredQuestionSet() {
        return availableReferredQuestionSet;
    }

    public Set<Answer> getAvailableReferredAnswerSet() {
        return availableReferredAnswerSet;
    }

    //initialise those parameters for the edit page of answer.
    private void initAnswerFormParameters(){
        debugSubmit();

        this.availableQuestionSet.addAll(this.questionManager.findAvailableQuestion(null));
        if (this.answer.getQuestion()!=null) {
            this.availableQuestionSet.add(this.answer.getQuestion());
        }

        this.availableReferredQuestionSet.addAll(this.questionManager.findAvailableQuestion(null));
        if (this.answer.getReferredQuestion()!=null) {
            this.availableReferredQuestionSet.add(this.answer.getReferredQuestion());
        }

        this.availableReferredAnswerSet.addAll(this.answerManager.findAvailableAnswer(aid));
        if (this.answer.getReferredAnswer()!=null) {
            this.availableReferredAnswerSet.add(this.answer.getReferredAnswer());
        }
        log.debug("...end...");
    }

    /**
     * executed when a user click update the answer.
     *
     * @return : SUCCESS
     */
    public String edit() {
        log.debug("edit->aid:" + aid);

        if (aid != null) {
            answer = answerManager.get(aid);
        } else {
            answer = new Answer();
        }
        this.initAnswerFormParameters();
        System.out.println("AnswerAction edit->answer:"+answer);
        return SUCCESS;
    }

    /**
     * executed when a user save(update/create),delete a answer or give it up
     *
     * @return CANCEL, SUCCESS, INPUT
     * @throws Exception
     */
    public String save() throws Exception {
        if (cancel != null) {
            return CANCEL;
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = false;
        if(answer.getAid() == null) {
            isNew=true;
        }


        if (answer.getQuestion()==null||answer.getQuestion().getQid()==null){
            answer.setQuestion(null);
        }
        else {
            answer.setQuestion(this.questionManager.get(answer.getQuestion().getQid()));
        }

        if (answer.getReferredQuestion()==null||answer.getReferredQuestion().getQid()==null){
            answer.setReferredQuestion(null);
        }
        else {
            answer.setReferredQuestion(this.questionManager.get(answer.getReferredQuestion().getQid()));
        }

        if (answer.getReferredAnswer()==null||answer.getReferredAnswer().getAid()==null){
            answer.setReferredAnswer(null);
        }
        else {
            answer.setReferredAnswer(this.answerManager.get(answer.getReferredAnswer().getAid()));
        }
        answer = answerManager.save(answer);


        this.initAnswerFormParameters();

        String key ;
        if(isNew){
            key ="answer.added";
        }
        else{
            key="answer.updated";
        }
        saveMessage(getText(key));

        if (isNew) {
            return SUCCESS;//created a new, then go to List
        } else {
            return INPUT;//updated an existed one, keep staying on the page
        }
    }

    /**
     * for the purpose of debugging
     */
    private void debugSubmit() {
        Map<String, String[]> map = this.getRequest().getParameterMap();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); )
        {
            String key = it.next();
            String value = Arrays.toString(map.get(key));
            log.debug("Answer key:" + key + " value:" + value);
        }

        String q = this.getRequest().getParameter("answer.question.qid");
        log.debug("q==null: "+(q==null));
        if(q!=null){

            log.debug("q.length(): "+q.length());
        }
    }
}