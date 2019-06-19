/*
 * All rights Reserved, Designed By DataDriver
 * Copyright:    DataDriver.Inc
 * Company:      Zhuo Wo Infomation Technology (ShangHai) CO.LTD
 */
package com.littlefox.controller;

import com.littlefox.model.User;
import com.littlefox.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/index.do",method = RequestMethod.GET)
    public String index(){
        return "user/user";
    }


    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String insert(@ModelAttribute("user")User user){
        userService.insert(user);
        return "";
    }

    @RequestMapping(value = "/queryList",method = RequestMethod.POST)
    public String queryList(@ModelAttribute("user")User user){
        userService.queryList(null);
        return "";
    }
}
