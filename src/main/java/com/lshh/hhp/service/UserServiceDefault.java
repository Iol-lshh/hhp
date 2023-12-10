package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.orm.entity.User;

import java.util.List;
import java.util.Optional;

public class UserServiceDefault implements UserService{

    public UserDto toDto(User entity){
        return new UserDto()
            .id(entity.id())
            .name(entity.name());
    }
    public User toEntity(UserDto dto){
        return new User()
            .id(dto.id())
            .name(dto.name());
    }

    @Override
    public Response.Result save(UserDto dto) {
        return null;
    }

    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public Optional<UserDto> find(long userId) {
        return Optional.empty();
    }
}
