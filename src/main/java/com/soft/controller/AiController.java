package com.soft.controller;



import com.soft.dto.AiDto;
import com.soft.pojo.AiAssistant;
import com.soft.pojo.Bed;
import com.soft.pojo.Elderly;
import com.soft.service.AiAssistantService;
import com.soft.service.BedService;
import com.soft.service.ElderlyService;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AiController {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private AiAssistantService aiAssistantService;

    @Autowired
    private BedService bedService;

    @Autowired
    private ElderlyService elderlyService;

    //定义接口实现大模型简单聊天
    @RequestMapping("/chat01")
    public String chat01System(@RequestParam(name="msg") String msg){
        //聊天结果一次性响应
        // chatModel.stream()
        //封装系统角色，指定AI的人社，全局有效
        SystemMessage sysMsg=new SystemMessage("你是一个养老护理专家,给出老人护理建议,其他问题不作出回答");
        //封装用户角色，封装用户和AI的对啊内容
        UserMessage userMsg=new UserMessage(msg);
        String content = chatModel.call(sysMsg,userMsg);
        //String content = chatModel.call(msg);
        return content;
    }
    //通过流式响应
    @RequestMapping(value = "/chat02",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE+";charset=UTF-8")
    public  Flux<String> chat02System(@RequestParam(name="msg") String msg){;

        // chatModel.stream()
        SystemMessage sysMsg=new SystemMessage("你是一个养老护理专家,给出老人护理建议,其他问题不作出回答");
        UserMessage userMsg=new UserMessage(msg);
        //聊天结果一次性响应
        Flux<String> flux = chatModel.stream(sysMsg,userMsg);

        System.out.println(flux);
        return flux;
    }

    //定义获得心理助手接口
    @RequestMapping("/getPsychology")
    public String getPsychologyResult(@RequestBody AiDto dto){
        //封装系统角色，指定AI的人社，全局有效
        SystemMessage sysMsg=new SystemMessage("你是一个老年人心理医生,完成老年人心理辅导,其他文件不做回答");
        //封装用户角色，封装用户和AI的对啊内容
        UserMessage userMsg=new UserMessage(dto.getInputmsg());
        String content = chatModel.call(sysMsg,userMsg);
        //String content = chatModel.call(msg);
        return content;
    }
    /*定义接口保存老人心理AI问询结果*/
    @RequestMapping("/savePsy")
    public Map<String,Object> savePsyResult(@RequestBody AiDto dto){
        Map<String,Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","心理咨询结果保存失败......");

        AiAssistant aiAssistant=new AiAssistant();
        aiAssistant.setOldid(dto.getOldid());
        aiAssistant.setAiresult(dto.getAiresult());
        aiAssistant.setInputmsg(dto.getInputmsg());
        aiAssistant.setCreatetime(new Date());
        aiAssistant.setAitype("心理助手");
        aiAssistantService.save(aiAssistant);
        result.put("code",200);
        result.put("msg","心理咨询结果保存成功......");

        return  result;
    }

    @RequestMapping("/queryInList")
    public Map<String, Object> queryInList() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Bed> beds = bedService.list(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Bed>().eq("status", 1)
            );
            List<Map<String, Object>> list = new java.util.ArrayList<>();
            for (Bed bed : beds) {
                if (bed.getElderlyId() != null) {
                    Elderly elderly = elderlyService.getById(bed.getElderlyId());
                    if (elderly != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", elderly.getId());
                        map.put("oldname", elderly.getRealName());
                        list.add(map);
                    }
                }
            }
            result.put("code", 200);
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败");
        }
        return result;
    }
}
