-- 将入住/退住流程中旧的字母护理等级统一为数字护理等级。
UPDATE t_check_in
SET nursing_level = CASE nursing_level
    WHEN 'A特级' THEN '一级护理'
    WHEN 'A特级护理' THEN '一级护理'
    WHEN '特级护理等级' THEN '一级护理'
    WHEN 'A级' THEN '二级护理'
    WHEN 'A级护理' THEN '二级护理'
    WHEN 'B级' THEN '三级护理'
    WHEN 'B级护理' THEN '三级护理'
    WHEN 'C级' THEN '四级护理'
    WHEN 'C级护理' THEN '四级护理'
    WHEN 'D级' THEN '四级护理'
    WHEN 'D级护理' THEN '四级护理'
    ELSE nursing_level
END
WHERE nursing_level IN (
    'A特级', 'A特级护理', '特级护理等级',
    'A级', 'A级护理', 'B级', 'B级护理',
    'C级', 'C级护理', 'D级', 'D级护理'
);

UPDATE t_check_out
SET nursing_level = CASE nursing_level
    WHEN 'A特级' THEN '一级护理'
    WHEN 'A特级护理' THEN '一级护理'
    WHEN '特级护理等级' THEN '一级护理'
    WHEN 'A级' THEN '二级护理'
    WHEN 'A级护理' THEN '二级护理'
    WHEN 'B级' THEN '三级护理'
    WHEN 'B级护理' THEN '三级护理'
    WHEN 'C级' THEN '四级护理'
    WHEN 'C级护理' THEN '四级护理'
    WHEN 'D级' THEN '四级护理'
    WHEN 'D级护理' THEN '四级护理'
    ELSE nursing_level
END
WHERE nursing_level IN (
    'A特级', 'A特级护理', '特级护理等级',
    'A级', 'A级护理', 'B级', 'B级护理',
    'C级', 'C级护理', 'D级', 'D级护理'
);
