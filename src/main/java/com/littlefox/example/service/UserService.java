package com.littlefox.example.service;

import com.github.pagehelper.PageInfo;
import com.littlefox.example.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 查询
     * @param map
     */
    List<User> queryList(Map<String, String> map);

    /**
     * 写入数据
     * @param user
     */
    void insert(User user);


    List<User> queryUserList(User user);


    PageInfo<User> selectPageUserList (User costBudget, int pageNum, int pageSize);


    void update(User user);


    void delete(User user);


    User queryById(User user);


}
