package com.hoainam.service;

import java.util.List; 

import com.hoainam.entity.User;

public interface UserService {
    User findById(String username);

    User findByEmail(String email);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void delete(String username);

    User login(String username, String password);

    // Register
    boolean register(User user);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExitsPhone(String phone);

    boolean updatePassword(String email, String password);
}
