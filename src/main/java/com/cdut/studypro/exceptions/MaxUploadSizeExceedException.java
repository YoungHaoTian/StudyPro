package com.cdut.studypro.exceptions;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-24 12:33
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class MaxUploadSizeExceedException extends RuntimeException {
    private final long maxSize;

    public MaxUploadSizeExceedException(long maxSize) {
        this("文件上传最大大小为：", maxSize);
    }

    public MaxUploadSizeExceedException(String message, long maxSize) {
        super(message + maxSize + "MB");
        this.maxSize = maxSize;
    }

    public long getMaxUploadSize() {
        return this.maxSize;
    }
}
