package com.littlefox.example.dao;

import com.littlefox.example.model.User;
import com.littlefox.generic.GenericMapper;
import com.littlefox.security.annotation.Cryptic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper extends GenericMapper<User,String> {

    int insert(User user);

    List<User> queryList(Map<String, String> map);

    List<User> queryUserList(User user);

    int update(User user);

    int delete(User user);

    User queryById(User user);

    List<User> query();

    User queryByParam(@Cryptic String id);

    @Override
    User selectByPrimaryKey(@Cryptic String id);

    @Override
    int deleteByPrimaryKey(@Cryptic String id);

    User query(@Cryptic @Param("userId") String id);

    User queryUserRole(@Cryptic @Param("userId") String id);

}
