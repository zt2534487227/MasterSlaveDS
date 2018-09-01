package com.zt.masterSlaveDS.controller;

import com.zt.masterSlaveDS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ZhouTian
 * @Description:
 * @Date: 2018/9/1
 */
@RestController
@RequestMapping("/")
public class DemoController {
    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    public void demo(){
        userService.selectById(1);
    }

}
