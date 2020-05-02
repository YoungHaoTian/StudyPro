package com.cdut.studypro.exceptions;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-18 22:59
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class FileIsNotExistException extends RuntimeException {
    public FileIsNotExistException(String message){
        super(message);
    }
}
