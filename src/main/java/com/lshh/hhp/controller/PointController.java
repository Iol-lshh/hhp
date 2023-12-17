package com.lshh.hhp.controller;

import com.lshh.hhp.dto.ResponseDto;
import com.lshh.hhp.dto.origin.PointDto;
import com.lshh.hhp.biz.biz1.PointBiz1;
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
    final PointBiz1 pointService;

    @Operation(summary = "포인트 잔액 확인")
    @GetMapping("/remain/{userId}")
    public ResponseDto<Integer> remain(@PathVariable Long userId){
        return new ResponseDto<>(pointService.remain(userId));
    }

    @Operation(summary = "포인트 사용 내역")
    @GetMapping("/history/{userId}")
    public ResponseDto<List<PointDto>> history(@PathVariable Long userId){
        return new ResponseDto<>(pointService.findAllByUserId(userId));
    }

    // 포인트 일괄 압축
    @GetMapping("/squash")
    public ResponseDto<List<PointDto>> squash(){
        return pointService.squash().toResponseDto();
    }

    // 포인트 유저 압축
    @GetMapping("/squash/{userId}")
    public ResponseDto<PointDto> squash(@PathVariable Long userId) throws Exception {
        return pointService.squash(userId).toResponseDto();
    }

}
