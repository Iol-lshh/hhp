package com.lshh.hhp.user.controller;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.user.User;
import com.lshh.hhp.user.service.UserService;
import com.lshh.hhp.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user")
@AllArgsConstructor
@RestController
public class UserController {

    final UserService userService;
    @Operation(summary = "유저 리스트")
    @GetMapping("/all")
    public ResultDto<List<UserDto>> all(){
        return new ResultDto<>(userService.findAll().stream().map(User::toDto).toList());
    }
}
