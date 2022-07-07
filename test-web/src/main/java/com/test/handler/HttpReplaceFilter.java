package com.test.handler;

/**
 * 功能描述:
 *
 * @author huachao
 * @create 2022/3/31 15:06
 * @since 1.0
 */

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName: HttpReplaceFilter
 * @Description: 过滤器
 * @Date: 2020/6/16 14:37
 */
@WebFilter(filterName = "httpReplaceFilter", urlPatterns = "/*")
@Component
public class HttpReplaceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String method = httpServletRequest.getMethod();
        String contentType = httpServletRequest.getContentType();
        if (StrUtil.isNotEmpty(contentType)) {
            contentType = contentType.toLowerCase();
        }
        // 该方法处理 POST请求并且contentType为application/json格式的
        if (HttpMethod.POST.name().equalsIgnoreCase(method) && StrUtil.isNotEmpty(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                servletRequest = new BodyRequestWrapper1(httpServletRequest);
            } catch (IOException e) {
                httpServletRequest.setAttribute(e.getMessage(), e);
                httpServletRequest.getRequestDispatcher("/jwtException").forward(servletRequest, servletResponse);
                return;
            } catch (JwtException e) {
                httpServletRequest.setAttribute("jwtException", e);
                httpServletRequest.getRequestDispatcher("/jwtException").forward(servletRequest, servletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}