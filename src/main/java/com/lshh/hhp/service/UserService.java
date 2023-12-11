package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.UserDto;


import java.util.List;
import java.util.Optional;

public interface UserService {
    ResultDto<UserDto> save(UserDto dto) throws Exception;

    List<UserDto> findAll();
    Optional<UserDto> find(long id);
}
