package com.cdut.studypro.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @description:
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
        HttpSession session = httpServletRequest.getSession();

        Object user = session.getAttribute("user");
        String requestURI = httpServletRequest.getRequestURI();
        if (user != null) {
            return true;
        }
        String path = session.getServletContext().getContextPath();
        httpServletResponse.sendRedirect(path + "/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
