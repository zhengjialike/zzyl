package com.soft.service;

import com.soft.dto.AiDto;

import java.util.Map;

public interface DeepSeekService {
    String chat(String message);
    Map<String, Object> savePsychology(AiDto dto);
}
