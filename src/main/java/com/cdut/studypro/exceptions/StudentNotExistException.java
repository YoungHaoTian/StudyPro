package com.cdut.studypro.exceptions;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-05 00:16
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class StudentNotExistException extends Exception {

    public StudentNotExistException(String message){
        super(message);
    }
}
