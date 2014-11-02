package uk.ac.le.student.zl91.service.impl;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.le.student.zl91.dao.AnswerDao;
import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.service.AnswerManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class implements the method of findAvailableAnswer() and inherit CRUD
 * methods from GenericManagerImpl.
 * <p/>
 * Created on 2014/3/31.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

@Service("answerManager")
public class AnswerManagerImpl extends GenericManagerImpl<Answer,Long> implements AnswerManager {

    private AnswerDao answerDao;

    public AnswerDao getAnswerDao() {
        return answerDao;
    }

    @Autowired
    public void setAnswerDao(AnswerDao answerDao) {
        // assign the specific dao object to the parent attribute the .this is important for testing.
        this.dao = answerDao;
        this.answerDao = answerDao;
    }

    @Override
    public List<Answer> getList() {
        return answerDao.getList();
    }


    @Override
    public List<Answer> findAvailableAnswer(Long aid){

        Map map = new HashMap();
//        map.put("qid", qid);
        List<Answer> questionList = this.answerDao.findByNamedQuery("Answer.findAvailableAnswer", map );
        if (aid!=null){
            questionList.remove(this.answerDao.get(aid));
        }
        return  questionList;
    }
}