package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@AllArgsConstructor
@RestController
public class UserController {
    UserService userService;

    @Operation(summary = "user 생성, 갱신")
    @PostMapping("/")
    public ResponseDto<UserDto> save(@RequestBody UserDto userDto) throws Exception {
        return userService.save(userDto).toResponseDto();
    }

    @Operation(summary = "user 내역")
    @GetMapping("/all")
    public ResponseDto<List<UserDto>> all(){
        return new ResponseDto<>(userService.findAll());
    }
}
