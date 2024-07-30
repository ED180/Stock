package com.example.edproject.controller;


import com.example.edproject.cache.OrderCache;
import com.example.edproject.dto.Login;
import com.example.edproject.dto.Register;
import com.example.edproject.model.User;
import com.example.edproject.result.Result;
import com.example.edproject.result.ResultCode;
import com.example.edproject.service.UserService;
import com.example.edproject.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderCache orderCache;

    @PostMapping("/register")
    public Result register(@RequestBody @Valid Register registerRequest) {
        userService.register(registerRequest);

        return Result.genSuccessResult();
    }

    @PostMapping("/login")
    public Result login(@RequestBody @Valid Login loginRequest) {
        User user = userService.login(loginRequest);

        if (user != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", loginRequest.getEmail());
            claims.put("password", loginRequest.getPassword());

            String token = JwtUtil.genToken(claims);

            orderCache.login(token);

            return Result.genSuccessResult(token);
        } else {
            return Result.genFailResult(ResultCode.FAIL);
        }
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {

//        if (user != null) {
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("email", loginRequest.getEmail());
//            claims.put("password", loginRequest.getPassword());
//
//            String token = JwtUtil.genToken(claims);
//
//            orderCache.login(token);
//
//            return Result.genSuccessResult(token);
//        } else {
//            return Result.genFailResult(ResultCode.FAIL);
//        }

        String token = request.getHeader("Authorization");

        orderCache.logout(token);

        return Result.genSuccessResult();
    }


}
