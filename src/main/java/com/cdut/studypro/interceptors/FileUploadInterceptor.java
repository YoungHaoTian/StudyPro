package com.cdut.studypro.interceptors;

import com.cdut.studypro.exceptions.MaxUploadSizeExceedException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: Mr.Young
 * @date: 2020-04-03 16:44
 * @email: no.bugs@foxmail.com
 * @qq: 1023704092
 */
public class FileUploadInterceptor implements HandlerInterceptor {
    private long maxSize;

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        if (httpServletRequest != null && ServletFileUpload.isMultipartContent(httpServletRequest)) {
            ServletRequestContext servletRequestContext = new ServletRequestContext(httpServletRequest);
            long requestSize = servletRequestContext.contentLength();
            if (requestSize > maxSize * 1024 * 1024) {
                // 抛出异常
                throw new MaxUploadSizeExceedException(maxSize);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
