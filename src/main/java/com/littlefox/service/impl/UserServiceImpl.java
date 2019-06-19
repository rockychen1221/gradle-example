package com.littlefox.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.littlefox.dao.UserMapper;
import com.littlefox.model.User;
import com.littlefox.service.UserService;
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
}
