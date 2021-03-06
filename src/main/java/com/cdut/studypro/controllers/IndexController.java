package com.cdut.studypro.controllers;

import com.cdut.studypro.services.StudentService;
import com.cdut.studypro.services.TeacherService;
import com.cdut.studypro.utils.RequestResult;
import com.zhenzi.sms.ZhenziSmsClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 22:56
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    private static final String APIURL = "https://sms_developer.zhenzikj.com";
    private static final String APPID = "105088";
    private static final String APPSERCRET = "bd4deaea-c9df-4076-a7b2-0f540c1c8e0a";

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/forget")
    public String forget() {
        return "forget";
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /*@RequestMapping("/error")
    public String error() {
        return "error";
    }*/

    @ResponseBody
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public RequestResult code(@RequestBody Map<String, String> map, HttpSession session) {
        try {
            String phone = map.get("phone");
            String type = map.get("type");
            System.out.println(type);
            JSONObject json = null;
            //生成6位验证码
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            //通过榛子云账户信息获取短信发送对象
            ZhenziSmsClient client = new ZhenziSmsClient(APIURL, APPID, APPSERCRET);
            //存放手机号码和短信内容
            Map<String, String> data = new HashMap<>();
            String message = null;
            if ("register".equals(type)) {
                message = "<好学习>验证码：" + verifyCode + "，您正在注册好学习账号，验证码在5分钟内有效。";
            }
            if ("forget".equals(type)) {
                message = "<好学习>验证码：" + verifyCode + "，您正在找回好学习账号密码，验证码在5分钟内有效。";
            }
            if ("update".equals(type)) {
                message = "<好学习>验证码：" + verifyCode + "，您正在修改个人信息，验证码在5分钟内有效。";
            }
            if (message == null) {
                RequestResult.failure("验证码获取失败，请稍后重试");
            }
            data.put("message", message);
            data.put("number", phone);
            //发送短信
            String send = client.send(data);
            json = JSONObject.fromObject(send);
            if (json.getInt("code") != 0) {//发送短信失败
                RequestResult.failure("验证码发送失败，请稍后重试");
            }
            //将验证码存到session中，同时存入创建时间，以json存放
            json = new JSONObject();
            json.put("verifyCode", verifyCode);
            json.put("createTime", System.currentTimeMillis());
            // 将验证码存入session
            if ("register".equals(type)) {
                session.setAttribute("RegisterCode", json);
            }
            if ("forget".equals(type)) {
                session.setAttribute("ForgetCode", json);
            }
            if ("update".equals(type)) {
                session.setAttribute("UpdateInfoCode", json);
            }
        } catch (Exception e) {//发送短信异常
            return RequestResult.failure("验证码发送失败，请稍后重试");
        }
        return RequestResult.success();
    }
}
