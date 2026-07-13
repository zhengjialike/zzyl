package com.soft.controller;

import com.soft.service.BedNurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bedNurse")
public class BedNurseController {

    @Autowired
    private BedNurseService bedNurseService;

    /**
     * 设置床位护理员
     */
    @PostMapping("/set")
    public Map<String, Object> set(@RequestParam("bedId") Integer bedId,
                                   @RequestBody List<Integer> nurseIds) {
        return bedNurseService.setBedNurses(bedId, nurseIds);
    }

    /**
     * 批量设置房间护理员
     */
    @PostMapping("/batchSet")
    public Map<String, Object> batchSet(@RequestParam("roomId") Integer roomId,
                                        @RequestBody List<Integer> nurseIds) {
        return bedNurseService.batchSetRoomNurses(roomId, nurseIds);
    }

    /**
     * 查询床位护理员列表
     */
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam("bedId") Integer bedId) {
        List<Integer> nurseIds = bedNurseService.getNurseIdsByBedId(bedId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("nurseIds", nurseIds);
        return result;
    }
}
