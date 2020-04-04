package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.Admin;
import com.cdut.studypro.beans.Student;
import com.cdut.studypro.beans.StudentExample;
import com.cdut.studypro.daos.StudentMapper;
import com.cdut.studypro.services.AdminService;
import com.cdut.studypro.services.StudentService;
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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> selectStudentByExample(StudentExample example) {
         return studentMapper.selectByExample(example);
    }

    @Override
    public boolean insertStudentSelective(Student student) {
        int i = studentMapper.insertSelective(student);
        if (i>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistsByExample(StudentExample example) {
        long l = studentMapper.countByExample(example);
        if (l>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStudentByPrimaryKeySelective(Student student) {
        int i = studentMapper.updateByPrimaryKeySelective(student);
        if (i>0){
            return true;
        }
        return false;
    }

    @Override
    public Integer getIdByTelephone(String phone) {
        return studentMapper.getIdByTelephone(phone);
    }

    @Override
    public int insertStudentBatch(List<Student> students) {
        return studentMapper.insertBatch(students);
    }

}
