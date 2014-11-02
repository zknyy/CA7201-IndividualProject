package uk.ac.le.student.zl91.service.impl;

import org.appfuse.dao.GenericDao;
import org.appfuse.service.impl.BaseManagerMockTestCase;
import org.appfuse.service.impl.GenericManagerImpl;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.ac.le.student.zl91.model.FilledForm;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

/**
 * The class is a test for testing the functionality of CRUD on filled form
 * <p/>
 * Created on 2014/4/2.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class FilledFormManagerImplTest extends BaseManagerMockTestCase {

    @InjectMocks
    private GenericManagerImpl<FilledForm,Long> filledFormManager;

    @Mock
    private GenericDao<FilledForm,Long> dao;

    @Test
    public void testGetAnswer() {
        log.debug("testing get...");
        //given
        final Long id = 1L;
        final FilledForm filledForm = new FilledForm();
        given(dao.get(id)).willReturn(filledForm);
        //when
        FilledForm result = filledFormManager.get(id);
        //then
        assertSame(filledForm, result);
    }

    @Test
    public void testGetAll() {
        log.debug("testing getAll...");
        //given
        final List filledFormList = new ArrayList();
        given(dao.getAll()).willReturn(filledFormList);
        //when
        List result = filledFormManager.getAll();
        //then
        assertSame(filledFormList, result);
    }

    @Test
    public void testSavePerson() {
        log.debug("testing save...");
        //given
        final FilledForm filledForm = new FilledForm();
        // enter all required fields

        given(dao.save(filledForm)).willReturn(filledForm);
        //when
        filledFormManager.save(filledForm);
        //then
        verify(dao).save(filledForm);
    }
    @Test
    public void testRemovePerson() {
        log.debug("testing remove...");
        //given
        final Long id = -11L;
        willDoNothing().given(dao).remove(id);
        //when
        filledFormManager.remove(id);
        //then
        verify(dao).remove(id);
    }
}
