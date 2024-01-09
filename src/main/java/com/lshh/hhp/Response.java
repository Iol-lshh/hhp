<<<<<<<< HEAD:src/main/java/com/lshh/hhp/dto/Response.java
package com.lshh.hhp.dto;
========
package com.lshh.hhp.common;
>>>>>>>> origin/ch3st1_aws:src/main/java/com/lshh/hhp/Response.java

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
