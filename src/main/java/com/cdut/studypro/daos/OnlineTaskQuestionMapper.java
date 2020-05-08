package com.cdut.studypro.daos;

import com.cdut.studypro.beans.OnlineTaskQuestion;
import com.cdut.studypro.beans.OnlineTaskQuestionExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OnlineTaskQuestionMapper {
    long countByExample(OnlineTaskQuestionExample example);

    int deleteByExample(OnlineTaskQuestionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OnlineTaskQuestion record);

    int insertSelective(OnlineTaskQuestion record);

    List<OnlineTaskQuestion> selectByExample(OnlineTaskQuestionExample example);

    List<OnlineTaskQuestion> selectByExampleWithTitleAndItem(OnlineTaskQuestionExample example);

    List<OnlineTaskQuestion> selectByExampleWithAnswerAndScore(OnlineTaskQuestionExample example);

    OnlineTaskQuestion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OnlineTaskQuestion record, @Param("example") OnlineTaskQuestionExample example);

    int updateByExample(@Param("record") OnlineTaskQuestion record, @Param("example") OnlineTaskQuestionExample example);

    int updateByPrimaryKeySelective(OnlineTaskQuestion record);

    int updateByPrimaryKey(OnlineTaskQuestion record);

    List<OnlineTaskQuestion> selectByOnlineTaskId(Integer id);

    //批量插入数据
    int insertBatch(@Param("questions") List<OnlineTaskQuestion> questions);

    Integer getTotalScore(Integer id);
}