package com.zt.masterSlaveDS.service;

import com.zt.masterSlaveDS.model.User;

/**
 * @Author: ZhouTian
 * @Description:
 * @Date: 2018/9/1
 */
public interface UserService {
    User selectById(Integer id);
}
