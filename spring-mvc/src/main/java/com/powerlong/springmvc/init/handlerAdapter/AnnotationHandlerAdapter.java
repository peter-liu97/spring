package com.powerlong.springmvc.init.handlerAdapter;


import com.alibaba.fastjson.JSON;
import com.powerlong.springmvc.annotation.RequestParam;
import com.powerlong.springmvc.annotation.ResponseBody;
import com.powerlong.springmvc.servlet.RequestMappingInfo;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof RequestMappingInfo);
    }

    @Override
    public Object handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestMappingInfo requestMappingInfo = (RequestMappingInfo) handler;
        Map<String, String[]> parameterMap = request.getParameterMap();
        String url = requestMappingInfo.getUrl();
        Method method = requestMappingInfo.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] params = new Object[method.getParameterTypes().length];
        for (int i = 0; i <parameters.length ; i++) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                if(parameters[i].getAnnotation(RequestParam.class) != null && entry.getKey()!= null &&
                        entry.getKey().equals(parameters[i].getAnnotation(RequestParam.class).value())){
                    params[i] = entry.getValue()[0];
                    //jdk1.8实现反射获取方法名   1.8之前使用asm实现
                }else if(entry.getKey().equals(parameters[i].getName())){
                    params[i] = entry.getValue()[0];
                }

                if(ServletRequest.class.isAssignableFrom(parameters[i].getType())){
                    params[i] = request;
                }else if(ServletResponse.class.isAssignableFrom(parameters[i].getType())){
                    params[i] = response;
                }
            }
        }
        try {
            Object result = method.invoke(requestMappingInfo.getObj(),params);
            if (result instanceof String) {
                if ("forward".equals(((String) result).split(":")[0])) {
                    request.getRequestDispatcher(((String) result).split(":")[1]).forward(request, response);
                } else {
                    response.sendRedirect(((String) result).split(":")[1]);
                }
            }else{
                if(method.isAnnotationPresent(ResponseBody.class)){
                    return JSON.toJSONString(result);
                }
            }

            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
