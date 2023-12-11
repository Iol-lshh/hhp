package com.lshh.hhp.common.point;

public interface PointVo {
    Integer count();
    <T extends PointVo> T count(Integer count);
}
