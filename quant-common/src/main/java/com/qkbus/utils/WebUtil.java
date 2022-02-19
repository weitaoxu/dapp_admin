package com.qkbus.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qkbus.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;


/**
 * Miscellaneous utilities for web applications.
 *
 * @author L.cm
 */
@Slf4j
public class WebUtil extends WebUtils {

    public static final String USER_AGENT_HEADER = "user-agent";

    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param key      cookie key
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置cookie
     *
     * @param response        HttpServletResponse
     * @param name            cookie name
     * @param value           cookie value
     * @param maxAgeInSeconds maxage
     */
    public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(StringPool.SLASH);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    private static final String[] IP_HEADER_NAMES = new String[]{
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    /**
     * 获取请求头的值
     *
     * @param name 请求头名称
     * @return 请求头
     */
    public static String getHeader(String name) {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getHeader(name);
    }

    //获取reques拦截器带的参数
    public static Object getValue(String type) {
        Object attribute = getRequest().getAttribute(type);
        if (ObjectUtil.isEmpty(attribute)) {
            log.info("从Request中获取type错误");
            throw new BadRequestException("");
        }
        return attribute;
    }

    /**
     * 获取请求头的值
     *
     * @param name 请求头名称
     * @return 请求头
     */
    public static Enumeration<String> getHeaders(String name) {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getHeaders(name);
    }

    /**
     * 获取所有的请求头
     *
     * @return 请求头集合
     */
    public static Enumeration<String> getHeaderNames() {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getHeaderNames();
    }

    /**
     * 获取请求参数
     *
     * @param name 请求参数名
     * @return 请求参数
     */
    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getParameter(name);
    }

    /**
     * 获取 request 请求体
     *
     * @param servletInputStream servletInputStream
     * @return body
     */
    public static String getRequestBody(ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}

