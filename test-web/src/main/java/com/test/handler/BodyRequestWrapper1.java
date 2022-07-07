package com.test.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.util.JwtDecoded;
import com.test.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpMethod;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 功能描述:
 *
 * @author huachao
 * @create 2022/3/31 15:10
 * @since 1.0
 */
public class BodyRequestWrapper1 extends HttpServletRequestWrapper {


    private byte[] body;

    public BodyRequestWrapper1(HttpServletRequest request) throws IOException, JwtException {
        super(request);
        String method = request.getMethod();
        // 由于request并没有提供现成的获取json字符串的方法，所以我们需要将body中的流转为字符串
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String json = stringBuilder.toString();
        if (HttpMethod.POST.name().equalsIgnoreCase(method)) {
            JSONObject tokenObject = JSON.parseObject(json);
            // JWT 工具包验证 token
            JwtUtils jwtUtils = new JwtUtils();
            JwtDecoded jwtDecoded = jwtUtils.verify((String) tokenObject.get("token"));
            body = jwtDecoded.getData().getBytes(StandardCharsets.UTF_8);
        } else {
            body = json.getBytes();
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * @param
     * @description 在使用@RequestBody注解的时候，其实框架是调用了getInputStream()方法，所以我们要重写这个方法
     * @date 2020/6/16 14:45
     */
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

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
