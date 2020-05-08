package com.cdut.studypro.daos;

import com.cdut.studypro.beans.OnlineTask;
import com.cdut.studypro.beans.OnlineTaskExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OnlineTaskMapper {
    long countByExample(OnlineTaskExample example);

    int deleteByExample(OnlineTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OnlineTask record);

    int insertSelective(OnlineTask record);

    List<OnlineTask> selectByExample(OnlineTaskExample example);

    OnlineTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OnlineTask record, @Param("example") OnlineTaskExample example);

    int updateByExample(@Param("record") OnlineTask record, @Param("example") OnlineTaskExample example);

    int updateByPrimaryKeySelective(OnlineTask record);

    int updateByPrimaryKey(OnlineTask record);

    List<OnlineTask> selectByExampleWithCourseAndCollegeAndChapter(OnlineTaskExample taskExample);

    List<OnlineTask> selectByExampleWithCourseAndTeacherAndChapter(OnlineTaskExample taskExample);

    List<OnlineTask> selectByExampleWithChapter(OnlineTaskExample taskExample);

    OnlineTask selectByPrimaryKeyWithCourseAndChapter(Integer id);

    List<Integer> selectOnlineTaskIdsByExample(OnlineTaskExample taskExample);


}