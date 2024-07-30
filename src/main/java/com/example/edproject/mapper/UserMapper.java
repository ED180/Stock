package com.example.edproject.mapper;

import com.example.edproject.dto.Register;
import com.example.edproject.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE email = #{email}")
    User getUserByEmail(@Param("email")String email);

    @Insert("INSERT INTO users (email, password, totalAmount, registeredDate) VALUES (#{register.email}, #{register.password}, #{register.totalAmount}, NOW())")
    void register(@Param("register") Register register);
}
