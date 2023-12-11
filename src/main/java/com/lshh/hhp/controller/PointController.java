package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.PointDto;
import com.lshh.hhp.service.PointService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/point")
@AllArgsConstructor
@RestController
public class PointController {
    final PointService pointService;

    // 포인트 잔액 확인
    @GetMapping("/remain/{userId}")
    public ResponseDto<Integer> remain(@PathVariable Long userId){
        return new ResponseDto<>(pointService.remain(userId));
    }

    // 포인트 사용 내역
    @GetMapping("/history/{userId}")
    public ResponseDto<List<PointDto>> history(@PathVariable Long userId){
        return new ResponseDto<>(pointService.findAllByUserId(userId));
    }

}
