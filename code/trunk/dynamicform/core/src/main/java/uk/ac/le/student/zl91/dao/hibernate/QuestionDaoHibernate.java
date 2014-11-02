package uk.ac.le.student.zl91.dao.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;
import uk.ac.le.student.zl91.dao.QuestionDao;
import uk.ac.le.student.zl91.model.Question;

import java.util.List;

/**
 * The class is the implementation of QuestionDao.
 * The class extends GenericDaoHibernate which is a implementation for CRUD
 * functionalities of the entity of question.
 * The only implemented method is getList().
 * <p/>
 * Created on 2014/3/31.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */


@Repository("questionDao")
public class QuestionDaoHibernate extends GenericDaoHibernate<Question, Long> implements QuestionDao {

    public QuestionDaoHibernate() {
        super(Question.class);
    }


    @Override
    public List<Question> getList() {
        List<Question> list = getSession().createCriteria(Question.class).list();

        return list;
    }
}