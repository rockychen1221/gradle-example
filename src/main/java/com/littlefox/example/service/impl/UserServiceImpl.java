package com.littlefox.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.littlefox.example.dao.UserMapper;
import com.littlefox.example.model.User;
import com.littlefox.example.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public List<User> queryList(Map<String, String> map) {
        return userMapper.queryList(map);
    }

    @Override
    public List<User> queryUserList(User user) {
        return userMapper.queryUserList(user);
    }

    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public PageInfo<User> selectPageUserList(User user, int pageNum, int pageSize) {
        //分页
        PageHelper.startPage(pageNum,pageSize,true);
        //查询数据
        List<User> list = userMapper.queryUserList(user);
        //返回分页后集合
        return new PageInfo<User>(list);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);

        User user1=userMapper.queryById(User.builder().id("5C070830295AD870F3DC867AB24CA22B").build());
        User user2=userMapper.queryById(User.builder().id("5C070830295AD870F3DC867AB24CA22B").build());

        System.out.println(user1.toString());
        System.out.println(user2.toString());

    }

    @Override
    public void delete(User user) {
        userMapper.delete(user);
    }


    @Override
    public User queryById(User user) {
        return userMapper.queryById(user);
    }
}
