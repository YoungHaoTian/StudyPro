package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

public class Discuss {
    private Integer id;

    @NotEmpty(message = "讨论标题不能为空", groups = {Validate3.class})
    @Length(max = 50, message = "讨论标题请控制在50字以内", groups = {Validate4.class})
    private String title;

    private Date recordTime;

    @NotNull(message = "请选择所属课程", groups = {Validate1.class})
    @Min(value = 1, message = "请选择所属课程", groups = {Validate2.class})
    private Integer courseId;

    @NotEmpty(message = "讨论内容不能为空", groups = {Validate5.class})
    @Length(max = 300, message = "讨论内容请控制在300字以内", groups = {Validate6.class})
    private String content;

    private Course course;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        return "Discuss{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", recordTime=" + recordTime +
                ", courseId=" + courseId +
                ", content='" + content + '\'' +
                ", course=" + course +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discuss discuss = (Discuss) o;
        return Objects.equals(id, discuss.id) &&
                Objects.equals(title, discuss.title) &&
                Objects.equals(courseId, discuss.courseId) &&
                Objects.equals(content, discuss.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, courseId, content);
    }
}