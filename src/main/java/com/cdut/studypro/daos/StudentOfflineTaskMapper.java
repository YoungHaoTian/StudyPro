package com.cdut.studypro.daos;

import com.cdut.studypro.beans.StudentOfflineTask;
import com.cdut.studypro.beans.StudentOfflineTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudentOfflineTaskMapper {
    long countByExample(StudentOfflineTaskExample example);

    int deleteByExample(StudentOfflineTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentOfflineTask record);

    int insertSelective(StudentOfflineTask record);

    List<StudentOfflineTask> selectByExample(StudentOfflineTaskExample example);

    StudentOfflineTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentOfflineTask record, @Param("example") StudentOfflineTaskExample example);

    int updateByExample(@Param("record") StudentOfflineTask record, @Param("example") StudentOfflineTaskExample example);

    int updateByPrimaryKeySelective(StudentOfflineTask record);

    int updateByPrimaryKey(StudentOfflineTask record);

    List<String> selectPathByExample(StudentOfflineTaskExample studentOfflineTaskExample);

    List<StudentOfflineTask> selectByExampleWithStudent(StudentOfflineTaskExample studentOfflineTaskExample);
}