package com.cdut.studypro.daos;

import com.cdut.studypro.beans.StudentTask;
import com.cdut.studypro.beans.StudentTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudentTaskMapper {
    long countByExample(StudentTaskExample example);

    int deleteByExample(StudentTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentTask record);

    int insertSelective(StudentTask record);

    List<StudentTask> selectByExample(StudentTaskExample example);

    StudentTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentTask record, @Param("example") StudentTaskExample example);

    int updateByExample(@Param("record") StudentTask record, @Param("example") StudentTaskExample example);

    int updateByPrimaryKeySelective(StudentTask record);

    int updateByPrimaryKey(StudentTask record);
}