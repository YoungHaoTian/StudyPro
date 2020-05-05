package com.cdut.studypro.beans;

import com.cdut.studypro.validates.common.*;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class OnlineTaskQuestion {
    private Integer id;

    @NotEmpty(message = "题目不能为空", groups = {Validate1.class})
    private String title;

    @NotEmpty(message = "选项A不能为空", groups = {Validate2.class})
    private String itemA;

    @NotEmpty(message = "选项B不能为空", groups = {Validate3.class})
    private String itemB;

    @NotEmpty(message = "选项C不能为空", groups = {Validate4.class})
    private String itemC;

    @NotEmpty(message = "选项D不能为空", groups = {Validate5.class})
    private String itemD;

    @NotEmpty(message = "请录入答案", groups = {Validate6.class})
    @Pattern(regexp = "[A-D]",message = "请录入合法的答案",groups = {Validate7.class})
    private String answer;

    private Integer onlineTaskId;
    @NotNull(message = "请录入分值", groups = {Validate8.class})
    @Range(min = 1, max = 10, message = "分值范围是1-10", groups = {Validate9.class})
    private Integer score;

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

    public String getItemA() {
        return itemA;
    }

    public void setItemA(String itemA) {
        this.itemA = itemA == null ? null : itemA.trim();
    }

    public String getItemB() {
        return itemB;
    }

    public void setItemB(String itemB) {
        this.itemB = itemB == null ? null : itemB.trim();
    }

    public String getItemC() {
        return itemC;
    }

    public void setItemC(String itemC) {
        this.itemC = itemC == null ? null : itemC.trim();
    }

    public String getItemD() {
        return itemD;
    }

    public void setItemD(String itemD) {
        this.itemD = itemD == null ? null : itemD.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Integer getOnlineTaskId() {
        return onlineTaskId;
    }

    public void setOnlineTaskId(Integer onlineTaskId) {
        this.onlineTaskId = onlineTaskId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "TaskQuestion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", itemA='" + itemA + '\'' +
                ", itemB='" + itemB + '\'' +
                ", itemC='" + itemC + '\'' +
                ", itemD='" + itemD + '\'' +
                ", answer='" + answer + '\'' +
                ", onlineTaskId=" + onlineTaskId +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlineTaskQuestion that = (OnlineTaskQuestion) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(itemA, that.itemA) &&
                Objects.equals(itemB, that.itemB) &&
                Objects.equals(itemC, that.itemC) &&
                Objects.equals(itemD, that.itemD) &&
                Objects.equals(answer, that.answer) &&
                Objects.equals(onlineTaskId, that.onlineTaskId) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, itemA, itemB, itemC, itemD, answer, onlineTaskId, score);
    }
}