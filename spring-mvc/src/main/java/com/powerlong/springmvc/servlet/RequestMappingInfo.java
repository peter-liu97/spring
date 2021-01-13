package com.powerlong.springmvc.servlet;

import java.lang.reflect.Method;

public class RequestMappingInfo {
    private Method method;

    private String url;

    private Object obj;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
