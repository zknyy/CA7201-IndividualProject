package uk.ac.le.student.zl91.dao;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.GenericDao;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.le.student.zl91.model.FilledForm;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * it is a test class for testing the functionality of CRUD on entity of FilledForm
 * <p/>
 * Created on 2014/4/2.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class FilledFormDaoTest extends BaseDaoTestCase {

    @Autowired
    private GenericDao<FilledForm, Long> filledFormDao;
    @Autowired
    private UserDao userDao;

    @Test
    public void testAllFilledForm() throws Exception {
        List<FilledForm> filledFormList = filledFormDao.getAll();
        log.debug("filledFormList.size():" + filledFormList.size());
        assertTrue(filledFormList.size() > 0);
    }

    @Test
    public void testFirstFilledForm() throws Exception {
        FilledForm filledForm = new FilledForm();
        filledForm.setUser(userDao.get(1L));
        List<FilledForm> filledFormList = this.filledFormDao.getAll();
        FilledForm a = filledFormList.get(0);
        assertEquals("the first FilledForm is same.", filledForm.getUser().getId(), a.getUser().getId());
    }
}