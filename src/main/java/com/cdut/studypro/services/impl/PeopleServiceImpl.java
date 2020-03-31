package com.cdut.studypro.services.impl;

import com.cdut.studypro.beans.People;
import com.cdut.studypro.daos.PeopleDao;
import com.cdut.studypro.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 12:20
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    private PeopleDao peopleDao;

    @Override
    public List<People> queryAll() {
        return peopleDao.queryAll();
    }
}
