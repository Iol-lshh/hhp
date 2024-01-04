package com.lshh.hhp.user.service;

import com.lshh.hhp.user.User;

import java.util.List;
import java.util.Optional;

/**
 * level 0
 * The UserService interface provides methods for managing user data.
 */
public interface UserService {
    List<User> findAll();

    Optional<User> find(long userId);
}
