package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.Message;
import com.zzyl.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private HttpSession session;

    @PostMapping("/page")
    public Result<PageResult<Message>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String type = (String) params.get("type");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        String isRead = (String) params.get("isRead");
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(messageService.findPage(pageNum, pageSize, type, startTime, endTime, isRead, userId));
    }

    @PostMapping("/markRead")
    public Result<Void> markRead(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        messageService.markAsRead(id);
        return Result.success();
    }

    @PostMapping("/markAllRead")
    public Result<Void> markAllRead() {
        Long userId = (Long) session.getAttribute("userId");
        messageService.markAllAsRead(userId);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        messageService.removeById(id);
        return Result.success();
    }

    @PostMapping("/deleteAll")
    public Result<Void> deleteAll() {
        Long userId = (Long) session.getAttribute("userId");
        messageService.deleteAll(userId);
        return Result.success();
    }

    @GetMapping("/unreadCount")
    public Result<Long> unreadCount() {
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(messageService.countUnread(userId));
    }
}
