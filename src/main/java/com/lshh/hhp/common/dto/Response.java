package com.lshh.hhp.common.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

public interface Response {
    enum Result {
        Start(0),
        OK(1),
        FAIL(2);

        Result(int i) {
        }

        public static Result of(final int code){
            return Result.values()[code];
        }
    }
}
