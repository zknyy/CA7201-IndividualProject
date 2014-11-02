package uk.ac.le.student.zl91.service;

import org.appfuse.service.GenericManager;
import uk.ac.le.student.zl91.model.Template;

import java.util.List;

/**
 * The interface is offering a service to actions accessing its DAO object.
 * <p/>
 * Created on 2014/4/11.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
public interface TemplateManager extends GenericManager<Template, Long> {

    /**
     * get the available templates for being assigned to a specific question.
     * an available template must be EDITABLE.
     * @param tid : a tid of template which is removed from the result.
     * @return : a list of available templates
     */
    public List<Template> findAvailableTemplate(Long tid);
}
