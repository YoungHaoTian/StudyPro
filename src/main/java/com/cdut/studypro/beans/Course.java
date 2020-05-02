package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

public class Course {
    private Integer id;
    @NotEmpty(message = "课程名称不能为空", groups = {Validate1.class})
    @Length(max = 30, message = "课程名称请控制在30字以内", groups = {Validate1.class})
    private String name;

    @NotNull(message = "请选择所属学院", groups = {Validate5.class})
    @Min(value = 1, message = "请选择所属学院", groups = {Validate6.class})
    private Integer collegeId;

    @NotEmpty(message = "课程编号不能为空", groups = {Validate3.class})
    @Pattern(regexp = "[a-zA-Z0-9]{6,18}", message = "课程编号由6-18位字母和数字组成，请重新输入", groups = {Validate4.class})
    private String number;

    @NotNull(message = "请选择授课教师", groups = {Validate7.class})
    @Min(value = 1, message = "请选择授课教师", groups = {Validate8.class})
    private Integer teacherId;

    private College college;

    private Teacher teacher;

    @NotEmpty(message = "课程介绍不能为空", groups = {Validate9.class})
    @Length(max = 100, message = "课程介绍请控制在100字以内", groups = {Validate10.class})
    private String intro;

    private List<CourseChapter> chapters;

    public List<CourseChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<CourseChapter> chapters) {
        this.chapters = chapters;
    }

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
                ", chapters=" + chapters +
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