package uk.ac.le.student.zl91.dao;


import org.appfuse.dao.GenericDao;
import uk.ac.le.student.zl91.model.Answer;

import java.util.List;

/**
 * The interface is the DAO interface of the answer entity.
 * This interface extends GenericDao which is a template interface for
 * customising the DAO interface.
 * <p/>
 * Created on 2014/3/29.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public interface AnswerDao extends GenericDao<Answer, Long> {

    /**
     * get all answers
     * @return: a list of all answers.
     */
    public List<Answer> getList();

}