package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUserByUsername(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) " +
            "VALUES (#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys =true, keyProperty = "userId")
    Integer insert(User user);

    @Update("UPDATE USERS SET userid = #{userId}, username = #{username}, salt = #{salt}, password = #{password}, firstname = #{firstname}, lastname = #{lastname}")
    void updateUser(User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userId}")
    void deleteUser(int userId);
}
