package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.Course;
import com.cdut.studypro.beans.CourseExample;
import com.cdut.studypro.beans.Student;
import com.cdut.studypro.beans.StudentExample;
import com.cdut.studypro.daos.CourseMapper;
import com.cdut.studypro.daos.StudentMapper;
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
    public List<Course> getAllCourseWithBLOBsAndTeacherAndCollege() {
        return courseMapper.selectByExampleWithBLOBsAndCollegeAndTeacher(null);
    }

    @Override
    public List<Course> getAllCourseWithBLOBsAndTeacherAndCollegeByCollegeId(Integer id) {
        CourseExample courseExample=new CourseExample();
        CourseExample.Criteria criteria = courseExample.createCriteria();
        criteria.andCollegeIdEqualTo(id);
        return courseMapper.selectByExampleWithBLOBsAndCollegeAndTeacher(courseExample);
    }


}
