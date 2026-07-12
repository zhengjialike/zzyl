package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.Nursing.NursingItemDto;
import com.soft.pojo.NursingItem;
import com.soft.mapper.NursingItemMapper;
import com.soft.service.NursingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NursingItemServiceImpl extends ServiceImpl<NursingItemMapper, NursingItem>
        implements NursingItemService {

    @Autowired
    private NursingItemMapper nursingItemMapper;

    @Override
    public Map<String, Object> queryNursingItemList(NursingItemDto nursingItemDto) {
        // 1. 创建分页对象（注意：必须传入页码和每页大小）
        Page<NursingItem> page = new Page<>(nursingItemDto.getPageNum(), nursingItemDto.getPageSize());

        // 2. 创建条件构造器
        QueryWrapper<NursingItem> wrapper = new QueryWrapper<>();
        String itemname = nursingItemDto.getItemname();
        String islock = nursingItemDto.getIslock();

        // 3. 添加条件（非空时生效，使用 StringUtils.hasText 更安全）
        if (StringUtils.hasText(itemname)) {
            wrapper.eq("itemname", itemname);  // 如需模糊查询可改为 .like("itemname", itemname)
        }
        if (StringUtils.hasText(islock)) {
            wrapper.eq("islock", islock);
        }

        // 4. 执行分页查询（注意：使用 selectPage，返回 IPage 对象）
        IPage<NursingItem> iPage = nursingItemMapper.selectPage(page, wrapper);
        List<NursingItem> nursingItems = iPage.getRecords();

        // 5. 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", iPage.getTotal());
        result.put("nursingItems", nursingItems);
        return result;
    }
}


