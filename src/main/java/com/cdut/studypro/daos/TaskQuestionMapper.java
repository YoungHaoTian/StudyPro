package com.cdut.studypro.daos;

import com.cdut.studypro.beans.TaskQuestion;
import com.cdut.studypro.beans.TaskQuestionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskQuestionMapper {
    long countByExample(TaskQuestionExample example);

    int deleteByExample(TaskQuestionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TaskQuestion record);

    int insertSelective(TaskQuestion record);

    List<TaskQuestion> selectByExample(TaskQuestionExample example);

    TaskQuestion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TaskQuestion record, @Param("example") TaskQuestionExample example);

    int updateByExample(@Param("record") TaskQuestion record, @Param("example") TaskQuestionExample example);

    int updateByPrimaryKeySelective(TaskQuestion record);

    int updateByPrimaryKey(TaskQuestion record);
}