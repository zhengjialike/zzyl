package com.soft.controller;

import com.soft.dto.MyApplyPageDto;
import com.soft.service.MyApplyService;
import com.soft.dto.UserLineDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class MyApplyController {

    @Autowired
    private MyApplyService myApplyService;

    @RequestMapping("/myApplyPage")
    public Map<String, Object> pageList(@RequestBody MyApplyPageDto dto, HttpSession session) {
        String applicant = currentUserName(session);
        return myApplyService.pageList(dto, applicant);
    }

    private String currentUserName(HttpSession session) {
        Object realName = session.getAttribute("realName");
        return realName != null ? realName.toString() : "未知";
    }
}
