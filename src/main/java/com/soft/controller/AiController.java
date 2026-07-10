package com.soft.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AiController {

    @Autowired
    private ChatModel chatModel;

    // 一次性响应（同步）
    @RequestMapping("/chat01")
    public String chat01System(@RequestParam(name = "msg") String msg) {
        String content = chatModel.call(msg);
        return content;
    }

    // 流式响应（异步）
    @RequestMapping(value = "/chat02", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
    public Flux<String> chat03System(@RequestParam(name = "msg") String msg) {
        Flux<String> flux = chatModel.stream(msg);
        return flux;
    }
}