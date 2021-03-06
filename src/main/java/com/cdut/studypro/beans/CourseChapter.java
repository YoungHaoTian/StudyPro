package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.Validate1;
import com.cdut.studypro.validates.common.Validate2;
import com.cdut.studypro.validates.common.Validate3;
import com.cdut.studypro.validates.common.Validate4;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CourseChapter {
    private Integer id;

    @NotEmpty(message = "章节标题不能为空", groups = {Validate1.class})
    @Length(max = 50, message = "章节标题请控制在50字以内", groups = {Validate2.class})
    private String title;

    private Integer courseId;

    private Date recordTime;

    @NotEmpty(message = "章节内容不能为空", groups = {Validate3.class})
    @Length(max = 300, message = "章节内容请控制在300字以内", groups = {Validate4.class})
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

    private List<OnlineTask> onlineTasks;

    public List<OnlineTask> getOnlineTasks() {
        return onlineTasks;
    }

    public void setOnlineTasks(List<OnlineTask> onlineTasks) {
        this.onlineTasks = onlineTasks;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseChapter that = (CourseChapter) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(courseId, that.courseId) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, courseId, content);
    }
}