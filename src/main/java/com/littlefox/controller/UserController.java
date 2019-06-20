/*
 * All rights Reserved, Designed By DataDriver
 * Copyright:    DataDriver.Inc
 * Company:      Zhuo Wo Infomation Technology (ShangHai) CO.LTD
 */
package com.littlefox.controller;

import com.github.pagehelper.PageInfo;
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

    @RequestMapping(value = "/queryPageList",method = RequestMethod.GET)
    public String queryList(){
        long startTime = System.currentTimeMillis();    //获取开始时间
        //List<User> users=userService.queryList(new HashMap<>());
        //users.forEach(user -> System.out.println(user.toString()));
        PageInfo<User> users2 = userService.selectPageUserList(User.builder().userName("userName5").build(), 0, 10);
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        users2.getList().forEach(user2 -> System.out.println(user2.toString()));

        return "";
    }
}
