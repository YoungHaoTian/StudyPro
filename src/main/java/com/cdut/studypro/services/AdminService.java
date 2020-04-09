package com.cdut.studypro.services;

import com.cdut.studypro.beans.*;

import java.util.List;
import java.util.Map;

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

    List<Integer> getTeacherIdByCourseExample(CourseExample example);

    boolean deleteTeacherById(Integer id);

    boolean deleteTeacherByIdBatch(List<Integer> ids);

    Teacher getTeacherByPrimaryKey(Integer id);

    boolean updateTeacherByPrimaryKeySelective(Teacher teacher);

    boolean unbindCourse(Course course);

    List<College> getAllCollegesWithBLOBsByExample(CollegeExample example);

    boolean isCollegeExistsByExample(CollegeExample example);

    boolean insertCollegeSelective(College college);

    boolean deleteCollegeById(Integer id);

    boolean deleteCollegeByIdBatch(List<Integer> ids);

    College getCollegeByPrimaryKey(Integer id);

    boolean updateCollegeByPrimaryKeySelective(College college);

    List<Teacher> getAllTeachersWithIdNameAndCollege();

    boolean isCourseExistsByExample(CourseExample example);

    boolean insertCourseSelective(Course course);

    List<Course> getAllCoursesWithBLOBsCollegeAndTeacherByExample(CourseExample example);

    boolean deleteCourseById(Integer id);

    boolean deleteCourseByIdBatch(List<Integer> ids);

    Course getCourseByPrimaryKey(Integer id);

    boolean updateCourseByPrimaryKeySelective(Course course);

    List<Integer> getTeacherIdByTeacherExample(TeacherExample example);

    Course getCourseByPrimaryKeyWithoutTeacherAndCollege(Integer id);

    List<Discuss> getAllDiscussWithBLOBsAndTeacherAndCourseByExample(DiscussExample example);

    List<Integer> getCourseIdByCourseExample(CourseExample example);

    List<Course> getAllCoursesWithWithCollegeAndTeacher();

    boolean deleteDiscussByIdBatch(List<Integer> ids);

    Discuss getDiscussByPrimaryKey(Integer id);

    boolean updateDiscussByPrimaryKeySelective(Discuss discuss);

    boolean insertNoticeSelective(Notice notice);

    List<Notice> getAllNoticesByExample(NoticeExample example);

    boolean deleteNoticeById(Integer id);

    boolean deleteNoticeByIdBatch(List<Integer> noticeIds);

    Notice getNoticeByPrimaryKey(Integer id);

    boolean updateNoticeByPrimaryKeySelective(Notice notice);

    Admin getAdminById(Integer id);

    boolean updateAdminByPrimaryKeySelective(Admin admin);
}
