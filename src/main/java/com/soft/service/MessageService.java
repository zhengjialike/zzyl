package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.common.PageResult;
import com.soft.entity.Message;

public interface MessageService extends IService<Message> {
    PageResult<Message> findPage(int pageNum, int pageSize, String type, String startTime, String endTime, Integer isRead, Long receiverId);
    void markAsRead(Long id);
    void markAllAsRead(Long receiverId);
    void deleteAll(Long receiverId);
    long countUnread(Long receiverId);
}
