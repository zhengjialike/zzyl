package com.soft.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soft.pojo.BedNurse;
import com.soft.pojo.User;
import com.soft.pojo.Bed;
import com.soft.service.BedNurseService;
import com.zzyl.mapper.UserMapper;
import com.soft.mapper.BedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class BedNurseController {

    @Autowired private BedNurseService bedNurseService;
    @Autowired private UserMapper userMapper;
    @Autowired private BedMapper bedMapper;

    @RequestMapping("/bedNursePage")
    public Map<String, Object> pageList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "50") int pageSize) {
        LambdaQueryWrapper<BedNurse> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(BedNurse::getBedId);
        List<BedNurse> records = bedNurseService.list(wrapper);

        List<Map<String, Object>> list = new ArrayList<>();
        for (BedNurse bn : records) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", bn.getId());
            m.put("bedId", bn.getBedId());
            Bed b = bedMapper.selectById(bn.getBedId());
            m.put("bedNo", b != null ? b.getBedNumber() : "");
            m.put("nurseId", bn.getNurseId());
            User u = userMapper.selectById(bn.getNurseId());
            m.put("nurseName", u != null ? u.getRealname() : "");
            m.put("createTime", bn.getCreateTime());
            list.add(m);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", list.size());
        return result;
    }

    @PostMapping("/bedNurse/add")
    public Map<String, Object> add(@RequestBody BedNurse bn) {
        bedNurseService.save(bn);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 200);
        r.put("msg", "success");
        return r;
    }

    @PostMapping("/bedNurse/delete")
    public Map<String, Object> delete(@RequestBody Map<String, Integer> body) {
        bedNurseService.removeById(body.get("id"));
        Map<String, Object> r = new HashMap<>();
        r.put("code", 200);
        r.put("msg", "success");
        return r;
    }
}
