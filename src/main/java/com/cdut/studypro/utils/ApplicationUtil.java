package com.cdut.studypro.utils;

import javax.servlet.ServletContext;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-05-06 12:30
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class ApplicationUtil {
    private static ServletContext application;

    public static void setApplication(ServletContext servletContext) {
        application = servletContext;
    }

    public static ServletContext getApplication() {
        return application;
    }
}
