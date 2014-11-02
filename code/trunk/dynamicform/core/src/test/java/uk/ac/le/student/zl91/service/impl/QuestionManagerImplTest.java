package uk.ac.le.student.zl91.service.impl;

import org.appfuse.service.impl.BaseManagerMockTestCase;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.ac.le.student.zl91.dao.QuestionDao;
import uk.ac.le.student.zl91.model.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

/**
 * The class is a test for QuestionManagerImpl
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

public class QuestionManagerImplTest extends BaseManagerMockTestCase {

    @InjectMocks
    private QuestionManagerImpl manager;

    @Mock
    private QuestionDao dao;

    @Test
    public void testGetAnswer() {
        log.debug("testing get...");
        //given
        final Long id = 2L;
        final Question question = new Question();
        given(dao.get(id)).willReturn(question);
        //when
        Question result = manager.get(id);
        //then
        assertSame(question, result);
    }

    @Test
    public void testGetAll() {
        log.debug("testing getAll...");
        //given
        final List answerList = new ArrayList();
        given(dao.getAll()).willReturn(answerList);
        //when
        List result = manager.getAll();
        //then
        assertSame(answerList, result);
    }

    @Test
    public void testSaveQuestion() {
        log.debug("testing save...");
        //given
        final Question question = new Question();
        // enter all required fields

        given(dao.save(question)).willReturn(question);
        //when
        manager.save(question);
        //then
        verify(dao).save(question);
    }
    @Test
    public void testRemoveQuestion() {
        log.debug("testing remove...");
        //given
        final Long id = -11L;
        willDoNothing().given(dao).remove(id);
        //when
        manager.remove(id);
        //then
        verify(dao).remove(id);
    }


    @Test
    public void testGetAvailableQuestion() {
        log.debug("testing GetAvailableQuestion...");
//        //given
//        final List answerList = new ArrayList();
//        given(dao.getAll()).willReturn(answerList);
//        //when
//        List result = manager.findAvailableQuestion(7L);
//        //then
//        assertEquals("available Questions is one less than all.",answerList.size()-1,result.size());
    }
}
