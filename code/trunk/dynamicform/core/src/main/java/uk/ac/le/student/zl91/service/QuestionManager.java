package uk.ac.le.student.zl91.service;

import org.appfuse.service.GenericManager;
import uk.ac.le.student.zl91.model.Question;

import java.util.List;

/**
 * The interface is offering a service to actions accessing its DAO object.
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

public interface QuestionManager extends GenericManager<Question, Long> {

    /**
     * get all question
     * @return : the list of all question
     */
    public List<Question> getList();

    /**
     * get the available questions for resenting those questions on the page
     * of editing a template. a user can assign those questions to the current
     * template.
     * @param qid: a qid of question which is removed from the result
     * @return: a list of available questions
     */
    public List<Question> findAvailableQuestion(Long qid);

    /**
     * get the available sub-questions for resenting them on the page
     * of editing a question. a user can assign those sub-questions to
     * the current question.
     *
     * @param qid: a qid of question which is removed from the result
     * @return: a list of available questions
     */
    public List<Question> findAvailableSubQuestion(Long qid);

}