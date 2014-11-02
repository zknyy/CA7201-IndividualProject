package uk.ac.le.student.zl91.service.impl;

import org.appfuse.dao.GenericDao;
import org.appfuse.service.impl.BaseManagerMockTestCase;
import org.appfuse.service.impl.GenericManagerImpl;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.ac.le.student.zl91.model.Template;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

/**
 * The class is a test for testing the functionality of CRUD on Template
 * <p/>
 * Created on 2014/4/1.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

public class TemplateManagerImplTest extends BaseManagerMockTestCase {

    @InjectMocks
    private GenericManagerImpl<Template,Long> templateManager;

    @Mock
    private GenericDao<Template,Long> dao;

    @Test
    public void testGetAnswer() {
        log.debug("testing get...");
        //given
        final Long id = 2L;
        final Template template = new Template();
        given(dao.get(id)).willReturn(template);
        //when
        Template result = templateManager.get(id);
        //then
        assertSame(template, result);
    }

    @Test
    public void testGetAll() {
        log.debug("testing getAll...");
        //given
        final List templateList = new ArrayList();
        given(dao.getAll()).willReturn(templateList);
        //when
        List result = templateManager.getAll();
        //then
        assertSame(templateList, result);
    }

    @Test
    public void testSavePerson() {
        log.debug("testing save...");
        //given
        final Template template = new Template();
        // enter all required fields

        given(dao.save(template)).willReturn(template);
        //when
        templateManager.save(template);
        //then
        verify(dao).save(template);
    }
    @Test
    public void testRemovePerson() {
        log.debug("testing remove...");
        //given
        final Long id = -11L;
        willDoNothing().given(dao).remove(id);
        //when
        templateManager.remove(id);
        //then
        verify(dao).remove(id);
    }
}
