package com.neuedu.busines.dao;

import com.neuedu.busines.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectByUserName (String username);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int countUsername(@Param("username") String username); //判断用户名是否重复

    int countEmail(@Param("email") String email); //判断邮箱是否重复

    User findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);//用户登录
}
