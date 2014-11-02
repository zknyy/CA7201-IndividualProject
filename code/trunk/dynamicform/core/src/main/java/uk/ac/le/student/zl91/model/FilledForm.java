package uk.ac.le.student.zl91.model;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * The class is the entity of a filled form whose name of table is filled_form.
 * The new object/entity of filled form is created when a user fill a template.
 * There a filled form entity relates to the entities of user, template and responses.
 * <p/>
 * Created on 2014/3/28.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
@Entity
@Table(name = "filled_form")
@Indexed
@XmlRootElement
public class FilledForm extends BaseObject {

    //the primary key of the table is ffid.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    @DocumentId
    private Long ffid;

    //a user may have may filled forms.
    @ManyToOne//filledForm is the entity object in the template class
    @JoinColumn(name = "uid",nullable = false)// unique = false(default)
    private User user;

    // a user may fill a template many times.
    @ManyToOne//filledForm is the entity object in the template class
    @JoinColumn(name = "tid",nullable = false)// unique = false(default)
    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    //the start time shows when a template is filled.
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "start_time", columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;

    //the updated time shows when a filled form is updated.
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "updated_time", columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedTime;

    //a filled form includes many responses, each of them is a input on a answer.
    @OneToMany(mappedBy = "filledForm")//filledForm is the entity object in the Response class
    private Set<Response> responseSet = new HashSet<Response>(0);

    public Set<Response> getResponseSet() {
        return responseSet;
    }

    public void setResponseSet(Set<Response> responseSet) {
        this.responseSet = responseSet;
    }

    public Long getFfid() {
        return ffid;
    }

    public void setFfid(Long ffid) {
        this.ffid = ffid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilledForm that = (FilledForm) o;

        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
//        if (uid != null ? !uid.equals(that.uid) : that.uid != null) return false;//may have null exception, when it created
        if (updatedTime != null ? !updatedTime.equals(that.updatedTime) : that.updatedTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = createTime != null ? createTime.hashCode() : 0;
        result = 31 * result + (updatedTime != null ? updatedTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilledForm{" +
                "ffid=" + ffid +
//                ", uid=" + uid +//may have null exception, when it created
                ", createTime=" + createTime +
                ", updatedTime=" + updatedTime +
                ", responses size=" + responseSet.size() +
                '}';
    }

//    @Transient
//    private File cachedFile;
//
//    public File getCachedFile() {
//        return cachedFile;
//    }
//
//    public void setCachedFile(File cachedFile) {
//        this.cachedFile = cachedFile;
//    }
}
