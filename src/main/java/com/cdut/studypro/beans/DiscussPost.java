package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.Validate1;
import com.cdut.studypro.validates.common.Validate3;
import com.cdut.studypro.validates.common.Validate4;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class DiscussPost {
    private Integer id;

    private Integer discussId;

    private Integer studentId;

    private Date recordTime;

    private String content;

    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDiscussId() {
        return discussId;
    }

    public void setDiscussId(Integer discussId) {
        this.discussId = discussId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", discussId=" + discussId +
                ", studentId=" + studentId +
                ", recordTime=" + recordTime +
                ", content='" + content + '\'' +
                '}';
    }
}