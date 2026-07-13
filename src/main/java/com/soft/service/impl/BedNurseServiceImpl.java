package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.BedNurseMapper;
import com.soft.pojo.Bed;
import com.soft.pojo.BedNurse;
import com.soft.service.BedNurseService;
import com.soft.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BedNurseServiceImpl extends ServiceImpl<BedNurseMapper, BedNurse>
        implements BedNurseService {

    @Autowired
    private BedNurseMapper bedNurseMapper;

    @Autowired
    private BedService bedService;

    @Override
    @Transactional
    public Map<String, Object> setBedNurses(Integer bedId, List<Integer> nurseIds) {
        Map<String, Object> result = new HashMap<>();

        if (bedId == null || nurseIds == null || nurseIds.isEmpty()) {
            result.put("code", 400);
            result.put("msg", "参数不能为空");
            return result;
        }

        if (nurseIds.size() > 4) {
            result.put("code", 400);
            result.put("msg", "最多只能选择4个护理员");
            return result;
        }

        try {
            // 先删除旧的关联
            bedNurseMapper.deleteByBedId(bedId);

            // 再插入新的关联
            bedNurseMapper.batchInsertBedNurses(bedId, nurseIds);

            result.put("code", 200);
            result.put("msg", "设置成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "设置失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> batchSetRoomNurses(Integer roomId, List<Integer> nurseIds) {
        Map<String, Object> result = new HashMap<>();

        if (roomId == null || nurseIds == null || nurseIds.isEmpty()) {
            result.put("code", 400);
            result.put("msg", "参数不能为空");
            return result;
        }

        if (nurseIds.size() > 4) {
            result.put("code", 400);
            result.put("msg", "最多只能选择4个护理员");
            return result;
        }

        try {
            List<Bed> beds = bedService.getBedsByRoomId(roomId);
            if (beds == null || beds.isEmpty()) {
                result.put("code", 400);
                result.put("msg", "该房间没有床位");
                return result;
            }

            for (Bed bed : beds) {
                bedNurseMapper.deleteByBedId(bed.getId());
                bedNurseMapper.batchInsertBedNurses(bed.getId(), nurseIds);
            }

            result.put("code", 200);
            result.put("msg", "批量设置成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "批量设置失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Integer> getNurseIdsByBedId(Integer bedId) {
        if (bedId == null) {
            return List.of();
        }

        try {
            return bedNurseMapper.selectNurseIdsByBedId(bedId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
