package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.NursingTaskMapper;
import com.soft.pojo.*;
import com.soft.service.NursingTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NursingTaskServiceImpl extends ServiceImpl<NursingTaskMapper, NursingTask>
        implements NursingTaskService {

    @Autowired
    private NursingTaskMapper nursingTaskMapper;

    @Autowired
    private com.soft.mapper.OrderMapper orderMapper;

    @Autowired
    private com.soft.mapper.ContractMapper contractMapper;

    @Autowired
    private com.soft.mapper.CheckInMapper checkInMapper;

    @Autowired
    private com.soft.mapper.NursingLevelMapper nursingLevelMapper;

    @Autowired
    private com.soft.mapper.PlainItemMapper plainItemMapper;

    @Autowired
    private com.soft.mapper.BedMapper bedMapper;

    @Autowired
    private com.soft.mapper.BedNurseMapper bedNurseMapper;

    @Override
    public Map<String, Object> queryTaskPageList(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取查询参数
            String elderlyName = (String) params.get("elderlyName");
            String nurseName = (String) params.get("nurseName");
            String nursingItemName = (String) params.get("nursingItemName");
            Integer status = (Integer) params.get("status");
            String startTime = (String) params.get("startTime");
            String endTime = (String) params.get("endTime");
            Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);

            // 创建分页对象
            Page<NursingTask> page = new Page<>(pageNum, pageSize);

            // 执行查询
            List<NursingTask> list = nursingTaskMapper.selectTaskPageList(
                elderlyName, nurseName, nursingItemName, status, startTime, endTime
            );

            // 计算总数
            long total = list.size();

            // 手动分页
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, list.size());
            List<NursingTask> pageList = fromIndex < list.size() ?
                list.subList(fromIndex, toIndex) : List.of();

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("tasks", pageList);
            result.put("total", total);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> executeTask(Integer taskId, String executionRecord,
                                          String executionImage, Integer executorId) {
        Map<String, Object> result = new HashMap<>();

        if (taskId == null || executorId == null) {
            result.put("code", 400);
            result.put("msg", "参数不能为空");
            return result;
        }

        try {
            NursingTask task = this.getById(taskId);
            if (task == null) {
                result.put("code", 400);
                result.put("msg", "任务不存在");
                return result;
            }

            if (task.getStatus() != 0) {
                result.put("code", 400);
                result.put("msg", "任务状态不正确，只有待执行的任务才能执行");
                return result;
            }

            // 更新任务状态和执行信息
            task.setStatus(1);
            task.setExecutorId(executorId);
            task.setExecutionTime(LocalDateTime.now());
            task.setExecutionRecord(executionRecord);
            task.setExecutionImage(executionImage);

            this.updateById(task);

            result.put("code", 200);
            result.put("msg", "执行成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "执行失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> cancelTask(Integer taskId, String cancelReason, Integer cancelerId) {
        Map<String, Object> result = new HashMap<>();

        if (taskId == null || cancelerId == null) {
            result.put("code", 400);
            result.put("msg", "参数不能为空");
            return result;
        }

        if (!StringUtils.hasText(cancelReason)) {
            result.put("code", 400);
            result.put("msg", "取消原因不能为空");
            return result;
        }

        try {
            NursingTask task = this.getById(taskId);
            if (task == null) {
                result.put("code", 400);
                result.put("msg", "任务不存在");
                return result;
            }

            if (task.getStatus() != 0) {
                result.put("code", 400);
                result.put("msg", "任务状态不正确，只有待执行的任务才能取消");
                return result;
            }

            // 更新任务状态和取消信息
            task.setStatus(2);
            task.setCancelerId(cancelerId);
            task.setCancelTime(LocalDateTime.now());
            task.setCancelReason(cancelReason);

            this.updateById(task);

            result.put("code", 200);
            result.put("msg", "取消成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "取消失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Map<String, Object> getTaskDetail(Integer taskId) {
        Map<String, Object> result = new HashMap<>();

        if (taskId == null) {
            result.put("code", 400);
            result.put("msg", "任务ID不能为空");
            return result;
        }

        try {
            NursingTask task = nursingTaskMapper.selectTaskDetail(taskId);
            if (task == null) {
                result.put("code", 400);
                result.put("msg", "任务不存在");
                return result;
            }

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("task", task);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "查询失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> generateTasks() {
        Map<String, Object> result = new HashMap<>();
        int totalCreated = 0;
        long baseTime = System.currentTimeMillis();
        LocalDate today = LocalDate.now();

        try {
            // ========== 来源一：从已支付的订单生成计划外任务（只生成今天的） ==========
            QueryWrapper<Order> orderWrapper = new QueryWrapper<>();
            orderWrapper.eq("status", 3);
            List<Order> paidOrders = orderMapper.selectList(orderWrapper);

            for (Order order : paidOrders) {
                Integer elderlyId = order.getElderlyId();
                Integer nursingItemId = order.getNursingItemId();
                LocalDateTime expectedTime = order.getExpectedServiceTime();

                if (elderlyId == null || nursingItemId == null || expectedTime == null) {
                    continue;
                }

                // 只处理期望时间在今天的订单
                if (!expectedTime.toLocalDate().equals(today)) {
                    continue;
                }

                QueryWrapper<Bed> bedWrapper = new QueryWrapper<>();
                bedWrapper.eq("elderly_id", elderlyId).eq("status", 1);
                Bed bed = bedMapper.selectOne(bedWrapper);

                Integer bedId = bed != null ? bed.getId() : null;

                Integer nurseId = null;
                if (bedId != null) {
                    List<Integer> nurseIds = bedNurseMapper.selectNurseIdsByBedId(bedId);
                    if (nurseIds != null && !nurseIds.isEmpty()) {
                        nurseId = nurseIds.get(0);
                    }
                }

                if (taskExists(elderlyId, nursingItemId, expectedTime)) {
                    continue;
                }

                NursingTask task = new NursingTask();
                task.setTaskNo("RW" + baseTime + "_" + (totalCreated + 1));
                task.setElderlyId(elderlyId);
                task.setBedId(bedId);
                task.setNursingItemId(nursingItemId);
                task.setItemType("护理计划外");
                task.setNurseId(nurseId);
                task.setExpectedServiceTime(expectedTime);
                task.setStatus(0);
                task.setCreator("系统");
                task.setCreateTime(LocalDateTime.now());
                task.setUpdateTime(LocalDateTime.now());
                this.save(task);
                totalCreated++;
            }

            // ========== 来源二：从生效中的合同生成今天的计划内任务 ==========
            QueryWrapper<Contract> contractWrapper = new QueryWrapper<>();
            contractWrapper.eq("status", "生效中");
            List<Contract> activeContracts = contractMapper.selectList(contractWrapper);

            for (Contract contract : activeContracts) {
                Integer checkInId = contract.getCheckInId();
                if (checkInId == null) continue;

                CheckIn checkIn = checkInMapper.selectById(checkInId);
                if (checkIn == null) continue;

                // 不再检查合同日期范围，只要合同生效中就生成今天的任务
                String nursingLevelName = checkIn.getNursingLevel();
                if (nursingLevelName == null || nursingLevelName.isEmpty()) continue;

                QueryWrapper<NursingLevel> levelWrapper = new QueryWrapper<>();
                levelWrapper.eq("levelname", nursingLevelName);
                NursingLevel nursingLevel = nursingLevelMapper.selectOne(levelWrapper);
                if (nursingLevel == null) continue;

                Integer plainId = nursingLevel.getPlainid();
                if (plainId == null) continue;

                QueryWrapper<PlainItem> itemWrapper = new QueryWrapper<>();
                itemWrapper.eq("plain_id", plainId);
                List<PlainItem> plainItems = plainItemMapper.selectList(itemWrapper);
                if (plainItems.isEmpty()) continue;

                QueryWrapper<Bed> bedWrapper = new QueryWrapper<>();
                bedWrapper.eq("elderly_id", checkIn.getElderId()).eq("status", 1);
                Bed bed = bedMapper.selectOne(bedWrapper);
                if (bed == null) continue;

                Integer bedId = bed.getId();

                Integer nurseId = null;
                List<Integer> nurseIds = bedNurseMapper.selectNurseIdsByBedId(bedId);
                if (nurseIds != null && !nurseIds.isEmpty()) {
                    nurseId = nurseIds.get(0);
                }

                // 只生成今天的任务
                for (PlainItem plainItem : plainItems) {
                    Integer itemId = plainItem.getItemId();

                    LocalTime serviceTime = LocalTime.of(8, 0);
                    if (plainItem.getHlsj() != null && !plainItem.getHlsj().isEmpty()) {
                        try {
                            LocalDateTime hlsjTime = LocalDateTime.parse(
                                plainItem.getHlsj(),
                                DateTimeFormatter.ISO_DATE_TIME
                            );
                            serviceTime = hlsjTime.toLocalTime();
                        } catch (Exception e) {
                            // 解析失败使用默认时间
                        }
                    }

                    LocalDateTime expectedTime = LocalDateTime.of(today, serviceTime);

                    if (taskExists(checkIn.getElderId(), itemId, expectedTime)) {
                        continue;
                    }

                    NursingTask task = new NursingTask();
                    task.setTaskNo("RW" + baseTime + "_" + (totalCreated + 1));
                    task.setElderlyId(checkIn.getElderId());
                    task.setBedId(bedId);
                    task.setNursingItemId(itemId);
                    task.setItemType("护理计划内");
                    task.setNurseId(nurseId);
                    task.setExpectedServiceTime(expectedTime);
                    task.setStatus(0);
                    task.setCreator("系统");
                    task.setCreateTime(LocalDateTime.now());
                    task.setUpdateTime(LocalDateTime.now());
                    this.save(task);
                    totalCreated++;
                }
            }

            result.put("code", 200);
            result.put("msg", "任务生成完成，共生成 " + totalCreated + " 条任务");
            result.put("totalCreated", totalCreated);
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "任务生成失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 检查某老人某护理项目在指定日期是否已有任务
     */
    private boolean taskExists(Integer elderlyId, Integer nursingItemId, LocalDateTime expectedTime) {
        LocalDate targetDate = expectedTime.toLocalDate();
        LocalDateTime dayStart = targetDate.atStartOfDay();
        LocalDateTime dayEnd = targetDate.plusDays(1).atStartOfDay();

        QueryWrapper<NursingTask> wrapper = new QueryWrapper<>();
        wrapper.eq("elderly_id", elderlyId)
               .eq("nursing_item_id", nursingItemId)
               .ge("expected_service_time", dayStart)
               .lt("expected_service_time", dayEnd);

        return this.count(wrapper) > 0;
    }
}
