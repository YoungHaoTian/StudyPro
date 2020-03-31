package com.cdut.studypro.daos;

import com.cdut.studypro.beans.People;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 12:21
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public interface PeopleDao {
    @Select("select * from people")
    List<People> queryAll();
}
