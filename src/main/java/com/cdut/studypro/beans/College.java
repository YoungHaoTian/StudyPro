package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.Validate1;
import com.cdut.studypro.validates.common.Validate2;
import com.cdut.studypro.validates.common.Validate3;
import com.cdut.studypro.validates.common.Validate4;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class College {
    private Integer id;

    @NotEmpty(message = "学院名称不能为空", groups = {Validate1.class})
    @Length(message = "学院名称请控制在30字以内", groups = {Validate2.class})
    private String name;

    @NotEmpty(message = "学院介绍不能为空", groups = {Validate3.class})
    @Length(max = 100, message = "学院介绍请控制在100字之内", groups = {Validate4.class})
    private String intro;

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    @Override
    public String toString() {
        return "College{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        College college = (College) o;
        return Objects.equals(id, college.id) &&
                Objects.equals(name, college.name) &&
                Objects.equals(intro, college.intro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, intro);
    }
}