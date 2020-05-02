package com.cdut.studypro.daos;

import com.cdut.studypro.beans.Discuss;
import com.cdut.studypro.beans.DiscussExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DiscussMapper {
    long countByExample(DiscussExample example);

    int deleteByExample(DiscussExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Discuss record);

    int insertSelective(Discuss record);

    List<Discuss> selectByExampleWithBLOBs(DiscussExample example);

    List<Discuss> selectByExample(DiscussExample example);

    Discuss selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Discuss record, @Param("example") DiscussExample example);

    int updateByExampleWithBLOBs(@Param("record") Discuss record, @Param("example") DiscussExample example);

    int updateByExample(@Param("record") Discuss record, @Param("example") DiscussExample example);

    int updateByPrimaryKeySelective(Discuss record);

    int updateByPrimaryKeyWithBLOBs(Discuss record);

    int updateByPrimaryKey(Discuss record);

    List<Discuss> selectByExampleWithBLOBsAndTeacherAndCourse(DiscussExample example);

    List<Discuss> selectByCourseNameAndTeacherName(Map<String, String> map);

    List<Discuss> selectByExampleWithBLOBsAndCourse(DiscussExample example);

    List<Integer> selectDiscussIdByExample(DiscussExample discussExample);
}