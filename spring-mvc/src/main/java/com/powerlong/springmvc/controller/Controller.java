package com.powerlong.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

    Object handler(HttpServletRequest request , HttpServletResponse response);
}
