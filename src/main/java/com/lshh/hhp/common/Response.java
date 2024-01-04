package com.lshh.hhp.common;

public interface Response {
    enum Result {
        START(0),
        SUCCESS(1),
        FAIL(2),
        CANCELING(3),
        CANCELED(4),
        OK(5);

        Result(int code) {
        }

        public static Result of(final int code){
            return Result.values()[code];
        }
    }
}
