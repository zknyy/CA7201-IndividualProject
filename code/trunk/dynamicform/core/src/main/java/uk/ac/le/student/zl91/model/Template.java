package uk.ac.le.student.zl91.model;

/**
 * The class is the entity of a template mapping to the table with the same name.
 * This entity represent the template of form.
 * There is a named query. It is for getting the available templates for being
 * assigned to a specific question. an available template must be EDITABLE.
 * <p/>
 * Created on 2014/3/28.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */

import org.appfuse.model.BaseObject;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "template")
@Indexed
@XmlRootElement
@NamedQueries(
        {
                @NamedQuery(name = "Template.findAvailableTemplate",
                        query = "select template      from Template template "
                                + "                     where   template.status = 'EDITABLE'"
                )
        }
)
public class Template extends BaseObject {
    //the primary key of the table, tid
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tid;

    //the time when the template is created
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime = new Date(System.currentTimeMillis());

    @Column(nullable = false, length = 1000)
    private String name;

    //the status of template, it could be EDITABLE, LIVING, DISCARDED.
    @Column(name = "status",
            columnDefinition="varchar(10) NOT NULL DEFAULT 'EDITABLE'")
    @Enumerated(EnumType.STRING)
    private TemplateStatus status = TemplateStatus.EDITABLE;

    //a template has may questions
    @OrderBy(value = "qid")
    @OneToMany(mappedBy = "template")//template is the entity object in the Question class
    private Set<Question> questionSet=new HashSet<Question>(0);

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
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

        Template template = (Template) o;

        if (createTime != null ? !createTime.equals(template.createTime) : template.createTime != null) return false;
        if (name != null ? !name.equals(template.name) : template.name != null) return false;
//        if (questionSet != null ? !questionSet.equals(template.questionSet) : template.questionSet != null)
//            return false;
        if (status != template.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = createTime != null ? createTime.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
//        result = 31 * result + (questionSet != null ? questionSet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Template{" +
                "tid=" + tid +
                ", createTime=" + createTime +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", questions size=" + questionSet.size() +
                '}';
    }
}
