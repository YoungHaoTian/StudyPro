package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.People;
import com.cdut.studypro.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 11:08
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private PeopleService peopleService;

    @ResponseBody
    @RequestMapping("/queryAll")
    public Object queryAll() {
        List<People> people = peopleService.queryAll();
        return people;
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("/json")
    public Map<String, String> json() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "张三");
        return map;
    }


}
