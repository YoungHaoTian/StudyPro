package com.cdut.studypro.daos;

import com.cdut.studypro.beans.Teacher;
import com.cdut.studypro.beans.TeacherExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TeacherMapper {
    long countByExample(TeacherExample example);

    int deleteByExample(TeacherExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    List<Teacher> selectByExample(TeacherExample example);

    Teacher selectByPrimaryKey(Integer id);

    Teacher selectByPrimaryKeyWithCourse(Integer id);

    Teacher selectByPrimaryKeyWithoutCollegeAndCourse(Integer id);

    Teacher selectByPrimaryKeyWithCollege(Integer id);

    List<Teacher> selectByExampleWithIdNameAndCollege(TeacherExample example);

    List<Teacher> selectByExampleWithIdNameNumberAndCollege(TeacherExample example);

    int updateByExampleSelective(@Param("record") Teacher record, @Param("example") TeacherExample example);

    int updateByExample(@Param("record") Teacher record, @Param("example") TeacherExample example);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    Integer getIdByTelephone(String phone);

    //批量插入数据
    int insertBatch(@Param("teachers") List<Teacher> teachers);

    //带学院和课程的条件查询
    List<Teacher> selectByExampleWithCollegeAndCourse(TeacherExample example);

    Teacher selectByPrimaryKeyWithIdNameAndCollege(Integer id);

    //根据教师姓名返回其主键值
    List<Integer> selectIdsByExample(TeacherExample example);

    List<String> selectTelephoneByExample(TeacherExample example);

}