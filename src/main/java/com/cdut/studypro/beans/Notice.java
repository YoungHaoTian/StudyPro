package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

public class Notice {
    private Integer id;
    @NotEmpty(message = "公告标题不能为空", groups = {Validate1.class})
    @Length(max = 50, message = "公告标题请控制在50字以内", groups = {Validate2.class})
    private String title;

    private Date recordTime;
    @NotEmpty(message = "公告内容不能为空", groups = {Validate3.class})
    @Length(max = 300, message = "公告内容请控制在300字以内", groups = {Validate4.class})
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", recordTime=" + recordTime +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notice notice = (Notice) o;
        return Objects.equals(id, notice.id) &&
                Objects.equals(title, notice.title) &&
                Objects.equals(content, notice.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content);
    }
}