package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.orm.entity.User;
import com.lshh.hhp.orm.repository.UserRepository;
import com.lshh.hhp.service.component.UserComponent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceDefault implements UserService{

    final UserComponent userComponent;

    @Override
    @Transactional
    public ResultDto<UserDto> save(UserDto dto) throws Exception {
        return new ResultDto<>(userComponent.save(dto));
    }

    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userComponent.findAll();
    }
}
