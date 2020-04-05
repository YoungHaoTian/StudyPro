package com.cdut.studypro.services;

import com.cdut.studypro.beans.*;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:54
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public interface AdminService {
    List<Admin> selectAdminByExample(AdminExample example);

    boolean isExistsByExample(AdminExample example);

    List<College> getAllColleges();

    List<Student> getAllStudentsWithCollege();

    List<Student> getAllStudentsWithCollegeByExample(StudentExample example);

    boolean isStudentExistsByExample(StudentExample example);

    boolean insertStudentBatch(List<Student> students);

    boolean deleteStudentById(Integer id);

    boolean deleteStudentByIdBatch(List<Integer> ids);

    Student getStudentByPrimaryKey(Integer id);

    boolean updateStudentByPrimaryKeySelective(Student student);

    boolean insertStudentSelective(Student student);

    List<Course> getAllCourses();

    boolean isTeacherExistsByExample(TeacherExample example);

    boolean insertTeacherSelective(Teacher teacher);

    boolean insertTeacherBatch(List<Teacher> teachers);

    List<Teacher> getAllTeachersWithCollegeAndCourseByExample(TeacherExample example);

    boolean deleteTeacherById(Integer id);

    boolean deleteTeacherByIdBatch(List<Integer> ids);

    Teacher getTeacherByPrimaryKey(Integer id);

    boolean updateTeacherByPrimaryKeySelective(Teacher teacher);
}
