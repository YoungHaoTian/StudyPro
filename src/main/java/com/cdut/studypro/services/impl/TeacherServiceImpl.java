package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.Admin;
import com.cdut.studypro.beans.Teacher;
import com.cdut.studypro.beans.TeacherExample;
import com.cdut.studypro.daos.TeacherMapper;
import com.cdut.studypro.services.TeacherService;
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
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Teacher> selectTeacherByExample(TeacherExample example) {
         return teacherMapper.selectByExample(example);
    }

    @Override
    public boolean isExistsByExample(TeacherExample example) {
        long l = teacherMapper.countByExample(example);
        if (l!=0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTeacherByPrimaryKeySelective(Teacher teacher) {
        int i = teacherMapper.updateByPrimaryKeySelective(teacher);
        if (i>0){
            return true;
        }
        return false;
    }

    @Override
    public Integer getIdByTelephone(String phone) {
        return teacherMapper.getIdByTelephone(phone);
    }


}
