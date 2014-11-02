package uk.ac.le.student.zl91.service.impl;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.le.student.zl91.dao.QuestionDao;
import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.model.Question;
import uk.ac.le.student.zl91.service.QuestionManager;

import java.util.*;

/**
 * The class implements the methods of findAvailableQuestion() and
 * findAvailableSubQuestion(), and inherit CRUD methods from
 * GenericManagerImpl.
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

@Service("questionManager")
public class QuestionManagerImpl extends GenericManagerImpl<Question, Long> implements QuestionManager {

    private QuestionDao questionDao;

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    @Autowired
    public void setQuestionDao(QuestionDao questionDao) {
        this.dao = questionDao;
        this.questionDao = questionDao;
    }


    @Override
    public List<Question> getList() {
        return questionDao.getList();
    }

    @Override
    public List<Question> findAvailableQuestion(Long qid) {
        Map map = new HashMap();
//        map.put("qid", qid);
        List<Question> questionList = questionDao.findByNamedQuery("Question.findAvailableQuestion", map );
        if (qid!=null){
            questionList.remove(this.questionDao.get(qid));
        }
        return  questionList;
    }

    @Override
    public List<Question> findAvailableSubQuestion(Long qid) {
        Map map = new HashMap();
//        map.put("qid", qid);
        List<Question> questionList = this.getAll();
        log.debug("All List.size():"+questionList.size());
        Set <Question> subQuestionSet = new HashSet<Question>();
        for (Question question:questionList){
            subQuestionSet.addAll(question.getQuestionSet());
        }
        log.debug("All subQuestionSet.size():"+subQuestionSet.size());//0?
        questionList = this.findAvailableQuestion(qid);
        log.debug("Available questionList.size():"+questionList.size());
        questionList.removeAll(subQuestionSet);
        log.debug("Available SubQuestionList.size():"+questionList.size());
        if (qid!=null){
            questionList.remove(this.questionDao.get(qid));
        }
        return  questionList;
    }


}
