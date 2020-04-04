package com.cdut.studypro.services;


import com.cdut.studypro.beans.AdminExample;
import com.cdut.studypro.beans.Student;
import com.cdut.studypro.beans.StudentExample;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:54
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public interface StudentService {
    List<Student> selectStudentByExample(StudentExample example);

    boolean insertStudentSelective(Student student);

    boolean isExistsByExample(StudentExample example);

    boolean updateStudentByPrimaryKeySelective(Student student);

    Integer getIdByTelephone(String phone);

    int insertStudentBatch(List<Student> students);
}
