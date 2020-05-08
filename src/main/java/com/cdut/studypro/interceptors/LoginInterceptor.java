package com.cdut.studypro.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * @description: 登录拦截器
 * @author: Mr.Young
 * @date: 2020-03-31 15:04
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //处理之前执行
        //1、判断session中有没有保存用户
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html");
        String appPath = (String) httpServletRequest.getServletContext().getAttribute("APP_PATH");
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {//当session为空时，说明超时了，需要用户重新登录
            PrintWriter out = httpServletResponse.getWriter();
            out.flush();
            StringBuilder sb = new StringBuilder();
            sb.append("<script>\n")
                    .append("let r=confirm('您当前身份已过期，请重新登录');\n")
                    .append("if(r==true){\n").append("window.location.href='").append(appPath).append("/index/login'\n")
                    .append("}\n")
                    .append("</script>");
            out.println(sb.toString());
        } else {
            Object user = session.getAttribute("user");
            if (user != null) {
                return true;
            }
            PrintWriter out = httpServletResponse.getWriter();
            out.flush();
            StringBuilder sb = new StringBuilder();
            sb.append("<script>\n")
                    .append("let r=confirm('您还未登录，请前去登录');\n")
                    .append("if(r==true){\n").append("window.location.href='").append(appPath).append("/index/login'\n")
                    .append("}\n")
                    .append("</script>");
            out.println(sb.toString());
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
