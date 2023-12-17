package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.origin.UserDto;
import com.lshh.hhp.orm.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class UserBizImpl implements UserBiz {
    final UserRepository userRepository;
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserBiz::toDto)
                .toList();
    }

    @Override
    public Optional<UserDto> find(long id) {
        return userRepository.findById(id).map(UserBiz::toDto);
    }
}
