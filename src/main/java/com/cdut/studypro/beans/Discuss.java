package com.cdut.studypro.beans;

import java.util.Date;
import java.util.Objects;

public class Discuss {
    private Integer id;

    private String title;

    private Date recordTime;

    private Integer courseId;

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