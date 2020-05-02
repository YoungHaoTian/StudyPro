package com.cdut.studypro.daos;

import com.cdut.studypro.beans.StudentOnlineTask;
import com.cdut.studypro.beans.StudentOnlineTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudentOnlineTaskMapper {
    long countByExample(StudentOnlineTaskExample example);

    int deleteByExample(StudentOnlineTaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentOnlineTask record);

    int insertSelective(StudentOnlineTask record);

    List<StudentOnlineTask> selectByExample(StudentOnlineTaskExample example);

    StudentOnlineTask selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentOnlineTask record, @Param("example") StudentOnlineTaskExample example);

    int updateByExample(@Param("record") StudentOnlineTask record, @Param("example") StudentOnlineTaskExample example);

    int updateByPrimaryKeySelective(StudentOnlineTask record);

    int updateByPrimaryKey(StudentOnlineTask record);

    List<Integer> selectTaskIdsByExample(StudentOnlineTaskExample studentTaskExample);
}