package com.cdut.studypro.services;


import com.cdut.studypro.beans.Teacher;
import com.cdut.studypro.beans.TeacherExample;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:54
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public interface TeacherService {
     List<Teacher> selectTeacherByExample(TeacherExample example);

     boolean isExistsByExample(TeacherExample example);

     boolean updateTeacherByPrimaryKeySelective(Teacher teacher);

     Integer getIdByTelephone(String phone);
}
