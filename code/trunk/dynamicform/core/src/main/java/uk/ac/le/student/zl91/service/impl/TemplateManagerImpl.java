package uk.ac.le.student.zl91.service.impl;

import org.appfuse.dao.GenericDao;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.le.student.zl91.model.Template;
import uk.ac.le.student.zl91.service.TemplateManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class implements the method of findAvailableTemplate() and inherit CRUD
 * methods from GenericManagerImpl.
 * <p/>
 * Created on 2014/4/11.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

@Service("templateManager")
public class TemplateManagerImpl extends GenericManagerImpl<Template, Long> implements TemplateManager {

    @Autowired
    private GenericDao<Template, Long> templateDao;

    public GenericDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(GenericDao templateDao) {
        this.dao = templateDao;
        this.templateDao = templateDao;
    }


    @Override
    public List<Template> findAvailableTemplate(Long tid) {
        Map map = new HashMap();
//        map.put("qid", qid);
        List<Template> templateList = templateDao.findByNamedQuery("Template.findAvailableTemplate", map );
        if (tid!=null){
            templateList.remove(this.templateDao.get(tid));
        }
        return  templateList;
    }
}
