package com.cdut.studypro.controllers;

import com.cdut.studypro.beans.DiscussPost;
import com.cdut.studypro.beans.Student;
import com.cdut.studypro.beans.Teacher;
import com.cdut.studypro.beans.TeacherExample;
import com.cdut.studypro.services.TeacherService;
import com.cdut.studypro.utils.MD5Util;
import com.cdut.studypro.utils.RequestResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 14:50
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ResponseBody
    @PostMapping(value = "/login")
    public RequestResult login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        System.out.println(map);
        TeacherExample teacherExample = new TeacherExample();
        TeacherExample.Criteria criteria = teacherExample.createCriteria();
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
        List<Teacher> teachers = teacherService.selectTeacherByExample(teacherExample);
        if (teachers != null && teachers.size() != 0) {
            Teacher teacher = teachers.get(0);
            String password = map.get("password");
            if (password.equals(MD5Util.stringToMD5(teacher.getPassword()))) {
                //登录成功后将教师信息保存在session中
                request.getSession().setAttribute("user", teacher);
                return RequestResult.success();
            } else {
                return RequestResult.failure("密码错误，请稍后重试");
            }
        }
        return RequestResult.failure(message);
    }

    @ResponseBody
    @PostMapping(value = "/findPassword")
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
        Integer id = teacherService.getIdByTelephone(phone);
        //不存在
        if (id == null) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("该手机号不存在，请稍后再试");
        }
        //存在，修老师密码
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setPassword(password);
        boolean b = teacherService.updateTeacherByPrimaryKeySelective(teacher);
        if (!b) {
            //删除session中的验证码
            request.getSession().removeAttribute("ForgetCode");
            return RequestResult.failure("密码找回失败，请稍后再试");
        }
        //删除session中的验证码
        request.getSession().removeAttribute("ForgetCode");
        return RequestResult.success();
    }


    /*@ResponseBody
    @PostMapping("/searchDiscussPostByTerm")
    public RequestResult searchDiscussPostByTerm(@RequestParam("name") String name, HttpSession session) {
        //将查询条件存放进session中，以回显查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("name", null);
        if (!"".equals(name.trim())) {
            map.put("name", name.trim());
        }
        session.setAttribute("discussPostQueryCriteria", map);
        return RequestResult.success();
    }

    @ResponseBody
    @PostMapping("/deleteDiscussPostBatch")
    public RequestResult deleteDiscussPostBatch(@RequestParam("ids") String id) {

        if (id == null) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        String[] ids = id.split("-");
        List<Integer> postIds = new ArrayList<>();
        for (String s : ids) {
            postIds.add(Integer.parseInt(s));
        }
        boolean b = adminService.deleteDiscussPostByIdBatch(postIds);
        if (!b) {
            return RequestResult.failure("批量删除失败，请稍后再试");
        }
        return RequestResult.success();

    }


    @RequestMapping("/searchDiscussReply/{id}")
    public String searchDiscussReply(@PathVariable("id") Integer id, Map<String, Object> map, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam("pageNumber") Integer pageNumber, HttpSession session) {

        Map<String, Object> map1 = (Map<String, Object>) session.getAttribute("discussPostQueryCriteria");
        if (map1 == null) {
            map1 = new HashMap<>();
            map1.put("id", id);
            map1.put("name", null);
        } else {
            map1.put("id", id);
        }
        PageHelper.startPage(pageNum, 10);
        List<DiscussPost> discussPosts = adminService.getDiscussPostByDiscussIdWithStudentName(map1);
        PageInfo<Student> page = new PageInfo(discussPosts, 10);
        map.put("pageInfo", page);
        map.put("pageNumber", pageNumber);
        map.put("id", id);
        return "teacher/searchDiscussReply";
    }


    @ResponseBody
    @PostMapping("/saveDiscussReply/{id}")
    public RequestResult saveDiscussReply(@PathVariable("id") Integer id, @RequestParam("content") String content) {
        if (id == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if (content == null) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        if ("".equals(content)) {
            return RequestResult.failure("回复内容为空，请重新输入");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setContent(content);
        discussPost.setDiscussId(id);
        discussPost.setStudentId(0);
        discussPost.setRecordTime(new Date());
        boolean b = adminService.insertDiscussPostSelective(discussPost);
        if (!b) {
            return RequestResult.failure("新增回复失败，请稍后再试");
        }
        return RequestResult.success();
    }

    @RequestMapping("/createReply/{id}")
    public String createReply(@PathVariable("id") Integer id, Map<String, Object> map) {
        map.put("id", id);
        return "admin/createReply";
    }*/
}
