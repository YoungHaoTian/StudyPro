package com.cdut.studypro.daos;

import com.cdut.studypro.beans.Course;
import com.cdut.studypro.beans.CourseExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CourseMapper {
    long countByExample(CourseExample example);

    int deleteByExample(CourseExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Course record);

    int insertSelective(Course record);

    List<Course> selectByExampleWithBLOBs(CourseExample example);

    List<Course> selectByExample(CourseExample example);

    Course selectByPrimaryKey(Integer id);

    Course selectByPrimaryKeyWithoutBLOBs(Integer id);

    Course selectByPrimaryKeyWithIdNameAndCollege(Integer id);

    Course selectByPrimaryKeyWithIdNameAndTeacher(Integer id);

    Course selectByPrimaryKeyWithIdName(Integer id);

    int updateByExampleSelective(@Param("record") Course record, @Param("example") CourseExample example);

    int updateByExampleWithBLOBs(@Param("record") Course record, @Param("example") CourseExample example);

    int updateByExample(@Param("record") Course record, @Param("example") CourseExample example);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKeyWithBLOBs(Course record);

    int updateByPrimaryKey(Course record);

    //根据teacher_id查找课程
    List<Course> selectByTeacherIdWithIdAndNameWithCollege(Integer id);

    //带学院信息和教师信息的条件查询
    List<Course> selectByExampleWithBLOBsAndCollegeAndTeacher(CourseExample example);

    List<Course> selectByExampleWithBLOBsAndCollege(CourseExample courseExample);

    List<Course> selectByExampleWithBLOBsAndTeacher(CourseExample example);

    //带id、name、teacher的条件查询
    List<Course> selectByExampleWithCollegeAndTeacher(CourseExample courseExample);

    List<Course> selectByExampleWithCollege(CourseExample courseExample);

    //根据课程名称查询教师id
    List<Integer> selectTeacherIdByExample(CourseExample example);

    Course selectByPrimaryKeyWithoutTeacherAndCollege(Integer id);

    //根据条件查询课程id
    List<Integer> selectCourseIdByExample(CourseExample example);

    List<Integer> selectCollegeIdByExample(CourseExample example);

    //根据teacher的id值查找该teacher所教授的所有课程，包含章节信息
    List<Course> selectByExampleWithChapterAndCollege(CourseExample courseExample);

    List<Course> selectByExampleWithIdAndNameAndTeacher(CourseExample courseExample);

    List<Course> selectByExampleWithChapterAndVideo(CourseExample courseExample);

    List<Course> selectByExampleWithChapterAndFile(CourseExample courseExample);

    List<Course> selectByExampleWithChapterAndTeacher(CourseExample courseExample);

    Course selectByPrimaryKeyWithChapterAndTeacher(Integer id);

    List<String> selectNumberByExample(CourseExample courseExample);

    boolean insertBatch(@Param("courses") List<Course> courses);
}