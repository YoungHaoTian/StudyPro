package com.cdut.studypro.beans;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Task {
    private Integer id;

    private Integer chapterId;

    private Integer teacherId;

    private String title;

    private Date recordTime;

    private List<TaskQuestion> questions;

    public List<TaskQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TaskQuestion> questions) {
        this.questions = questions;
    }

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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", chapterId=" + chapterId +
                ", teacherId=" + teacherId +
                ", title='" + title + '\'' +
                ", recordTime=" + recordTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(chapterId, task.chapterId) &&
                Objects.equals(teacherId, task.teacherId) &&
                Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chapterId, teacherId, title);
    }
}