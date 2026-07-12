package com.soft.mapper;

import com.soft.pojo.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 12
* @description 针对表【t_customer(客户/家属表（小程序使用者）)】的数据库操作Mapper
* @createDate 2026-07-11 11:02:13
* @Entity com.soft.pojo.Customer
*/
public interface CustomerMapper extends BaseMapper<Customer> {
    /**
     * 检查客户是否已签约（是否存在相关合同）
     */
    boolean checkContractExists(@Param("phone") String phone);

    /**
     * 查询客户的订单数量（状态为已完成或待执行）
     */
    Integer getOrderCountByPhone(@Param("phone") String phone);

    /**
     * 查询客户绑定的所有老人姓名
     */
    List<String> getBoundElderlyNames(@Param("customerId") Integer customerId);

}




