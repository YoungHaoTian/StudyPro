package com.cdut.studypro.daos;

import com.cdut.studypro.beans.Student;
import com.cdut.studypro.beans.StudentExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StudentMapper {
    long countByExample(StudentExample example);

    int deleteByExample(StudentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);

    List<Student> selectByExample(StudentExample example);

    Student selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Student record, @Param("example") StudentExample example);

    int updateByExample(@Param("record") Student record, @Param("example") StudentExample example);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    //根据手机号获取id，该方法可以根据phone查看用户是否存在
    Integer getIdByTelephone(String phone);

    //批量插入数据
    int insertBatch(@Param("students") List<Student> students);
}