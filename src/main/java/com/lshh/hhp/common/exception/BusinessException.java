package com.lshh.hhp.common.exception;

public class BusinessException extends RuntimeException{

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

// - IllegalArgumentException: 메서드에 유형이 일치하지 않는 매개변수를 전달하는 경우
// - ArrayStoreException: 배열 유형이 허락하지 않는 객체를 객체를 배열에 저장하려는 경우
// - ArrayIndexOutOfBoundsException: 배열을 참조하는 인덱스가 잘못된 경우
// - IndexOutOfBoundsException: 객체의 범위를 벗어난 색인(Index)를 사용하는 경우
// - NullPointerException: 널(null) 객체를 참조했을 경우
// - ArithmeticException: 정수를 0으로 나누었을 경우
// - ClassCastException: 적절치 못하게 Class를 형 변환하는 경우
// - NegativeArraySizeException: 배열의 크기가 음수인 경우
// - NoClassDefFoundException: 클래스를 찾을 수 없는 경우
// - OutOfMemoryException: 사용 가능한 메모리가 없는 경우
// - IndexOutOfBoundsException: 객체의 범위를 벗어난 색인(Index)를 사용하는 경우
// - IllegalMonitorStateException: 스레드가 스레드에 속하지 않는 객체를 모니터 하려고 기다리는 경우
// - IllegalStateException: 적절하지 않은 때에 메서드를 호출하는 경우
// - SecurityException: 보안 위반 관련의 경우
// - UnsupportedOperationException: 지원되지 않는 메소드를 호출 했을때
