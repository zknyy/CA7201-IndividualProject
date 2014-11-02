package uk.ac.le.student.zl91.dao;

import org.appfuse.dao.BaseDaoTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.le.student.zl91.model.Question;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * it is a test class for QuestionDao
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class QuestionDaoTest extends BaseDaoTestCase {
    @Autowired
    private QuestionDao questionDao;

    @Test
    public void testAllAnswer() throws Exception {
        List<Question> questionList = questionDao.getList();
        log.debug("answerList.size():" + questionList.size());
        assertTrue(questionList.size() > 0);
    }

    @Test
    public void testFirstAnswer() throws Exception {
        List<Question> answerList = questionDao.getList();
        Question a = answerList.get(0);
        assertEquals("the first question is same.","Site Name",a.getTitle());
    }
}
