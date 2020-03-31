package com.cdut.studypro.beans;

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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", itemA=").append(itemA);
        sb.append(", itemB=").append(itemB);
        sb.append(", itemC=").append(itemC);
        sb.append(", itemD=").append(itemD);
        sb.append(", answer=").append(answer);
        sb.append(", taskId=").append(taskId);
        sb.append(", score=").append(score);
        sb.append("]");
        return sb.toString();
    }
}