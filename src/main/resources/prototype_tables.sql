-- 原型全功能补齐表（基于原型的缺失表）
USE zzyl;
SET NAMES utf8mb4;

-- 1. 合同表
CREATE TABLE IF NOT EXISTS t_contract (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  contract_no varchar(50) COMMENT '合同编号',
  contract_name varchar(100) COMMENT '合同名称',
  elderly_id bigint COMMENT '关联老人ID',
  checkin_id bigint COMMENT '关联入住单ID',
  party_a varchar(50) COMMENT '甲方（养老院）',
  party_b varchar(50) COMMENT '乙方（老人/家属）',
  party_c varchar(50) COMMENT '丙方姓名',
  party_c_phone varchar(20) COMMENT '丙方联系方式',
  start_date date COMMENT '合同开始日期',
  end_date date COMMENT '合同结束日期',
  sign_date date COMMENT '签约日期',
  contract_file varchar(255) COMMENT '合同文件路径',
  status tinyint DEFAULT 0 COMMENT '状态：0-未生效 1-生效中 2-已过期 3-已失效',
  termination_date date COMMENT '解除日期',
  termination_reason varchar(500) COMMENT '解除原因',
  creator varchar(50) COMMENT '创建人',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';

-- 2. 入住申请表
CREATE TABLE IF NOT EXISTS t_checkin (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  checkin_no varchar(50) COMMENT '入住单号',
  elderly_id bigint COMMENT '关联老人ID',
  status tinyint DEFAULT 0 COMMENT '状态：0-申请中 1-评估中 2-审批中 3-配置中 4-签约中 5-已完成 6-已关闭',
  applicant bigint COMMENT '申请人ID',
  applied_at datetime COMMENT '申请时间',
  completed_at datetime COMMENT '完成时间',
  remark varchar(500) COMMENT '备注',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入住申请表';

-- 3. 入住配置表
CREATE TABLE IF NOT EXISTS t_checkin_config (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  checkin_id bigint COMMENT '关联入住申请ID',
  stay_start_date date COMMENT '入住期限开始',
  stay_end_date date COMMENT '入住期限结束',
  bed_id bigint COMMENT '关联床位ID',
  nursing_level_id bigint COMMENT '关联护理等级ID',
  deposit_amount decimal(10,2) DEFAULT 0 COMMENT '押金',
  nursing_fee decimal(10,2) DEFAULT 0 COMMENT '护理费用/月',
  bed_fee decimal(10,2) DEFAULT 0 COMMENT '床位费用/月',
  other_fee decimal(10,2) DEFAULT 0 COMMENT '其他费用/月',
  medical_insurance decimal(10,2) DEFAULT 0 COMMENT '医保支付/月',
  government_subsidy decimal(10,2) DEFAULT 0 COMMENT '政府补贴/月',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入住配置表';

-- 4. 健康评估表
CREATE TABLE IF NOT EXISTS t_elderly_assessment (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  checkin_id bigint COMMENT '关联入住申请ID',
  elderly_id bigint COMMENT '关联老人ID',
  disease_diagnosis varchar(500) COMMENT '疾病诊断',
  medication_info text COMMENT '用药情况',
  fall_risk tinyint DEFAULT 0 COMMENT '跌倒风险',
  wander_risk tinyint DEFAULT 0 COMMENT '走失风险',
  choke_risk tinyint DEFAULT 0 COMMENT '噎食风险',
  suicide_risk tinyint DEFAULT 0 COMMENT '自杀风险',
  coma_risk tinyint DEFAULT 0 COMMENT '昏迷风险',
  wound_status varchar(200) COMMENT '伤口情况',
  special_care varchar(200) COMMENT '特殊医疗照护',
  self_care_ability varchar(20) COMMENT '自理能力',
  dementia_signs varchar(200) COMMENT '痴呆前兆',
  health_report_file varchar(255) COMMENT '体检报告文件',
  assessed_by bigint COMMENT '评估人ID',
  assessed_at datetime COMMENT '评估时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康评估表';

-- 5. 能力评估表（Barthel指数）
CREATE TABLE IF NOT EXISTS t_elderly_ability (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  checkin_id bigint COMMENT '关联入住申请ID',
  elderly_id bigint COMMENT '关联老人ID',
  eating_score tinyint DEFAULT 0 COMMENT '进食评分',
  bathing_score tinyint DEFAULT 0 COMMENT '洗澡评分',
  grooming_score tinyint DEFAULT 0 COMMENT '修饰评分',
  dressing_score tinyint DEFAULT 0 COMMENT '穿衣评分',
  bowel_control tinyint DEFAULT 0 COMMENT '大便控制',
  bladder_control tinyint DEFAULT 0 COMMENT '小便控制',
  toilet_score tinyint DEFAULT 0 COMMENT '如厕评分',
  bed_transfer_score tinyint DEFAULT 0 COMMENT '床椅转移',
  walking_score tinyint DEFAULT 0 COMMENT '平地行走',
  stairs_score tinyint DEFAULT 0 COMMENT '上下楼梯',
  total_score int DEFAULT 0 COMMENT '总分',
  ability_level varchar(20) COMMENT '能力等级',
  assessed_by bigint COMMENT '评估人ID',
  assessed_at datetime COMMENT '评估时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='能力评估表';

-- 6. 评估报告表
CREATE TABLE IF NOT EXISTS t_assessment_report (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  checkin_id bigint COMMENT '关联入住申请ID',
  elderly_id bigint COMMENT '关联老人ID',
  health_assessment_id bigint COMMENT '关联健康评估ID',
  ability_assessment_id bigint COMMENT '关联能力评估ID',
  report_content text COMMENT '报告内容',
  generated_by bigint COMMENT '生成人ID',
  generated_at datetime COMMENT '生成时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评估报告表';

-- 7. 退住申请表
CREATE TABLE IF NOT EXISTS t_checkout (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  checkout_no varchar(50) COMMENT '退住单号',
  elderly_id bigint COMMENT '关联老人ID',
  contract_id bigint COMMENT '关联合同ID',
  checkout_date date COMMENT '退住日期',
  reason varchar(200) COMMENT '退住原因',
  remark varchar(500) COMMENT '备注',
  status tinyint DEFAULT 0 COMMENT '状态：0-申请中 1-审批中 2-解除合同中 3-调整账单中 4-账单审批中 5-退住审批中 6-费用清算中 7-已完成 8-已关闭',
  applicant bigint COMMENT '申请人ID',
  applied_at datetime COMMENT '申请时间',
  settlement_status tinyint DEFAULT 0 COMMENT '费用清算：0-未结清 1-已结清',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退住申请表';

-- 8. 订单表
CREATE TABLE IF NOT EXISTS t_order (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  order_no varchar(50) COMMENT '订单编号',
  elderly_id bigint COMMENT '关联老人ID',
  customer_id bigint COMMENT '下单客户ID',
  order_amount decimal(10,2) DEFAULT 0 COMMENT '订单金额',
  status tinyint DEFAULT 0 COMMENT '状态：0-待支付 1-待执行 2-已执行 3-已完成 4-已退款 5-已关闭',
  bed_no varchar(20) COMMENT '床位号',
  nursing_item_id bigint COMMENT '关联护理项目ID',
  expected_service_time datetime COMMENT '期望服务时间',
  creator varchar(50) COMMENT '创建人',
  cancel_reason varchar(200) COMMENT '取消原因',
  paid_at datetime COMMENT '支付时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 9. 订单明细表
CREATE TABLE IF NOT EXISTS t_order_item (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  order_id bigint COMMENT '关联订单ID',
  item_name varchar(100) COMMENT '项目名称',
  unit_price decimal(10,2) DEFAULT 0 COMMENT '单价',
  quantity int DEFAULT 1 COMMENT '数量',
  subtotal decimal(10,2) DEFAULT 0 COMMENT '小计',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 10. 退款记录表
CREATE TABLE IF NOT EXISTS t_refund (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  refund_no varchar(50) COMMENT '退款编号',
  order_id bigint COMMENT '关联订单ID',
  refund_amount decimal(10,2) DEFAULT 0 COMMENT '退款金额',
  status tinyint DEFAULT 0 COMMENT '状态：0-处理中 1-成功 2-失败',
  applicant_type tinyint DEFAULT 0 COMMENT '申请人类型：0-前台 1-后台',
  applicant varchar(50) COMMENT '申请人',
  refund_reason varchar(500) COMMENT '退款原因',
  refund_channel varchar(20) COMMENT '退款渠道',
  refund_method varchar(20) COMMENT '退款方式',
  applied_at datetime COMMENT '申请时间',
  refunded_at datetime COMMENT '退款时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款记录表';

-- 11. 护理任务表
CREATE TABLE IF NOT EXISTS t_nursing_task (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  elderly_id bigint COMMENT '关联老人ID',
  nursing_item_id bigint COMMENT '关联护理项目ID',
  task_type tinyint DEFAULT 0 COMMENT '类型：0-计划内 1-计划外(订单)',
  order_id bigint COMMENT '关联订单ID（计划外）',
  bill_id bigint COMMENT '关联账单ID（计划内）',
  expected_time datetime COMMENT '期望服务时间',
  status tinyint DEFAULT 0 COMMENT '状态：0-待执行 1-已执行 2-已取消',
  assigned_to bigint COMMENT '护理员ID',
  cancel_reason varchar(500) COMMENT '取消原因',
  creator varchar(50) COMMENT '创建人',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理任务表';

-- 12. 任务执行记录表
CREATE TABLE IF NOT EXISTS t_task_execution (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  task_id bigint COMMENT '关联护理任务ID',
  execute_time datetime COMMENT '执行时间',
  execute_record varchar(500) COMMENT '执行记录',
  execute_images text COMMENT '执行图片(JSON数组)',
  executor bigint COMMENT '执行人ID',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务执行记录表';

-- 13. 工作流实例表
CREATE TABLE IF NOT EXISTS t_workflow_instance (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  process_type varchar(20) COMMENT '流程类型：checkin/checkout/leave',
  business_id bigint COMMENT '业务主键ID',
  business_no varchar(50) COMMENT '业务单据编号',
  title varchar(200) COMMENT '单据标题',
  applicant bigint COMMENT '申请人ID',
  status tinyint DEFAULT 0 COMMENT '状态：0-申请中 1-已完成 2-已关闭',
  current_node varchar(50) COMMENT '当前节点',
  applied_at datetime COMMENT '申请时间',
  completed_at datetime COMMENT '完成时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作流实例表';

-- 14. 工作流节点/审批记录表
CREATE TABLE IF NOT EXISTS t_workflow_node (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  instance_id bigint COMMENT '关联工作流实例ID',
  node_name varchar(50) COMMENT '节点名称',
  node_order int DEFAULT 0 COMMENT '节点顺序',
  assignee bigint COMMENT '处理人ID',
  status tinyint DEFAULT 0 COMMENT '状态：0-待处理 1-已处理 2-已跳过',
  action varchar(20) COMMENT '审批动作',
  comment varchar(500) COMMENT '审批意见',
  processed_at datetime COMMENT '处理时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作流节点表';

-- 15. 老人档案附件表
CREATE TABLE IF NOT EXISTS t_elderly_file (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  elderly_id bigint COMMENT '关联老人ID',
  checkin_id bigint COMMENT '关联入住申请ID',
  file_type varchar(50) COMMENT '文件类型',
  file_name varchar(255) COMMENT '文件名',
  file_path varchar(255) COMMENT '文件路径',
  file_size bigint DEFAULT 0 COMMENT '文件大小',
  uploaded_by bigint COMMENT '上传人ID',
  uploaded_at datetime COMMENT '上传时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='老人档案附件表';

-- 16. 设备事件表
CREATE TABLE IF NOT EXISTS t_device_event (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  device_id bigint COMMENT '关联设备ID',
  event_name varchar(100) COMMENT '事件名称',
  event_type varchar(50) COMMENT '事件类型',
  event_data text COMMENT '事件数据(JSON)',
  event_time datetime COMMENT '事件发生时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备事件表';

-- 17. 设备属性表
CREATE TABLE IF NOT EXISTS t_device_property (
  id bigint AUTO_INCREMENT PRIMARY KEY,
  device_id bigint COMMENT '关联设备ID',
  property_name varchar(100) COMMENT '属性名称',
  property_value varchar(255) COMMENT '属性值',
  report_time datetime COMMENT '上报时间',
  create_time datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备属性表';

SELECT 'All 17 tables created' AS result;
SHOW TABLES;
