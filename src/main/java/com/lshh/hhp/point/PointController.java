package com.lshh.hhp.point;

import com.lshh.hhp.common.ResultDto;
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
    final PointBiz pointService;

    @Operation(summary = "포인트 잔액 확인")
    @GetMapping("/remain/{userId}")
    public ResultDto<Integer> remain(@PathVariable Long userId){
        return new ResultDto<>(pointService.remain(userId));
    }

    @Operation(summary = "포인트 사용 내역")
    @GetMapping("/history/{userId}")
    public ResultDto<List<PointDto>> history(@PathVariable Long userId){
        return new ResultDto<>(pointService.findAllByUserId(userId));
    }

    // 포인트 일괄 압축
    @GetMapping("/squash")
    public ResultDto<List<PointDto>> squash(){
        return new ResultDto<>(pointService.squash());
    }

    // 포인트 유저 압축
    @GetMapping("/squash/{userId}")
    public ResultDto<PointDto> squash(@PathVariable Long userId) throws Exception {
        return new ResultDto<>(pointService.squash(userId));
    }

}
