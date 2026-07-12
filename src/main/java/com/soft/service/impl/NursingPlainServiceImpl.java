package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.dto.Nursing.NursingPlainDto;
import com.soft.dto.Nursing.NursingPlainPageDto;
import com.soft.dto.Nursing.PlainItemDto;
import com.soft.mapper.PlainItemMapper;
import com.soft.pojo.NursingPlain;
import com.soft.pojo.PlainItem;
import com.soft.service.NursingPlainService;
import com.soft.mapper.NursingPlainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Teacher
 * @description 针对表【t_nursing_plain】的数据库操作Service实现
 * @createDate 2026-07-09 14:28:30
 */
@Service
public class NursingPlainServiceImpl extends ServiceImpl<NursingPlainMapper, NursingPlain>
        implements NursingPlainService{

    //注册mapper代理对象
    @Autowired
    private NursingPlainMapper nursingPlainMapper;
    @Autowired
    private PlainItemMapper plainItemMapper;

    @Transactional
    @Override
    public Map<String, Object> saveNursingPlainService(NursingPlainDto nursingPlainDto) {
        Map<String, Object> result=new HashMap<>();

        result.put("code",400);
        result.put("msg","保存护理计划失败......");

        //1 保存护理计划信息
        NursingPlain nursingPlain=new NursingPlain();
        System.out.println("1-----"+nursingPlain.getId());
        nursingPlain.setPlainname(nursingPlainDto.getPlainname());
        nursingPlain.setCreatetime(new Date());
        nursingPlain.setCreateuser("马云");
        nursingPlain.setIslock("启动");
        nursingPlainMapper.insert(nursingPlain);
        //可以获得数据库自增的id
        System.out.println("2-----"+nursingPlain.getId());


        //获得当前的护理计划下的所有护理项
        List<PlainItemDto> plainItemList = nursingPlainDto.getPlainItemList();
        plainItemList.forEach(item->{
            //创建中间表对应的实体类对象
            PlainItem plainItem=new PlainItem();
            plainItem.setPlainId(nursingPlain.getId());
            plainItem.setItemId(item.getItemid());
            plainItem.setHlsj(item.getHlsj());
            plainItem.setHlzq(item.getHlzq());
            plainItem.setHlpc(item.getHlpc());
            plainItem.setItemname(item.getHlmc());
            //2 保存护理计划和护理项之间的关系
            plainItemMapper.insert(plainItem);

        });
        result.put("code",200);
        result.put("msg","保存护理计划成功......");
        return result;
    }

    @Override
    public Map<String, Object> updateNursingPlainService(NursingPlainDto nursingPlainDto) {
        Map<String, Object> result=new HashMap<>();
        result.put("code",400);
        result.put("msg","更新护理计划失败......");
        //1 保存护理计划信息
        NursingPlain nursingPlain=new NursingPlain();
        nursingPlain.setId(nursingPlainDto.getId());
        nursingPlain.setPlainname(nursingPlainDto.getPlainname());
        nursingPlain.setCreatetime(new Date());
        nursingPlain.setCreateuser("马云");
        nursingPlain.setIslock("启动");
        nursingPlainMapper.updateById(nursingPlain);

        //获得当前的护理计划下的所有护理项
        List<PlainItemDto> plainItemList = nursingPlainDto.getPlainItemList();
        plainItemList.forEach(item->{
            //创建中间表对应的实体类对象
            PlainItem plainItem=new PlainItem();
            plainItem.setPlainId(nursingPlain.getId());
            plainItem.setItemId(item.getItemid());
            plainItem.setHlsj(item.getHlsj());
            plainItem.setHlzq(item.getHlzq());
            plainItem.setHlpc(item.getHlpc());
            plainItem.setItemname(item.getHlmc());
            //2 保存护理计划和护理项之间的关系
            plainItemMapper.insert(plainItem);

        });
        result.put("code",200);
        result.put("msg","更新护理计划成功......");
        return result;
    }

    @Override
    public Map<String, Object> loadNursingListPageService(
            NursingPlainPageDto dto) {
        Map<String, Object> result=new HashMap<>();
        //指定分页查询参数
        Page<NursingPlain> page=new Page<>(dto.getPageNum(),dto.getPageSize());
        //创建params封装分页查询参数
        QueryWrapper<NursingPlain> params=new QueryWrapper<>();
        String plainname = dto.getPlainname();
        String islock = dto.getIslock();
        params.eq(!StringUtils.isBlank(plainname),"plainname",plainname);
        params.eq(!StringUtils.isBlank(islock),"islock",islock);
        List<NursingPlain> nursingPlains = nursingPlainMapper.selectList(page, params);
        nursingPlains.forEach(item->{
            //获得每个护理计划id
            Integer id = item.getId();
            //根据id查询中间表中某个护理计划对应的所有护理项
            QueryWrapper<PlainItem> wrapper=new QueryWrapper<>();
            wrapper.eq("plain_id",id);
            Long value = plainItemMapper.selectCount(wrapper);
            if(value!=0){
                //该护理计划下存在护理项
                item.setFlag(1);
            }
        });

        result.put("nursingPlains",nursingPlains);
        result.put("total",page.getTotal());
        return result;
    }

    @Override
    public Double totalPlainItemPayService(Integer id) {

        return nursingPlainMapper.totalPlainItemPayMapper(id);

    }
}




