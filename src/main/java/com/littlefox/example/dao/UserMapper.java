package com.littlefox.example.dao;

import com.littlefox.example.model.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    int insert(User user);

    List<User> queryList(Map<String, String> map);

    List<User> queryUserList(User user);

    int update(User user);

    int delete(User user);

    User queryById(User user);

}
