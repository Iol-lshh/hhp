package com.lshh.hhp.domain.user;

import com.lshh.hhp.common.annotation.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService{
    final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Optional<User> find(long id) {
        return userRepository.findById(id);
    }
}
