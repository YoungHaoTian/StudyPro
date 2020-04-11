package com.cdut.studypro.beans;

import java.util.Date;
import java.util.List;

public class CourseChapter {
    private Integer id;

    private String title;

    private Integer courseId;

    private Date recordTime;

    private String content;

    private Course course;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    private List<CourseVideo> videos;

    private List<CourseFile> files;

    public List<CourseVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<CourseVideo> videos) {
        this.videos = videos;
    }

    public List<CourseFile> getFiles() {
        return files;
    }

    public void setFiles(List<CourseFile> files) {
        this.files = files;
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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
        return "CourseChapter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", courseId=" + courseId +
                ", recordTime=" + recordTime +
                ", content='" + content + '\'' +
                '}';
    }
}