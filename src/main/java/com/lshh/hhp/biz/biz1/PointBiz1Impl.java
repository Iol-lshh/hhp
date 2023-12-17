package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.PointDto;
import com.lshh.hhp.biz.base.PointBiz;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class PointBiz1Impl implements PointBiz1 {

    final PointBiz pointComponent;

    @Override
    public List<PointDto> findAllByUserId(long userId) {
        return pointComponent.findAllByUserId(userId);
    }

    @Override
    public Integer remain(long userId) {
        return pointComponent.remain(userId);
    }

    @Override
    @Transactional
    public ResultDto<List<PointDto>> squash() {
        // 1. 각 user 마다, 이전 포인트들의 count를 0처리하고
        // 2. sum의 count인 포인트를 추가해줌
        // 의의: 0 처리 된 것을 백업하고 비울 수 있게된다. => payment와 purchase에 지불 정보는 남아있다.
        // # sum group by user_id
        return pointComponent.squash();
    }

    @Override
    public ResultDto<PointDto> squash(long userId) throws Exception {
        return pointComponent.squash(userId);
    }

}
