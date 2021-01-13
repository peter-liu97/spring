package com.springframework.service;

import org.springframework.annotation.Autowired;
import org.springframework.annotation.Component;

@Component(value = "userService")
public class UserService {

    @Autowired
    private OrderService orderService;
}
