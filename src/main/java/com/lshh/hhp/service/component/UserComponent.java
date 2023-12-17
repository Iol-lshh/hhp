package com.lshh.hhp.service.component;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.orm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserComponent {

    static UserDto toDto(User entity){
        return new UserDto()
                .id(entity.id())
                .name(entity.name());
    }
    static User toEntity(UserDto dto){
        return new User()
                .id(dto.id())
                .name(dto.name());
    }

    UserDto save(UserDto dto) throws Exception;

    List<UserDto> findAll();
    Optional<UserDto> find(long id);
}
