package com.cdut.studypro.beans;

import java.util.Date;

public class StudentOfflineTask {
    private Integer id;

    private Integer studentId;

    private Integer offlineTaskId;

    private String path;

    private Date recordTime;

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

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getOfflineTaskId() {
        return offlineTaskId;
    }

    public void setOfflineTaskId(Integer offlineTaskId) {
        this.offlineTaskId = offlineTaskId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", studentId=").append(studentId);
        sb.append(", offlineTaskId=").append(offlineTaskId);
        sb.append(", path=").append(path);
        sb.append(", recordTime=").append(recordTime);
        sb.append("]");
        return sb.toString();
    }
}