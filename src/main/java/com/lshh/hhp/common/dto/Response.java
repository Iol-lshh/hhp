package com.lshh.hhp.common.dto;

public interface Response {
    enum Result {
        Start(0),
        OK(1),
        FAIL(2);

        Result(int i) {
        }

    }
}
