package com.cdut.studypro.beans;

import java.util.Date;

public class Task {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task.id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task.teacher_id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    private Integer teacherId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task.course_id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    private Integer courseId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task.record_time
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    private Date recordTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task.id
     *
     * @return the value of task.id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task.id
     *
     * @param id the value for task.id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task.teacher_id
     *
     * @return the value of task.teacher_id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task.teacher_id
     *
     * @param teacherId the value for task.teacher_id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task.course_id
     *
     * @return the value of task.course_id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public Integer getCourseId() {
        return courseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task.course_id
     *
     * @param courseId the value for task.course_id
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task.record_time
     *
     * @return the value of task.record_time
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public Date getRecordTime() {
        return recordTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task.record_time
     *
     * @param recordTime the value for task.record_time
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task
     *
     * @mbg.generated Tue Mar 31 13:05:28 CST 2020
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", teacherId=").append(teacherId);
        sb.append(", courseId=").append(courseId);
        sb.append(", recordTime=").append(recordTime);
        sb.append("]");
        return sb.toString();
    }
}