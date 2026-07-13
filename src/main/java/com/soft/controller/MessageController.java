package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soft.pojo.Message;
import com.soft.service.MessageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/page")
    public Map<String, Object> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Message::getCreateTime);
        Object isRead = params.get("isRead");
        if (isRead != null) wrapper.eq(Message::getIsRead, isRead);
        IPage<Message> p = messageService.page(new Page<>(pageNum, pageSize), wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", Map.of("records", p.getRecords(), "total", p.getTotal()));
        return result;
    }

    @GetMapping("/unreadCount")
    public Map<String, Object> unreadCount() {
        long count = messageService.count(new LambdaQueryWrapper<Message>().eq(Message::getIsRead, 0));
        return Map.of("code", 200, "data", count);
    }

    @PostMapping("/markRead")
    public Map<String, Object> markRead(@RequestBody Map<String, Integer> body) {
        Message m = new Message(); m.setId(body.get("id")); m.setIsRead(1);
        messageService.updateById(m);
        return Map.of("code", 200, "msg", "success");
    }

    @PostMapping("/markAllRead")
    public Map<String, Object> markAllRead() {
        List<Message> list = messageService.list(new LambdaQueryWrapper<Message>().eq(Message::getIsRead, 0));
        for (Message m : list) { m.setIsRead(1); messageService.updateById(m); }
        return Map.of("code", 200, "msg", "success");
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody Map<String, Integer> body) {
        messageService.removeById(body.get("id"));
        return Map.of("code", 200, "msg", "success");
    }

    @PostMapping("/deleteAll")
    public Map<String, Object> deleteAll() {
        messageService.remove(new LambdaQueryWrapper<>());
        return Map.of("code", 200, "msg", "success");
    }
}
