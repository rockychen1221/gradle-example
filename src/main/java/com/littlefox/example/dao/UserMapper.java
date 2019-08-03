package com.littlefox.example.dao;

import com.littlefox.example.model.User;
import com.littlefox.generic.GenericMapper;
import com.littlefox.security.annotation.CrypticField;
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

    User queryByParam(@CrypticField(type = CrypticField.Type.ONLY_ENCRYPT) String id);

    @Override
    User selectByPrimaryKey(@CrypticField(type = CrypticField.Type.ONLY_ENCRYPT) String id);

    @Override
    int deleteByPrimaryKey(@CrypticField(type = CrypticField.Type.ONLY_ENCRYPT) String id);

    User query(@CrypticField(type = CrypticField.Type.ONLY_ENCRYPT) @Param("userId") String id);

}
