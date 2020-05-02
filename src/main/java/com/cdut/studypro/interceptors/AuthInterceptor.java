package com.cdut.studypro.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: 权限拦截器
 * @author: Mr.Young
 * @date: 2020-04-23 22:49
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 获取用户的请求地址
        HttpSession session = httpServletRequest.getSession();
        String role = (String) session.getAttribute("role");
        String uri = httpServletRequest.getRequestURI();
        //判断当前路径是否需要进行权限验证：/admin/*、/teacher/*、/student/*需要权限拦截
        if (!(uri.contains("/admin") || uri.contains("/teacher") || uri.contains("/student"))) {
            return true;
        }
        if (uri.contains("/admin") && role.equals("admin")) {
            return true;
        }
        if (uri.contains("/teacher") && role.equals("teacher")) {
            return true;
        }
        if (uri.contains("/student") && role.equals("student")) {
            return true;
        }
        PrintWriter out = httpServletResponse.getWriter();
        out.flush();
        StringBuilder sb = new StringBuilder();
        sb.append("<script>\n")
                .append("alert('没有权限访问该路径');\n")
                .append("window.history.back(0);\n")
                .append("</script>");
        out.println(sb.toString());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
