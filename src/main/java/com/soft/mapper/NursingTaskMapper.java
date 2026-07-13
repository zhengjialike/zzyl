package com.soft.mapper;

import com.soft.pojo.NursingTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 12
* @description 针对表【t_nursing_task(护理任务表)】的数据库操作Mapper
* @createDate 2026-07-13 09:53:47
* @Entity com.soft.pojo.NursingTask
*/
public interface NursingTaskMapper extends BaseMapper<NursingTask> {
    /**
     * 分页查询护理任务列表
     */
    List<NursingTask> selectTaskPageList(@Param("elderlyName") String elderlyName,
                                         @Param("nurseName") String nurseName,
                                         @Param("nursingItemName") String nursingItemName,
                                         @Param("status") Integer status,
                                         @Param("startTime") String startTime,
                                         @Param("endTime") String endTime);

    /**
     * 根据任务ID查询任务详情（包含关联信息）
     */
    NursingTask selectTaskDetail(@Param("taskId") Integer taskId);

}




