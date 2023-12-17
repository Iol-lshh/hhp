package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.origin.UserDto;
import com.lshh.hhp.orm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserBiz {

    static UserDto toDto(User entity){
        return new UserDto()
                .id(entity.id())
                .name(entity.name());
    }
    List<UserDto> findAll();

    Optional<UserDto> find(long userId);
}
