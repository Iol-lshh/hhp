package com.lshh.hhp.user.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.user.User;
import com.lshh.hhp.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> find(long id) {
        return userRepository.findById(id);
    }
}
