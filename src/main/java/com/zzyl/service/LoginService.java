package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.User;
import java.util.Map;

public interface LoginService {
    Map<String, Object> login(String account, String upwd);
    Map<String, Object> loadInfo(Long userId);
    void logout();
}
