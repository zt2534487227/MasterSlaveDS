package com.zt.masterSlaveDS.service.impl;

import com.zt.masterSlaveDS.dao.UserDao;
import com.zt.masterSlaveDS.model.User;
import com.zt.masterSlaveDS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhouTian
 * @Description:
 * @Date: 2018/9/1
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User selectById(Integer id) {
        return userDao.selectById(id);
    }
}
