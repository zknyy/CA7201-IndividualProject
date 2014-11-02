package uk.ac.le.student.zl91.model;

import org.appfuse.model.BaseObject;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Set;

/**
 * The class is the entity of answer mapping to the table with the same name.
 * There are two named query. One is for getting the questions by a specific
 * question. The other one is for getting the available answers for being
 * assigned to a specific question. an available answer is one of the case
 * as the following:
 *    <li>a answer is not assigned to a question</li>
 *    <li>a answer is assigned to a question, but the question is not
 *    assigned to a template</li>
 *    <li>a answer is assigned to a question, and the question is assigned
 *    to a template, but the status of the template is EDITABLE</li>
 * This class implement the interface of Comparable by implementing the method
 * of compareTo() which offers sorting those answers according to order number
 * increasely.
 * <p/>
 * Created on 2014/3/28.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
@Indexed
@XmlRootElement
@Entity
@Table(name = "answer")
@NamedQueries(
        {
                @NamedQuery(name = "Answer.findByQuestion", query = "SELECT answer FROM Answer answer WHERE answer.question.qid = :qid"),
                @NamedQuery(name = "Answer.findAvailableAnswer",
                        query = "select answer      from Answer answer "
                                + "                     left join answer.question question "
                                + "                     left join question.template template "
                                + "                     where   (template.status is null or template.status = 'EDITABLE')"
                )
        }
)
public class Answer extends BaseObject implements Comparable<Answer>{
    //the primary key of the table is aid
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aid;

//    the foreign key from question.qid
    @ManyToOne//(optional = false)//filledForm is the entity object in the Response class
    @JoinColumn(name = "qid")// unique = false(default)
        private Question question;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    //there are 6 types of an answer: RADIO, CHECKBOX, TEXT, TEXTAREA, FILE, LABEL
    //the LABEL is for the needs of layout.
    //the default type is RADIO.
    @Column(name = "type",
            columnDefinition="varchar(15) NOT NULL DEFAULT 'RADIO'")
    @Enumerated(EnumType.STRING)
    private AnswerType type=AnswerType.RADIO;

    //the title of the answer which show before/after the element of the HTML.
    //it works for all of the types expect FILE.
    @Column(length = 1000)
    private String title;

    //the hint shows as text in the element of the HTML.
    //it works on the types of TEXT and TEXTAREA.
    @Column(length = 1000)
    private String hint;

    //the number of order on the answer. it defines the order of answers showing in a question.
    @Column(name = "order_number",columnDefinition = "int NOT NULL DEFAULT 0")
    private Integer orderNumber=0;

    //when the answer is created, which is a good identity for distingrish the objects of answer
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time",columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime = new Date(System.currentTimeMillis());

    //the default value of a element of the HTML.
    //it works on the types of RADIO, CHECKBOX, TEXT and TEXTAREA.
    @Column(name = "default_value", length = 1000)
    private String defaultValue;

    //for the layout each element of HTML on a page, the width should between 1 and 12
    @Column(name = "width",columnDefinition = "int NOT NULL DEFAULT 12")
    private Integer width=12;

    //one answer may have many responses
    @OneToMany(mappedBy = "answer")//answer is the entity object in the Response class
    private Set<Response> responseSet;

    //one answer may control a question
    //the foreign key from question.qid
    @OneToOne
    @JoinColumn(name = "referred_qid", nullable = true)
    private Question referredQuestion;

    public Question getReferredQuestion() {
        return referredQuestion;
    }

    public void setReferredQuestion(Question referredQuestion) {
        this.referredQuestion = referredQuestion;
    }


    //one answer may control another answer
//    foreign key from answer.aid
    @OneToOne
    @JoinColumn(name = "referred_aid")
    private Answer referredAnswer;
    public Answer getReferredAnswer() {
        return referredAnswer;
    }
    public void setReferredAnswer(Answer referredAnswer) {
        this.referredAnswer = referredAnswer;
    }

    public Set<Response> getResponseSet() {
        return responseSet;
    }

    public void setResponseSet(Set<Response> responseSet) {
        this.responseSet = responseSet;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public AnswerType getType() {
        return type;
    }

    public void setType(AnswerType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (createTime != null ? !createTime.equals(answer.createTime) : answer.createTime != null) return false;
        if (defaultValue != null ? !defaultValue.equals(answer.defaultValue) : answer.defaultValue != null)
            return false;
        if (hint != null ? !hint.equals(answer.hint) : answer.hint != null) return false;
        if (orderNumber != null ? !orderNumber.equals(answer.orderNumber) : answer.orderNumber != null) return false;
        if (width != null ? !width.equals(answer.width) : answer.width != null) return false;
        if (title != null ? !title.equals(answer.title) : answer.title != null) return false;
        if (type != answer.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (hint != null ? hint.hashCode() : 0);
        result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "aid=" + aid +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", hint='" + hint + '\'' +
                ", orderNumber=" + orderNumber +
                ", createTime=" + createTime +
                ", defaultValue='" + defaultValue + '\'' +
                ", width=" + width +
                '}';
    }


    @Override
    public int compareTo(Answer a) {
        return this.orderNumber-a.orderNumber;
    }

    //the content is for temporarily keep the value of content from response entity.
    @Transient
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //the content is for temporarily keep the response id of response entity.
    @Transient
    private long rid;//for downloading file

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }
}
