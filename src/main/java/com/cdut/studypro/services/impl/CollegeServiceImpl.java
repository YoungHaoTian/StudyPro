package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.College;
import com.cdut.studypro.daos.CollegeMapper;
import com.cdut.studypro.services.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-02 22:31
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */

@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;


}
