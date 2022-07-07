package com.test.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @description:
 * @author: huachao
 * @createDate: 2022/7/6
 */
@Component
public class UrlFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        StringBuffer urlBuffer = httpServletRequest.getRequestURL();
        if(urlBuffer.toString().endsWith("/room")){
            String postContent = getBody(httpServletRequest);
            httpServletRequest = new BodyRequestWrapper(httpServletRequest,postContent);
            JSONObject tokenObject = JSON.parseObject(postContent);
            if("keygen".equals(tokenObject.getString("type"))){
                httpServletRequest.getRequestDispatcher("/room/keygen").forward(httpServletRequest,httpServletResponse);
                return;
            }else{
                httpServletRequest.getRequestDispatcher("/room/sign").forward(httpServletRequest,httpServletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private String getBody(HttpServletRequest servletRequest) throws IOException {
        // 由于request并没有提供现成的获取json字符串的方法，所以我们需要将body中的流转为字符串
        BufferedReader reader = servletRequest.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
