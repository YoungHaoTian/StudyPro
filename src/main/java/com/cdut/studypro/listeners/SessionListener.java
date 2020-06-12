package com.cdut.studypro.listeners;

import com.cdut.studypro.beans.Admin;
import com.cdut.studypro.beans.Student;
import com.cdut.studypro.beans.Teacher;
import com.cdut.studypro.utils.ApplicationUtil;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

/**
 * @description: 未使用
 * @author: Mr.Young
 * @date: 2020-05-06 11:52
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class SessionListener implements HttpSessionListener {
    //创建session时调用
    public void sessionCreated(HttpSessionEvent arg0) {
    }

    //session失效时调用
    public void sessionDestroyed(HttpSessionEvent arg0) {
        ServletContext application = ApplicationUtil.getApplication();
        HttpSession session = arg0.getSession();
        Object user = session.getAttribute("user");
        if (user != null) {
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                List<Integer> adminLogin = (List<Integer>) application.getAttribute("adminLogin");
                adminLogin.remove(admin.getId());
            }
            if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                List<Integer> teacherLogin = (List<Integer>) application.getAttribute("teacherLogin");
                teacherLogin.remove(teacher.getId());
            }
            if (user instanceof Student) {
                Student student = (Student) user;
                List<Integer> studentLogin = (List<Integer>) application.getAttribute("studentLogin");
                studentLogin.remove(student.getId());
            }
        }
    }
}
