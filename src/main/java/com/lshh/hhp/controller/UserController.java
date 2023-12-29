package com.lshh.hhp.controller;

import com.lshh.hhp.biz.base.UserBiz;
import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.UserDto;
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

    final UserBiz userService;
    @Operation(summary = "유저 리스트")
    @GetMapping("/all")
    public ResultDto<List<UserDto>> all(){
        return new ResultDto<>(userService.findAll());
    }
}
