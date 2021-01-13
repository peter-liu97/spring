package com.powerlong.springmvc.init.handlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;

/**
 * 适配器与处理器一一对应  处理器是有适配器来执行的
 */
public interface HandlerAdapter {

    /**
     *  判断当前适配器是否支持该处理器
     * @param handler
     * @return
     */
    boolean supports(Object handler);



    Object handle(HttpServletRequest request, HttpServletResponse response , Object handler);
}
