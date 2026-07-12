package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.PageResult;
import com.soft.pojo.User;
import java.util.Map;

public interface UserService extends IService<User> {
    PageResult<User> findPage(int pageNum, int pageSize, String name, String email, String status, Long deptId);
    void addUser(User user, Long[] roleIds);
    void updateUser(User user, Long[] roleIds);
    void updateStatus(Long id, String status);
    void resetPassword(Long id);
    Long[] getUserRoleIds(Long userId);
    default Object queryUserService(Object userDto, Object session) { return null; }
    default Object updateUserPwdService(Object userPwdDto) { return null; }
}
