package uk.ac.le.student.zl91.webapp.action;

import org.appfuse.service.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.model.Question;
import uk.ac.le.student.zl91.model.Template;
import uk.ac.le.student.zl91.model.TemplateStatus;
import uk.ac.le.student.zl91.service.QuestionManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * The class handles creating, updating, deleting and preparing data on
 * the pages of template.
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class TemplateAction extends BaseAction {

    private QuestionManager questionManager;
    private GenericManager<Template, Long> templateManager;

    //the list of templates shown on the list page of templates.
    private List templates;

    public void setTemplateManager(GenericManager<Template, Long> templateManager) {
        this.templateManager = templateManager;
    }
    public void setQuestionManager(QuestionManager questionManager) {
        this.questionManager = questionManager;
    }

    public List getTemplates() {
        return templates;
    }

    public String list() {
        templates = templateManager.getAll();
        log.debug("templates.size:"+templates.size());
        return SUCCESS;
    }

    //the current object of template
    private Template template;
    //id of template
    private Long tid;

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    //executed when deleting the current template.
    //this method is never executed, as a template is not allowed to be deleted
    public String delete() {
        templateManager.remove(template.getTid());
        saveMessage(getText("template.deleted"));

        return SUCCESS;
    }

    //executed when discarding the current template.
    public String discard() {
        this.template = this.templateManager.get(this.tid);
        template.setStatus(TemplateStatus.DISCARDED);
        this.templateManager.save(this.template);
        return SUCCESS;
    }

    List<Question> availableQuestionList;
    public List<Question> getAvailableQuestionList() {
        return availableQuestionList;
    }

    //execute before show the update/create page.
    public String edit() {
        this.availableQuestionList = this.questionManager.getAll();

        log.debug("tid:" + tid);
        if (tid != null) {
            template = templateManager.get(tid);
        } else {
            template = new Template();
        }
        log.debug("template:"+template);
        return SUCCESS;
    }

    /**
     * the save method handel three actions on the edit/create page of question:
     * 1. create/update the question into database
     * 2. delete the question
     * 3. cancel and go back to the list page of questions
     * @return : CANCEL, delete, SUCCESS, INPUT
     * @throws Exception
     */
    public String save() throws Exception {
        this.availableQuestionList = this.questionManager.getAll();

        if (cancel != null) {
            return CANCEL;
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = false;
        if(template.getTid() == null) {
            isNew=true;
            this.template = this.templateManager.save(template);
        }else{
            this.template.setQuestionSet(
                    this.templateManager.get(this.template.getTid()).getQuestionSet());
        }


        log.debug("template:"+template);
        log.debug("this.template.getQuestionSet().size():"
                + this.template.getQuestionSet().size());

        String[] questions = getRequest().getParameterValues("questions");
        log.debug("questions:"+ Arrays.toString(questions));


        //remove the relation between the template and all its question
        for (Question q : this.template.getQuestionSet()){
            log.debug("before q:"+q);
            q.setTemplate(null);
            log.debug("delete q:"+q);
            this.questionManager.save(q);
        }

        this.template.getQuestionSet().clear();//for showing the result on page
        log.debug("1. template.getQuestionSet().size():"+ template.getQuestionSet().size());
//        this.templateManager.save(template);

        if (questions != null) {
            for (String q : questions) {
                Question question = this.questionManager.get(Long.parseLong(q));
                question.setTemplate(this.template);//save the tid in the question table(really do it in database)
                this.questionManager.save(question);
                this.template.getQuestionSet().add(question);//for show the result on page
            }
        }

        log.debug("2. template.getQuestionSet().size():"+ template.getQuestionSet().size());
        this.template = this.templateManager.save(template);

        String key ;
        if(isNew){
            key ="template.added";
        }
        else{
            key="template.updated";
        }
        saveMessage(getText(key));

        if (isNew) {
            return SUCCESS;//created a new, then go to List
        } else {
            return INPUT;//updated an existed one, keep staying on the page
        }
    }
}