package com.cdut.studypro.daos;

import com.cdut.studypro.beans.DiscussExample;
import com.cdut.studypro.beans.DiscussPost;
import com.cdut.studypro.beans.DiscussPostExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DiscussPostMapper {
    long countByExample(DiscussPostExample example);

    int deleteByExample(DiscussPostExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DiscussPost record);

    int insertSelective(DiscussPost record);

    List<DiscussPost> selectByExampleWithBLOBs(DiscussPostExample example);

    List<DiscussPost> selectByExampleWithBLOBsAndStudentName(DiscussPostExample example);

    List<DiscussPost> selectByExample(DiscussPostExample example);

    DiscussPost selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    int updateByExampleWithBLOBs(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    int updateByExample(@Param("record") DiscussPost record, @Param("example") DiscussPostExample example);

    int updateByPrimaryKeySelective(DiscussPost record);

    int updateByPrimaryKeyWithBLOBs(DiscussPost record);

    int updateByPrimaryKey(DiscussPost record);

    DiscussPost selectByPrimaryKeyWithStudentName(Integer id);

    List<DiscussPost> selectByDiscussIdWithStudentName(Map<String, Object> map);

    List<Integer> selectPostIdByExample(DiscussExample discussExample);
}