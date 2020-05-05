package com.cdut.studypro.beans;

import java.util.Date;

public class StudentOnlineTask {
    private Integer id;

    private Integer studentId;

    private Integer onlineTaskId;

    private Integer score;

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

    public Integer getOnlineTaskId() {
        return onlineTaskId;
    }

    public void setOnlineTaskId(Integer taskId) {
        this.onlineTaskId = taskId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "StudentOnlineTask{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", onlineTaskId=" + onlineTaskId +
                ", score=" + score +
                ", recordTime=" + recordTime +
                '}';
    }
}