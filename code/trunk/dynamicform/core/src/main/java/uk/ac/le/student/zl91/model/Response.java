package uk.ac.le.student.zl91.model;

import org.appfuse.model.BaseObject;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Blob;

/**
 * The class is the entity of a response mapping to the table with the same name.
 * This entity represent a result which user response on a answer.
 * <p/>
 * Created on 2014/3/28.
 * For the individual project(CO7201) with University of Leicester in 2014.
 *
 * @author Zhipeng Liang (ZL91)
 */
@Entity
@Table(name = "response")
@Indexed
@XmlRootElement
public class Response extends BaseObject {

    // the primary key is rid.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rid;

    //the foreign key ffid from filled form
    @ManyToOne//(optional = false)//filledForm is the entity object in the Response class
    @JoinColumn(name = "ffid",nullable = false)// unique = false(default)
    private FilledForm filledForm;

    //the foreign key qid from question
    @ManyToOne//(optional = false)//filledForm is the entity object in the Response class
    @JoinColumn(name = "qid",nullable = false)// unique = false(default)
    private Question question;

    //the foreign key aid from answer
    @ManyToOne//(optional = false)//filledForm is the entity object in the Response class
    @JoinColumn(name = "aid",nullable = false)// unique = false(default)
    private Answer answer;

    //the result which a user response to the answer
    @Column(nullable = true, length = 1000)
    private String content;

    //if a user upload a file, the database store the content of the file.
    @Lob
    @Column(name = "file_content", nullable = true, columnDefinition = "longblob")
    private byte[] fileContent;

    //the type of file which the user upload.
    @Column(nullable = true, length = 30)
    private String fileType;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public FilledForm getFilledForm() {
        return filledForm;
    }

    public void setFilledForm(FilledForm filledForm) {
        this.filledForm = filledForm;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (answer != null ? !answer.equals(response.answer) : response.answer != null) return false;
        if (content != null ? !content.equals(response.content) : response.content != null) return false;
        if (filledForm != null ? !filledForm.equals(response.filledForm) : response.filledForm != null) return false;
        if (question != null ? !question.equals(response.question) : response.question != null) return false;
        if (fileType != null ? !fileType.equals(response.fileType) : response.fileType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filledForm != null ? filledForm.hashCode() : 0;
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "rid=" + rid +
                ", filledForm=" + filledForm.getFfid() +
                ", question=" + question.getQid() +
                ", answer=" + answer.getAid() +
                ", content='" + content + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
