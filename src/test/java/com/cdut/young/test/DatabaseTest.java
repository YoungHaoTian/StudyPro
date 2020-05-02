package com.cdut.young.test;

import net.sf.ezmorph.test.ArrayAssertions;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-03-25 21:00
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class DatabaseTest {
    @Test
    public void test() throws ClassNotFoundException, IOException, SQLException {


        Properties pro = new Properties();
        pro.load(DatabaseTest.class.getClassLoader().getResourceAsStream("dbconfig.properties"));
        String jdbcUrl = pro.getProperty("jdbc.jdbcUrl");
        String driverClass = pro.getProperty("jdbc.driverClass");
        String user = pro.getProperty("jdbc.user");
        String password = pro.getProperty("jdbc.password");
        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
        System.out.println(connection);

    }

    @Test
    public void testStringArray() {
        String[] original_title = new String[]{"姓名", "学号", "电话", "身份证号码", "账号", "密码", "邮箱"};
        String[] current_title = new String[]{"姓名", "学号", "电话", "身份证号码", "账号", "密码", "邮箱"};
        boolean equals = Arrays.equals(original_title, current_title);
        System.out.println(equals);

        String path = "\15\1\1\1586594478031_movie.mp4";
        String substring = path.substring(path.indexOf("_") + 1);
        System.out.println(substring);

    }

    @Test
    public void testRegex() {
        String s="aaaa";
        System.out.println(s.matches("[^(.*\\s+.*)]"));
        System.out.println(s.matches("(.*\\s+.*)"));

        String s1="513901199806163319";
        System.out.println(s1.matches("[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]"));
    }
}
