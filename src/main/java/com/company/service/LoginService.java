package com.company.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginService {

    public static void saveLogin(HttpServletRequest request, HttpServletResponse response, Object o,String key){
        request.getSession().setAttribute(key,o);
        /*利用cookie保存账号登录状态*/
        Cookie c = new Cookie("JSESSIONID",request.getSession().getId());
        c.setMaxAge(60*30);
        response.addCookie(c);
    }

    public static void deleteLogin(HttpServletRequest request){
        request.getSession().invalidate();
    }

    public static Object getLogin(HttpServletRequest request,String key){
        return request.getSession().getAttribute(key);
    }
}