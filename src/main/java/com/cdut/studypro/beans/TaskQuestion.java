package com.cdut.studypro.beans;

import java.util.Objects;

public class TaskQuestion {
    private Integer id;

    private String title;

    private String itemA;

    private String itemB;

    private String itemC;

    private String itemD;

    private String answer;

    private Integer taskId;

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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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
                ", taskId=" + taskId +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskQuestion that = (TaskQuestion) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(itemA, that.itemA) &&
                Objects.equals(itemB, that.itemB) &&
                Objects.equals(itemC, that.itemC) &&
                Objects.equals(itemD, that.itemD) &&
                Objects.equals(answer, that.answer) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, itemA, itemB, itemC, itemD, answer, taskId, score);
    }
}