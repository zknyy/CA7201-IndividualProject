package uk.ac.le.student.zl91.dao;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.GenericDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.le.student.zl91.model.Response;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * it is a test class for testing the functionality of CRUD on entity of Response.
 * <p/>
 * Created on 2014/4/2.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public class ResponseDaoTest extends BaseDaoTestCase {

    @Autowired
    private GenericDao<Response, Long> responseDao;

    @Test
    public void testAllTemplate() throws Exception {
        List<Response> responseList = responseDao.getAll();
        log.debug("responseList.size():" + responseList.size());
        assertTrue(responseList.size() > 0);
    }

    @Test
    public void testFirstAnswer() throws Exception {
//        Response response = new Response();
//        response.setContent("checked");
        List<Response> templateList = this.responseDao.getAll();
        Response a = templateList.get(0);
        assertEquals("the first Response is same.", "Marry Chicken", a.getContent());
    }
}
