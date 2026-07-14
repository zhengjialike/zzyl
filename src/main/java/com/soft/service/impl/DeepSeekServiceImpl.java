package com.soft.service.impl;

import com.soft.dto.AiDto;
import com.soft.entity.AiAssistant;
import com.soft.mapper.AiAssistantMapper;
import com.soft.service.DeepSeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.base-url}")
    private String baseUrl;

    @Value("${deepseek.model}")
    private String model;

    @Autowired
    private AiAssistantMapper aiAssistantMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String chat(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "你是一个老年人心理医生，专门为老年人提供心理辅导和建议。请用温暖、耐心的语气回答，其他无关问题不回答。");

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", message);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", Arrays.asList(systemMsg, userMsg));
        body.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    baseUrl + "/v1/chat/completions", request, Map.class);

            Map<String, Object> respBody = response.getBody();
            if (respBody != null && respBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) respBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> msg = (Map<String, Object>) choice.get("message");
                    return (String) msg.get("content");
                }
            }
            return "AI 服务暂时无响应，请稍后重试";
        } catch (Exception e) {
            return "调用 AI 服务失败：" + e.getMessage();
        }
    }

    @Override
    public Map<String, Object> savePsychology(AiDto dto) {
        Map<String, Object> result = new HashMap<>();
        AiAssistant ai = new AiAssistant();
        ai.setOldid(dto.getOldid());
        ai.setAiresult(dto.getAiresult());
        ai.setInputmsg(dto.getInputmsg());
        ai.setCreatetime(new Date());
        ai.setAitype("心理助手");
        aiAssistantMapper.insert(ai);
        result.put("code", 200);
        result.put("msg", "心理咨询结果保存成功");
        return result;
    }
}
