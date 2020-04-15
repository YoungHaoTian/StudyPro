package com.cdut.studypro.beans;

import java.util.Date;

public class CourseFile {
    private Integer id;

    private String path;

    private Integer chapterId;

    private Date recordTime;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "CourseFile{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", chapterId=" + chapterId +
                ", recordTime=" + recordTime +
                ", courseChapter=" + courseChapter +
                '}';
    }
}