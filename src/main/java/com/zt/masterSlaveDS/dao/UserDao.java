package com.zt.masterSlaveDS.dao;

import com.zt.masterSlaveDS.model.User;
import org.springframework.stereotype.Repository;

/**
 * @Author: ZhouTian
 * @Description:
 * @Date: 2018/9/1
 */
@Repository
public interface UserDao {

    User selectById(Integer id);
}
