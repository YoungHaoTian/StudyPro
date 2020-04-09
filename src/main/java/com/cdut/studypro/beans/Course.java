package com.cdut.studypro.beans;

import java.util.Objects;

public class Course {
    private Integer id;

    private String name;

    private Integer collegeId;

    private String number;

    private Integer teacherId;

    private College college;

    private Teacher teacher;

    private String intro;

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", collegeId=" + collegeId +
                ", number='" + number + '\'' +
                ", teacherId=" + teacherId +
                ", college=" + college +
                ", teacher=" + teacher +
                ", intro='" + intro + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) &&
                Objects.equals(name, course.name) &&
                Objects.equals(collegeId, course.collegeId) &&
                Objects.equals(number, course.number) &&
                Objects.equals(teacherId, course.teacherId) &&
                Objects.equals(intro, course.intro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, collegeId, number, teacherId, intro);
    }
}