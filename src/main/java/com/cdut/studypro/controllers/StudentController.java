package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.Student;
import com.cdut.studypro.beans.StudentExample;
import com.cdut.studypro.beans.StudentExample.*;
import com.cdut.studypro.beans.TeacherExample;
import com.cdut.studypro.services.StudentService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.RequestResult;
import com.zhenzi.sms.ZhenziSmsClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:50
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Controller
@RequestMapping("/student")
public class StudentController {


    @Autowired
    private StudentService studentService;

    @ResponseBody
    @RequestMapping("/login")
    public RequestResult login(@RequestBody Map<String, String> map) {
        System.out.println(map);
        StudentExample studentExample = new StudentExample();
        Criteria criteria = studentExample.createCriteria();
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
        List<Student> students = studentService.selectStudentByExample(studentExample);
        if (students != null && students.size() != 0) {
            Student student = students.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(student.getPassword()))) {
                return RequestResult.success();
            } else {
                return RequestResult.failure("密码错误，请稍后重试");
            }
        }
        return RequestResult.failure(message);
    }

    @ResponseBody
    @RequestMapping("/register")
    public RequestResult register(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String code = map.get("code");
        String phone = map.get("phone");
        String password = map.get("password");

        //查询该手机号是否已经被注册
        Integer id = studentService.getIdByTelephone(phone);
        if (id != null) {
            return RequestResult.failure("该手机号已经被注册了");
        }
        JSONObject json = (JSONObject) request.getSession().getAttribute("RegisterCode");
        if (json == null) {
            return RequestResult.failure("注册失败，还未获取验证码");
        }
        if (!json.getString("verifyCode").equals(code)) {
            return RequestResult.failure("验证码错误，请稍后重试");
        }

        if ((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            //删除session中的验证码
            request.getSession().removeAttribute("RegisterCode");
            return RequestResult.failure("验证码已过期，请重新获取");
        }

        //将用户存入数据库
        Student student = new Student();
        student.setTelephone(phone);
        student.setPassword(password);

        boolean b = studentService.insertStudentSelective(student);
        if (b) {
            //注册成功，删除session中的验证码
            request.getSession().removeAttribute("RegisterCode");
            return RequestResult.success();
        }
        //删除session中的验证码
        request.getSession().removeAttribute("RegisterCode");
        return RequestResult.failure("注册失败，请稍后重试");
    }

    @ResponseBody
    @RequestMapping("/findPassword")
    public RequestResult findPassword(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String code = map.get("code");
        String phone = map.get("phone");
        String password = map.get("password");
        //1、先验证验证码是否正确
        JSONObject json = (JSONObject) request.getSession().getAttribute("ForgetCode");
        if (json == null) {
            return RequestResult.failure("密码找回失败，还未获取验证码");
        }
        if (!json.getString("verifyCode").equals(code)) {
            return RequestResult.failure("验证码错误，请稍后重试");
        }
        if ((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 5) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("验证码已过期，请重新获取");
        }

        //2、查询该手机号是否存在
        Integer id = studentService.getIdByTelephone(phone);
        System.out.println(id);
        //不存在
        if (id == null) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("该手机号不存在，请稍后再试");
        }
        //存在，修改学生密码
        Student student = new Student();
        student.setId(id);
        student.setPassword(password);
        boolean b = studentService.updateStudentByPrimaryKeySelective(student);
        if (!b) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            RequestResult.failure("密码找回失败，请稍后再试");
        }
        //删除session中的验证码
        request.getSession().removeAttribute("ForgetCode");
        return RequestResult.success();
    }
}
