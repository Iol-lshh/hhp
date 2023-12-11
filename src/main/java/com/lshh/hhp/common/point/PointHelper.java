package com.lshh.hhp.common.point;

import java.util.List;

public interface PointHelper {
    PointVo squash(List<PointVo> pointVos);
    // 1. 각 user 마다, 이전 포인트들의 count를 0처리하고,
    // 2. sum의 count인 포인트를 추가해줌
    // 의의: 0 처리 된 것을 백업하고 비울 수 있게된다. => payment와 purchase에 지불 정보는 남아있다.

    PointHelper setPointService(PointHelperService pointHelperService);

    PointHelper backup();
}
