package com.company.service;

import com.company.domain.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginService {
    /**
     * 保存登录状态
     * @param request
     * @param response
     * @param o
     * @param key
     */
    public static void saveLogin(HttpServletRequest request, HttpServletResponse response, Object o,String key){
        request.getSession().setAttribute(key,o);
        /*利用cookie保存账号登录状态*/
        Cookie c = new Cookie("JSESSIONID",request.getSession().getId());
        c.setMaxAge(60*30);
        response.addCookie(c);
    }

    /**
     * 获取登录信息
     * @param request
     * @param key
     * @return
     */
    public static Object getLogin(HttpServletRequest request,String key){
        return request.getSession().getAttribute(key);
    }
}
