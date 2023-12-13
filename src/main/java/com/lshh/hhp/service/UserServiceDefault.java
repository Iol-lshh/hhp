package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.orm.entity.User;
import com.lshh.hhp.orm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceDefault implements UserService{

    final UserRepository userRepository;

    public static UserDto toDto(User entity){
        return new UserDto()
            .id(entity.id())
            .name(entity.name());
    }
    public static User toEntity(UserDto dto){
        return new User()
            .id(dto.id())
            .name(dto.name());
    }

    @Override
    @Transactional
    public ResultDto<UserDto> save(UserDto dto) throws Exception {
        User entity;

        if (Optional.ofNullable(dto.id()).isEmpty()) {
            entity = toEntity(dto);
        } else {
            entity = userRepository.findById(dto.id())
                .orElseThrow(Exception::new);
            entity.name(dto.name() != null ? dto.name() : entity.name());
        }
        entity = userRepository.save(entity);
        return new ResultDto<>(toDto(entity));
    }

    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userRepository
            .findAll()
            .stream()
            .map(UserServiceDefault::toDto)
            .toList();
    }

    @Override
    @Transactional
    public Optional<UserDto> find(long id) {
        return userRepository
            .findById(id)
            .map(UserServiceDefault::toDto);
    }
}
