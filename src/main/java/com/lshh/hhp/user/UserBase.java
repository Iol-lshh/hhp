package com.lshh.hhp.user;

import java.util.List;
import java.util.Optional;

public interface UserBase {

    static UserDto toDto(User entity){
        return new UserDto()
                .id(entity.id())
                .name(entity.name());
    }
    List<UserDto> findAll();

    Optional<UserDto> find(long userId);
}
