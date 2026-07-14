package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.entity.AiAssistant;
import com.soft.mapper.AiAssistantMapper;
import com.soft.service.AiAssistantService;
import org.springframework.stereotype.Service;

@Service
public class AiAssistantServiceImpl extends ServiceImpl<AiAssistantMapper, AiAssistant>
        implements AiAssistantService {
}
