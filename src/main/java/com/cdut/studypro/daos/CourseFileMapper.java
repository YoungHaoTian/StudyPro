package com.cdut.studypro.daos;

import com.cdut.studypro.beans.CourseFile;
import com.cdut.studypro.beans.CourseFileExample;
import java.util.List;

import com.cdut.studypro.beans.CourseVideo;
import org.apache.ibatis.annotations.Param;

public interface CourseFileMapper {
    long countByExample(CourseFileExample example);

    int deleteByExample(CourseFileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CourseFile record);

    int insertSelective(CourseFile record);

    List<CourseFile> selectByExample(CourseFileExample example);

    CourseFile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CourseFile record, @Param("example") CourseFileExample example);

    int updateByExample(@Param("record") CourseFile record, @Param("example") CourseFileExample example);

    int updateByPrimaryKeySelective(CourseFile record);

    int updateByPrimaryKey(CourseFile record);

    List<Integer> selectFileIdByExample(CourseFileExample example);

    List<CourseFile> selectByExampleWithCourseChapter(CourseFileExample example);
}