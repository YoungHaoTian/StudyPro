package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Objects;

public class Teacher {
    private Integer id;

    @NotEmpty(message = "教师姓名不能为空", groups = {Validate1.class})
    @Pattern(regexp = "[\\u4e00-\\u9fa5]{2,5}", message = "姓名为2-5个中文字符，请重新输入", groups = {Validate2.class})
    private String name;

    @NotNull(message = "请选择性别", groups = {Validate11.class})
    @Range(min = 0, max = 1, message = "请选择正确的性别", groups = {Validate12.class})
    private Integer gender;

    @NotNull(message = "请选择所属学院", groups = {Validate5.class})
    @Min(value = 1, message = "请选择所属学院", groups = {Validate6.class})
    private Integer collegeId;

    @NotEmpty(message = "联系电话不能为空", groups = {Validate7.class})
    @Pattern(regexp = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}", message = "联系电话格式不正确，请重新输入", groups = {Validate8.class})
    private String telephone;

    @NotEmpty(message = "身份证号码不能为空", groups = {Validate9.class})
    @Pattern(regexp = "[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]", message = "身份证号码格式不正确，请重新输入", groups = {Validate10.class})
    private String idCardNo;

    @NotEmpty(message = "登录账户不能为空", groups = {Validate13.class})
    @Pattern(regexp = "[a-zA-Z0-9_-]{12,18}", message = "账户是12-18位，且由数字、字母、下划线组成", groups = {Validate14.class})
    private String account;

    @NotEmpty(message = "登录密码不能为空", groups = {Validate15.class})
    @Pattern(regexp = "[a-zA-Z0-9]{6,18}", message = "登录密码只能由字母和数字组成，长度6-18位，请重新输入", groups = {Validate16.class})
    private String password;

    @NotEmpty(message = "教师编号不能为空", groups = {Validate3.class})
    @Pattern(regexp = "\\d{12,18}", message = "编号是由12-18位纯数字组成，请重新输入", groups = {Validate4.class})
    private String number;

    @NotEmpty(message = "邮箱地址不能为空", groups = {Validate17.class})
    @Email(message = "邮箱地址格式错误，请重新输入", groups = {Validate18.class})
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