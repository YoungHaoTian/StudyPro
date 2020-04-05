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
        long l = adminMapper.countByExample(example);
        if (l != 0) {
            return true;
        }
        return false;
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
        long l = studentMapper.countByExample(example);
        return l > 0;
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
        int i = studentMapper.deleteByExample(studentExample);
        return i > 0;
    }

    @Override
    public Student getStudentByPrimaryKey(Integer id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateStudentByPrimaryKeySelective(Student student) {
        int i = studentMapper.updateByPrimaryKeySelective(student);
        return i > 0;
    }

    @Override
    public boolean insertStudentSelective(Student student) {
        int i = studentMapper.insertSelective(student);
        return i > 0;
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
}
