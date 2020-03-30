package com.cdut.young.test;

import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
}
