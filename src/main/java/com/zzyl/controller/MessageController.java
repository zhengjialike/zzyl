package com.zzyl.controller;

import com.zzyl.common.PageResult;
import com.zzyl.common.Result;
import com.zzyl.entity.Message;
import com.zzyl.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired private MessageService messageService;
    @Autowired private HttpSession session;

    @PostMapping("/page")
    public Result<PageResult<Message>> page(@RequestBody Map<String, Object> params) {
        int pageNum = (int) params.getOrDefault("pageNum", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 10);
        String type = (String) params.get("type");
        Integer isRead = params.get("isRead") != null ? Integer.valueOf(params.get("isRead").toString()) : null;
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(messageService.findPage(pageNum, pageSize, type, null, null, isRead, userId));
    }

    @PostMapping("/markRead") public Result<Void> markRead(@RequestBody Map<String, Object> params) { messageService.markAsRead(Long.valueOf(params.get("id").toString())); return Result.success(); }
    @PostMapping("/markAllRead") public Result<Void> markAllRead() { messageService.markAllAsRead((Long)session.getAttribute("userId")); return Result.success(); }
    @PostMapping("/delete") public Result<Void> delete(@RequestBody Map<String, Object> params) { messageService.removeById(Long.valueOf(params.get("id").toString())); return Result.success(); }
    @PostMapping("/deleteAll") public Result<Void> deleteAll() { messageService.deleteAll((Long)session.getAttribute("userId")); return Result.success(); }
    @GetMapping("/unreadCount") public Result<Long> unreadCount() { return Result.success(messageService.countUnread((Long)session.getAttribute("userId"))); }
}
