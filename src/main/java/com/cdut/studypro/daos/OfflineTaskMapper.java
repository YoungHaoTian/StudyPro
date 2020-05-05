package com.cdut.studypro.daos;

import com.cdut.studypro.beans.OfflineTask;
import com.cdut.studypro.beans.OfflineTaskExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OfflineTaskMapper {
    long countByExample(OfflineTaskExample example);

    int deleteByExample(OfflineTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OfflineTask record);

    int insertSelective(OfflineTask record);

    List<OfflineTask> selectByExampleWithBLOBs(OfflineTaskExample example);

    List<OfflineTask> selectByExample(OfflineTaskExample example);

    OfflineTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OfflineTask record, @Param("example") OfflineTaskExample example);

    int updateByExampleWithBLOBs(@Param("record") OfflineTask record, @Param("example") OfflineTaskExample example);

    int updateByExample(@Param("record") OfflineTask record, @Param("example") OfflineTaskExample example);

    int updateByPrimaryKeySelective(OfflineTask record);

    int updateByPrimaryKeyWithBLOBs(OfflineTask record);

    int updateByPrimaryKey(OfflineTask record);

    List<Integer> selectOfflineTaskIdsByExample(OfflineTaskExample offlineTaskExample);

    OfflineTask selectByPrimaryKeyWithCourseAndChapter(Integer id);

    List<OfflineTask> selectByExampleWithCourseAndCollegeAndChapter(OfflineTaskExample taskExample);
}