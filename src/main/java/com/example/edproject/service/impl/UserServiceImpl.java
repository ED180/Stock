package com.example.edproject.service.impl;

import com.example.edproject.dto.Login;
import com.example.edproject.dto.Register;
import com.example.edproject.mapper.UserMapper;
import com.example.edproject.model.User;
import com.example.edproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(Register registerRequest) {
        User user = userMapper.getUserByEmail(registerRequest.getEmail());

        if (user != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用MD5生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(registerRequest.getPassword().getBytes());
        registerRequest.setPassword(hashedPassword);


        userMapper.register(registerRequest);
    }

    @Override
    public User login(Login loginRequest) {

        User user = userMapper.getUserByEmail(loginRequest.getEmail());


        //檢查user是否存在
        if (user == null) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }



        //使用MD5生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(loginRequest.getPassword().getBytes());


        if (user.getPassword().equals(hashedPassword)) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}
