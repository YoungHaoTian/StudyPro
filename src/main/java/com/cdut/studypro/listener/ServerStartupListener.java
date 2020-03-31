package com.cdut.studypro.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
        application.setAttribute("APP_PATH", path);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }

}
