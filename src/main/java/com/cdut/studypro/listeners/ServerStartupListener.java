package com.cdut.studypro.listeners;

import com.cdut.studypro.utils.ApplicationUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-31 13:16
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
@WebListener
public class ServerStartupListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        // 将web应用名称（路径）保存到application范围中
        ServletContext application = sce.getServletContext();
        String path = application.getContextPath();
        /*ApplicationUtil.setApplication(application);
        //用来记录已经登录的管理员
        List<Integer> adminLogin = new ArrayList<>();
        application.setAttribute("adminLogin", adminLogin);
        //用来记录已经登录的教师
        List<Integer> teacherLogin = new ArrayList<>();
        application.setAttribute("teacherLogin", teacherLogin);
        //用来记录已经登录的学生
        List<Integer> studentLogin = new ArrayList<>();
        application.setAttribute("studentLogin", studentLogin);*/

        application.setAttribute("APP_PATH", path);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

}
