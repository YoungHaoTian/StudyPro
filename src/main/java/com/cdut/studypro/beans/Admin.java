package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class Admin {
    private Integer id;
    @NotEmpty(message = "姓名不能为空", groups = {Validate1.class})
    @Pattern(regexp = "[\\u4e00-\\u9fa5]{2,5}", message = "姓名为2-5个中文字符，请重新输入", groups = {Validate2.class})
    private String name;

    @NotEmpty(message = "登录账户不能为空", groups = {Validate3.class})
    @Pattern(regexp = "[a-zA-Z0-9_-]{12,18}", message = "账户是12-18位，且由数字、字母、下划线组成", groups = {Validate4.class})
    private String account;

    @NotEmpty(message = "登录密码不能为空", groups = {Validate5.class})
    @Pattern(regexp = "[a-zA-Z0-9]{6,18}", message = "登录密码只能由字母和数字组成，长度6-18位，请重新输入", groups = {Validate6.class})
    private String password;

    @NotEmpty(message = "邮箱地址不能为空", groups = {Validate9.class})
    @Email(message = "邮箱地址格式错误，请重新输入", groups = {Validate10.class})
    private String email;

    @NotEmpty(message = "联系电话不能为空", groups = {Validate7.class})
    @Pattern(regexp = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}", message = "联系电话格式不正确，请重新输入", groups = {Validate8.class})
    private String telephone;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id) &&
                Objects.equals(name, admin.name) &&
                Objects.equals(account, admin.account) &&
                Objects.equals(password, admin.password) &&
                Objects.equals(email, admin.email) &&
                Objects.equals(telephone, admin.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, account, password, email, telephone);
    }
}