package uk.ac.le.student.zl91.dao.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;
import uk.ac.le.student.zl91.dao.AnswerDao;
import uk.ac.le.student.zl91.model.Answer;
import uk.ac.le.student.zl91.model.Template;

import java.util.List;

/**
 * The class is the implementation of AnswerDao.
 * The class extends GenericDaoHibernate which is a implementation for CRUD
 * functionalities of the entity of answer.
 * The only implemented method is getList().
 * <p/>
 * Created on 2014/3/29.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
@Repository("answerDao")
public class AnswerDaoHibernate extends GenericDaoHibernate<Answer, Long> implements AnswerDao {

    public AnswerDaoHibernate() {
        super(Answer.class);
    }


    @Override
    public List<Answer> getList() {
        List<Answer> list = getSession().createCriteria(Answer.class).list();

        Answer answer = list.get(0);
        Template template = answer.getQuestion().getTemplate();

        log.debug(answer+" [Q"+answer.getQuestion().getQid()+"]:"
        +answer.getQuestion().getTitle()
        +" [T"+template.getTid()+"]:"+template.getName()+" Status:"+template.getStatus());

        return list;
    }
}
