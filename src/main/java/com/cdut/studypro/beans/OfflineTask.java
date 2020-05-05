package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.Validate1;
import com.cdut.studypro.validates.common.Validate2;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

public class OfflineTask {
    private Integer id;

    private Integer chapterId;

    private Integer teacherId;

    private Date recordTime;
    @NotEmpty(message = "作业标题不能为空", groups = {Validate1.class})
    @Length(max = 300, message = "作业标题请控制在300字以内", groups = {Validate2.class})
    private String title;

    private CourseChapter courseChapter;

    public CourseChapter getCourseChapter() {
        return courseChapter;
    }

    public void setCourseChapter(CourseChapter courseChapter) {
        this.courseChapter = courseChapter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    @Override
    public String toString() {
        return "OfflineTask{" +
                "id=" + id +
                ", chapterId=" + chapterId +
                ", teacherId=" + teacherId +
                ", recordTime=" + recordTime +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfflineTask that = (OfflineTask) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(chapterId, that.chapterId) &&
                Objects.equals(teacherId, that.teacherId) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chapterId, teacherId, title);
    }
}