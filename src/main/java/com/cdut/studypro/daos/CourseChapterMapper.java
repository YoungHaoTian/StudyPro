package com.cdut.studypro.daos;

import com.cdut.studypro.beans.CourseChapter;
import com.cdut.studypro.beans.CourseChapterExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CourseChapterMapper {
    long countByExample(CourseChapterExample example);

    int deleteByExample(CourseChapterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CourseChapter record);

    int insertSelective(CourseChapter record);

    List<CourseChapter> selectByExampleWithBLOBs(CourseChapterExample example);

    List<CourseChapter> selectByExample(CourseChapterExample example);

    CourseChapter selectByPrimaryKey(Integer id);

    CourseChapter selectByPrimaryKeyWithIdTitleAndCourse(Integer id);

    int updateByExampleSelective(@Param("record") CourseChapter record, @Param("example") CourseChapterExample example);

    int updateByExampleWithBLOBs(@Param("record") CourseChapter record, @Param("example") CourseChapterExample example);

    int updateByExample(@Param("record") CourseChapter record, @Param("example") CourseChapterExample example);

    int updateByPrimaryKeySelective(CourseChapter record);

    int updateByPrimaryKeyWithBLOBs(CourseChapter record);

    int updateByPrimaryKey(CourseChapter record);

    CourseChapter selectByCourseIdWithIdAndTitle(Integer id);

    List<Integer> selectChapterIdByExample(CourseChapterExample courseChapterExample);
}