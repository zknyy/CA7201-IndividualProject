package uk.ac.le.student.zl91.service;

import org.appfuse.service.GenericManager;
import uk.ac.le.student.zl91.model.Answer;

import java.util.List;

/**
 * The interface is offering a service to actions accessing its DAO object.
 * <p/>
 * Created on 2014/3/31.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public interface AnswerManager extends GenericManager<Answer, Long> {

    /**
     * get all answers.
     * @return : a list of all answers.
     */
    public List<Answer> getList();

    /**
     * get the available answers for resenting them on the page
     * of editing a question. a user can assign those answers to
     * the current question.
     *
     * @param qid
     * @return
     */
    public List<Answer> findAvailableAnswer(Long qid);
}
