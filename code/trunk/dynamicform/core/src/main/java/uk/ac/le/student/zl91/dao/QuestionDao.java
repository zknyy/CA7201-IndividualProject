package uk.ac.le.student.zl91.dao;


import org.appfuse.dao.GenericDao;
import uk.ac.le.student.zl91.model.Question;

import java.util.List;

/**
 * The interface is the DAO interface of the question entity.
 * This interface extends GenericDao which is a template interface for
 * customising the DAO interface.
 * <p/>
 * Created on 2014/3/31.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public interface QuestionDao extends GenericDao<Question, Long> {

    /**
     * get all questions
     * @return: a list of all questions
     */
    public List<Question> getList();

}