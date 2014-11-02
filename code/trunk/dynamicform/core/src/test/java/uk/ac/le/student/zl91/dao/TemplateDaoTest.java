package uk.ac.le.student.zl91.dao;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.GenericDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.model.Question;
import uk.ac.le.student.zl91.model.Template;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * it is a test class for testing the functionality of CRUD on entity of Template
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class TemplateDaoTest extends BaseDaoTestCase {

    @Autowired
    private GenericDao<Template, Long> templateDao;

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void testAllTemplate() throws Exception {
        List<Template> templateList = templateDao.getAll();
        log.debug("templateList.size():" + templateList.size());
        assertTrue(templateList.size() > 0);
    }

    @Test
    public void testFirstTemplate() throws Exception {
//        Template template = new Template();
//        template.setName("the first template");
        List<Template> templateList = this.templateDao.getAll();
        Template a =null;
        for (Template t:templateList){
            a = (t.getTid()==1L)?t:null;
            if (a!=null) break;
        }
        assertEquals("the first template is same.", "Report Foreign Body", a.getName());
    }

    @Test
    public void testRemoveTemplates() throws Exception {

        Template template = this.templateDao.get(1L);
        assertEquals("Before updated", 15, template.getQuestionSet().size());
        template.getQuestionSet().clear();
        assertEquals("After set updated",0,template.getQuestionSet().size());
        this.templateDao.save(template);


        Template newT = this.templateDao.get(1L);
        assertEquals("After saved",0,template.getQuestionSet().size());

        Question question = this.questionDao.get(1L);
        question.getTemplate().setTid(null);
        this.questionDao.save(question);
        assertEquals("After saved: the question 1 to ",null,question.getTemplate().getTid());


        Question q = this.questionDao.get(1L);
        assertEquals("After saved: the question 1:1 to ",null,q.getTemplate().getTid());

    }

    }
