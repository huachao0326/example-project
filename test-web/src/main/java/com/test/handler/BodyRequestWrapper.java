package com.test.handler;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 功能描述:
 *
 * @author huachao
 * @create 2022/3/31 15:10
 * @since 1.0
 */
public class BodyRequestWrapper extends HttpServletRequestWrapper {


    private String body;

    public BodyRequestWrapper(HttpServletRequest request, String context) {
        super(request);
        body = context;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * @param
     * @description 在使用@RequestBody注解的时候，其实框架是调用了getInputStream()方法，所以我们要重写这个方法
     * @date 2020/6/16 14:45
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes("UTF-8"));

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

}
