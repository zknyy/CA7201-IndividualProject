package uk.ac.le.student.zl91.dao;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.GenericDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.model.AnswerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * it is a test class for AnswerDao
 * <p/>
 * Created on 2014/3/29.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class AnswerDaoTest extends BaseDaoTestCase {
    @Autowired
    private AnswerDao answerDao;

    @Test
    public void testAllAnswer() throws Exception {
        List<Answer> answer = answerDao.getList();
        log.debug("answerList.size():" + answer.size());
        assertTrue(answer.size() > 0);
    }

    public void setAnswerDao(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Test
    public void testFirstAnswer() throws Exception {
//        log.debug("new answer:"+answer);
//        answer = (Answer) this.populate(answer);
        Map map = new HashMap();
        map.put("qid", 1L);
        List<Answer> answerList = answerDao.findByNamedQuery("Answer.findByQuestion", map );
        Answer a = answerList.get(0);
        assertEquals("the first answer type is...", AnswerType.TEXT,a.getType());
    }
}
