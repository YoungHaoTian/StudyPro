package com.cdut.studypro.services;

import com.cdut.studypro.beans.College;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-02 22:31
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public interface CollegeService {
    List<College> getAllCollegesWithBLOBs();
    List<College> getAllColleges();
}
