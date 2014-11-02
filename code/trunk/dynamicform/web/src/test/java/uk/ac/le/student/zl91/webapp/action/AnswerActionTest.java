package uk.ac.le.student.zl91.webapp.action;


import org.appfuse.service.GenericManager;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import uk.ac.le.student.zl91.model.Answer;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.junit.Before;
import org.junit.Test;
//import org.apache.struts.mock.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.ac.le.student.zl91.service.AnswerManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;

/**
 * This is test class for AnswerAction
 * <p/>
 * Created on 2014/3/31.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

public class AnswerActionTest extends BaseActionTestCase {
    private AnswerAction action;

    @Before
    public void onSetUp() {
        super.onSetUp();

        action = new AnswerAction();
        AnswerManager answerManager = (AnswerManager) applicationContext.getBean("answerManager");
        action.setAnswerManager(answerManager);

        // add a test person to the database
        Answer answer = new Answer();
        answer.setTitle("test title");

        // enter all required fields

        answerManager.save(answer);
    }

    @Test
    public void testList() throws Exception {
        assertEquals("File in testList()-SUCCESS",action.list(), ActionSupport.SUCCESS);
        assertTrue("File in testList()-size() >= 1",action.getAnswers().size() >= 1);
    }


    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setAid(1L);
        assertNull("File in testEdit()-getAnswer",action.getAnswer());
        assertEquals("File in testEdit()-success","success", action.edit());
        assertNotNull("File in testEdit()-getAnswer",action.getAnswer());
        assertFalse("File in testEdit()-hasActionErrors",action.hasActionErrors());
    }

    @Test
    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setAid(1L);
        assertEquals("File in testSave()-success","success", action.edit());
        assertNotNull("File in testSave()-getAnswer()",action.getAnswer());

        // update last name and save
        action.getAnswer().setTitle("Updated Title");
        assertEquals("File in testSave()-input","input", action.save());
        assertEquals("File in testSave()-Testing -> Updated Title","Updated Title", action.getAnswer().getTitle());
        assertFalse("File in testSave()-hasActionErrors",action.hasActionErrors());
        assertFalse("File in testSave()-hasFieldErrors",action.hasFieldErrors());
        assertNotNull("File in testSave()-messages",request.getSession().getAttribute("messages"));
    }

    @Test
    public void testRemove() throws Exception {
        ConfigurableWroManagerFactory a;
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        Answer answer = new Answer();
        answer.setAid(2L);
        action.setAnswer(answer);
        assertEquals("File in testRemove()-delete","success", action.delete());
        assertNotNull("File in testRemove()-messages",request.getSession().getAttribute("messages"));
    }
}
