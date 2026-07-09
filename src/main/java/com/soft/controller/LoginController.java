package com.soft.controller;

import com.soft.dto.UserDto;
import com.soft.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public Map<String, Object> userLogin(@RequestBody UserDto userDto, HttpSession session) {
        return userService.queryUserService(userDto,session);
    }
}