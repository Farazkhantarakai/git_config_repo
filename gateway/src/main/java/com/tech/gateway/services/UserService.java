package com.tech.gateway.services;

import com.tech.gateway.model.MyUser;

import java.util.Optional;

public interface UserService {

    public Optional<MyUser> findByEmail(String email);
    public void save(MyUser user);

}
