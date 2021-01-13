package com.powerlong.springmvc.init.handlerAdapter;


import com.powerlong.springmvc.annotation.RequestMapping;
import com.powerlong.springmvc.controller.Controller;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BeanNameHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public Object handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return ((Controller)handler).handler(request,response);
    }
}
