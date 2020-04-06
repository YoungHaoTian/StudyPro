package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.*;
import com.cdut.studypro.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:55
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Admin> selectAdminByExample(AdminExample example) {
        return adminMapper.selectByExample(example);
    }

    @Override
    public boolean isExistsByExample(AdminExample example) {
        return adminMapper.countByExample(example) != 0;
    }

    @Override
    public List<College> getAllColleges() {
        return collegeMapper.selectByExample(null);
    }

    @Override
    public List<Student> getAllStudentsWithCollege() {
        return studentMapper.selectByExampleWithCollege(null);
    }

    @Override
    public List<Student> getAllStudentsWithCollegeByExample(StudentExample example) {
        return studentMapper.selectByExampleWithCollege(example);
    }

    @Override
    public boolean isStudentExistsByExample(StudentExample example) {
        return studentMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertStudentBatch(List<Student> students) {
        return studentMapper.insertBatch(students) > 0;
    }

    @Override
    public boolean deleteStudentById(Integer id) {
        return studentMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteStudentByIdBatch(List<Integer> ids) {
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andIdIn(ids);
        return studentMapper.deleteByExample(studentExample) > 0;
    }

    @Override
    public Student getStudentByPrimaryKey(Integer id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateStudentByPrimaryKeySelective(Student student) {
        return studentMapper.updateByPrimaryKeySelective(student) > 0;
    }

    @Override
    public boolean insertStudentSelective(Student student) {
        return studentMapper.insertSelective(student) > 0;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.selectByExample(null);
    }

    @Override
    public boolean isTeacherExistsByExample(TeacherExample example) {
        return teacherMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertTeacherSelective(Teacher teacher) {
        return teacherMapper.insertSelective(teacher) > 0;
    }

    @Override
    public boolean insertTeacherBatch(List<Teacher> teachers) {
        return teacherMapper.insertBatch(teachers) > 0;
    }

    @Override
    public List<Teacher> getAllTeachersWithCollegeAndCourseByExample(TeacherExample example) {
        return teacherMapper.selectByExampleWithCollegeAndCourse(example);
    }

    @Override
    public boolean deleteTeacherById(Integer id) {
        return teacherMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteTeacherByIdBatch(List<Integer> ids) {
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
        criteria.andIdIn(ids);
        return teacherMapper.deleteByExample(teacherExample) > 0;
    }

    @Override
    public Teacher getTeacherByPrimaryKey(Integer id) {
        return teacherMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateTeacherByPrimaryKeySelective(Teacher teacher) {
        return teacherMapper.updateByPrimaryKeySelective(teacher) > 0;
    }

    @Override
    public List<College> getAllCollegesWithBLOBs() {
        return collegeMapper.selectByExampleWithBLOBs(null);
    }

    @Override
    public List<College> getAllCollegesWithBLOBsByExample(CollegeExample example) {
        return collegeMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public boolean isCollegeExistsByExample(CollegeExample example) {
        return collegeMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertCollegeSelective(College college) {
        return collegeMapper.insertSelective(college) > 0;
    }

    @Override
    public boolean deleteCollegeById(Integer id) {
        return collegeMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean deleteCollegeByIdBatch(List<Integer> ids) {
        CollegeExample collegeExample = new CollegeExample();
        CollegeExample.Criteria criteria = collegeExample.createCriteria();
        criteria.andIdIn(ids);
        return collegeMapper.deleteByExample(collegeExample) > 0;
    }

    @Override
    public College getCollegeByPrimaryKey(Integer id) {
        return collegeMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateCollegeByPrimaryKeySelective(College college) {
        return collegeMapper.updateByPrimaryKeySelective(college) > 0;
    }

    @Override
    public boolean isCourseExistsByExample(CourseExample example) {
        return courseMapper.countByExample(example) > 0;
    }

    @Override
    public boolean insertCourseSelective(Course course) {
        return courseMapper.insertSelective(course) > 0;
    }
}
