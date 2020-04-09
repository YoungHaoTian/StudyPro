package com.cdut.studypro.beans;

import java.util.List;
import java.util.Objects;

public class Teacher {
    private Integer id;

    private String name;

    private Integer gender;

    private Integer collegeId;

    private String telephone;

    private String idCardNo;

    private String account;

    private String password;

    private String number;

    private String email;

    private College college;

    private List<Course> courses;

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", collegeId=" + collegeId +
                ", telephone='" + telephone + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", number='" + number + '\'' +
                ", email='" + email + '\'' +
                ", college=" + college +
                ", courses=" + courses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) &&
                Objects.equals(name, teacher.name) &&
                Objects.equals(gender, teacher.gender) &&
                Objects.equals(collegeId, teacher.collegeId) &&
                Objects.equals(telephone, teacher.telephone) &&
                Objects.equals(idCardNo, teacher.idCardNo) &&
                Objects.equals(account, teacher.account) &&
                Objects.equals(password, teacher.password) &&
                Objects.equals(number, teacher.number) &&
                Objects.equals(email, teacher.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, collegeId, telephone, idCardNo, account, password, number, email);
    }
}