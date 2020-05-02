package com.cdut.studypro.daos;

import com.cdut.studypro.beans.College;
import com.cdut.studypro.beans.CollegeExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CollegeMapper {
    long countByExample(CollegeExample example);

    int deleteByExample(CollegeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(College record);

    int insertSelective(College record);

    List<College> selectByExampleWithBLOBs(CollegeExample example);

    List<College> selectByExample(CollegeExample example);

    College selectByPrimaryKey(Integer id);

    College selectByPrimaryKeyWithIdAndName(Integer id);

    int updateByExampleSelective(@Param("record") College record, @Param("example") CollegeExample example);

    int updateByExampleWithBLOBs(@Param("record") College record, @Param("example") CollegeExample example);

    int updateByExample(@Param("record") College record, @Param("example") CollegeExample example);

    int updateByPrimaryKeySelective(College record);

    int updateByPrimaryKeyWithBLOBs(College record);

    int updateByPrimaryKey(College record);

    List<String> selectNameByExample(CollegeExample example);

    boolean insertBatch(@Param("colleges") List<College> colleges);
}