package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.Admin;
import com.cdut.studypro.beans.AdminExample;
import com.cdut.studypro.beans.AdminExample.*;
import com.cdut.studypro.services.AdminService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.RequestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:50
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping("/login")
    public RequestResult login(@RequestBody Map<String, String> map) {
        System.out.println(map);
        AdminExample adminExample = new AdminExample();
        Criteria criteria = adminExample.createCriteria();
        String message = "";
        if ("phone".equals(map.get("type"))) {
            criteria.andTelephoneEqualTo(map.get("username"));
            message = "手机号不存在，请稍后重试";
        } else if ("email".equals(map.get("type"))) {
            criteria.andEmailEqualTo(map.get("username"));
            message = "邮箱不存在，请稍后重试";
        } else if ("account".equals(map.get("type"))) {
            criteria.andAccountEqualTo(map.get("username"));
            message = "帐号不存在，请稍后重试";
        }
//        criteria.andPasswordEqualTo(map.get("password"));
        List<Admin> admins = adminService.selectAdminByExample(adminExample);
        if (admins != null && admins.size() != 0) {
            Admin admin=admins.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(admin.getPassword()))){
                return RequestResult.success();
            }else{
                return RequestResult.failure("密码错误，请稍后重试");
            }
        }
        return RequestResult.failure(message);
    }
}
