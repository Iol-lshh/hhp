package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.service.PointService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/point")
@AllArgsConstructor
@RestController
public class PointController {
    final PointService pointService;

    @GetMapping("/{userId}")
    public ResponseDto<Integer> remain(@PathVariable long userId){
        return new ResponseDto<>(pointService.remain(userId));
    }


}
