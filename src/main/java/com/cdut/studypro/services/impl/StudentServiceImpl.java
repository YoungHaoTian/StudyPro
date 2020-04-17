package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.*;
import com.cdut.studypro.daos.CourseMapper;
import com.cdut.studypro.daos.StudentMapper;
import com.cdut.studypro.daos.TeacherMapper;
import com.cdut.studypro.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Student> selectStudentByExample(StudentExample example) {
        return studentMapper.selectByExample(example);
    }

    @Override
    public boolean insertStudentSelective(Student student) {
        int i = studentMapper.insertSelective(student);
        return i > 0;
    }

    @Override
    public boolean isExistsByExample(StudentExample example) {
        long l = studentMapper.countByExample(example);
        return l > 0;
    }

    @Override
    public boolean updateStudentByPrimaryKeySelective(Student student) {
        int i = studentMapper.updateByPrimaryKeySelective(student);
        return i > 0;
    }

    @Override
    public Integer getIdByTelephone(String phone) {
        return studentMapper.getIdByTelephone(phone);
    }

    @Override
    public List<Course> getAllCourseWithBLOBsAndTeacherByExample(CourseExample courseExample) {
        return courseMapper.selectByExampleWithBLOBsAndTeacher(courseExample);
    }

    @Override
    public List<Integer> getTeacherIdByTeacherExample(TeacherExample teacherExample) {
        return teacherMapper.selectIdsByExample(teacherExample);
    }


}
