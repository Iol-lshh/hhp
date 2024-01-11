package com.lshh.hhp.point.controller;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.point.Point;
import com.lshh.hhp.point.dto.PointDto;
import com.lshh.hhp.point.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "포인트 잔액 확인")
    @GetMapping("/remain/{userId}")
    public ResultDto<Integer> remain(@PathVariable Long userId){
        return ResultDto.ok(pointService.countRemain(userId));
    }

    @Operation(summary = "포인트 사용 내역")
    @GetMapping("/history/{userId}")
    public ResultDto<List<PointDto>> history(@PathVariable Long userId){
        return ResultDto.ok(pointService.findAllByUserId(userId).stream().map(Point::toDto).toList());
    }

    // 포인트 유저 압축
    @GetMapping("/squash/{userId}")
    public ResultDto<PointDto> squash(@PathVariable Long userId) throws Exception {
        return ResultDto.ok(pointService.squash(userId).toDto());
    }

}
