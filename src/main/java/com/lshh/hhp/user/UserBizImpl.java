package com.lshh.hhp.user;

import com.lshh.hhp.common.Biz;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Biz
public class UserBizImpl implements UserBiz {
    final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserBiz::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> find(long id) {
        return userRepository.findById(id).map(UserBiz::toDto);
    }
}
