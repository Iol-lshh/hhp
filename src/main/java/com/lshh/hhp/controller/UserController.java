package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@AllArgsConstructor
@RestController
public class UserController {
    UserService userService;

    // user 생성
    @PostMapping("/")
    public ResponseDto<UserDto> create(@RequestBody UserDto userDto) throws Exception {
        return userService.save(userDto).toResponseDto();
    }
    // user 내역
    @GetMapping("/all")
    public ResponseDto<List<UserDto>> all(){
        return new ResponseDto<>(userService.findAll());
    }
}
