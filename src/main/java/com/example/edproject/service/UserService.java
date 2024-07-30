package com.example.edproject.service;

import com.example.edproject.dto.Login;
import com.example.edproject.dto.Register;
import com.example.edproject.model.User;

public interface UserService {
    void register(Register registerRequest);

    User login(Login loginRequest);
}
