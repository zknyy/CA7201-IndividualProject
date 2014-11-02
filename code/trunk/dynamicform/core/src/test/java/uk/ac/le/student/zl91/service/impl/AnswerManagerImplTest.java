package uk.ac.le.student.zl91.service.impl;

import org.appfuse.service.impl.BaseManagerMockTestCase;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import uk.ac.le.student.zl91.dao.AnswerDao;
import uk.ac.le.student.zl91.model.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
/**
 * The class is a test for AnswerManagerImpl
 * <p/>
 * Created on 2014/3/31.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class AnswerManagerImplTest extends BaseManagerMockTestCase {

    @InjectMocks
    private AnswerManagerImpl manager;

    @Mock
    private AnswerDao dao;

    @Test
    public void testGetAnswer() {
        log.debug("testing get...");
        //given
        final Long id = 2L;
        final Answer answer = new Answer();
        given(dao.get(id)).willReturn(answer);
        //when
        Answer result = manager.get(id);
        //then
        assertSame(answer, result);
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
    public void testSavePerson() {
        log.debug("testing save...");
        //given
        final Answer answer = new Answer();
        // enter all required fields

        given(dao.save(answer)).willReturn(answer);
        //when
        manager.save(answer);
        //then
        verify(dao).save(answer);
    }
    @Test
    public void testRemovePerson() {
        log.debug("testing remove...");
        //given
        final Long id = -11L;
        willDoNothing().given(dao).remove(id);
        //when
        manager.remove(id);
        //then
        verify(dao).remove(id);
    }
}