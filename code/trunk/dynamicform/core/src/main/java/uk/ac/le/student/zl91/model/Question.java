package uk.ac.le.student.zl91.model;

import org.appfuse.model.BaseObject;
import org.hibernate.annotations.Cascade;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The class is the entity of a question mapping to the table with the same name.
 * There is a named query. It is for getting the available questions for being
 * assigned to a specific template. an available question is one of the case
 * as the following:
 *    <li>a question is not assigned to a template</li>
 *    <li>a question is assigned to a template, but the status of the template
 *    is EDITABLE</li>
 * This class implement the interface of Comparable by implementing the method
 * of compareTo() which offers sorting those questions according to order number
 * increasely.
 * <p/>
 * Created on 2014/3/28.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
@Entity
@Table(name = "question")
@Indexed
@XmlRootElement
@NamedQueries(
        {
                @NamedQuery(name = "Question.findAvailableQuestion",
                        query = "select question      from Question question "
                                +"                     left join question.template template "
                                +"                     where   (template.status is null or template.status = 'EDITABLE')"
                )
        }
)
public class Question extends BaseObject  implements Comparable<Question>{
    //the primary key is qid
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long qid;

    //a template which the question is assigned to.
    //the foreign key from template.tid
    @ManyToOne
    @JoinColumn(name = "tid")
    private Template template;

    //title
    @Column(length = 1000)
    private String title;

    //the number of order which defines the order of questions in a template
    @Column(name = "order_number",columnDefinition = "int NOT NULL DEFAULT 0")
    private Integer orderNumber=0;

    //the time when the question created.
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "create_time",columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime = new Date(System.currentTimeMillis());

    //one question may have many response
    @OneToMany(mappedBy = "question")//question is the entity object in the Response class
    @OrderBy("rid")
    private Set<Response> responseSet=new HashSet<Response>(0);

    //a question may have many answers.
    @OneToMany(mappedBy = "question")//question is the entity object in the Answer class
    @OrderBy("orderNumber")
    private Set<Answer> answerSet=new HashSet<Answer>(0);

    //a question may have may sub-questions.
    @OneToMany
    @JoinTable(name = "question_group",
            joinColumns = @JoinColumn(name = "parent_qid",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "child_qid",nullable = false)
    )
    @OrderBy("orderNumber")
    private Set<Question> questionSet=new HashSet<Question>(0);

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<Response> getResponseSet() {
        return responseSet;
    }

    public void setResponseSet(Set<Response> responseSet) {
        this.responseSet = responseSet;
    }

    public Set<Answer> getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(Set<Answer> answerSet) {
        this.answerSet = answerSet;
    }

    public Set<Question> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Set<Question> questionSet) {
        this.questionSet = questionSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

//        if (answerSet != null ? !answerSet.equals(question.answerSet) : question.answerSet != null) return false;
        if (createTime != null ? !createTime.equals(question.createTime) : question.createTime != null) return false;
        if (orderNumber != null ? !orderNumber.equals(question.orderNumber) : question.orderNumber != null)
            return false;
//        if (questionSet != null ? !questionSet.equals(question.questionSet) : question.questionSet != null)
//            return false;
//        if (responseSet != null ? !responseSet.equals(question.responseSet) : question.responseSet != null)
//            return false;
        if (template != null ? !template.equals(question.template) : question.template != null) return false;
        if (title != null ? !title.equals(question.title) : question.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = template != null ? template.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
//        result = 31 * result + (responseSet != null ? responseSet.hashCode() : 0);
//        result = 31 * result + (answerSet != null ? answerSet.hashCode() : 0);
//        result = 31 * result + (questionSet != null ? questionSet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "qid=" + qid +
                ", template=" + template +
                ", title='" + title + '\'' +
                ", orderNumber=" + orderNumber +
                ", createTime=" + createTime +
                ", responses size=" + responseSet.size() +
                ", answers size=" + answerSet.size() +
                ", questions size=" + questionSet.size() +
                '}';
    }

    @Override
    public int compareTo(Question q) {
        return this.orderNumber-q.orderNumber;
    }
}
