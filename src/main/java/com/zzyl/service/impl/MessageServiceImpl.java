package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Message;
import com.zzyl.mapper.MessageMapper;
import com.zzyl.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public PageResult<Message> findPage(int pageNum, int pageSize, String type, String startTime, String endTime, Integer isRead, Long receiverId) {
        Page<Message> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(type)) { wrapper.eq(Message::getMsgType, type); }
        if (isRead != null) { wrapper.eq(Message::getIsRead, isRead); }
        if (receiverId != null) { wrapper.eq(Message::getReceiverId, receiverId); }
        wrapper.orderByDesc(Message::getCreateTime);
        IPage<Message> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override
    public void markAsRead(Long id) { Message msg = new Message(); msg.setId(id); msg.setIsRead(1); baseMapper.updateById(msg); }

    @Override
    public void markAllAsRead(Long receiverId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getReceiverId, receiverId).eq(Message::getIsRead, 0);
        Message msg = new Message(); msg.setIsRead(1); baseMapper.update(msg, wrapper);
    }

    @Override
    public void deleteAll(Long receiverId) { LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>(); wrapper.eq(Message::getReceiverId, receiverId); baseMapper.delete(wrapper); }

    @Override
    public long countUnread(Long receiverId) { LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>(); wrapper.eq(Message::getReceiverId, receiverId).eq(Message::getIsRead, 0); return baseMapper.selectCount(wrapper); }
}
