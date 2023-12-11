package com.lshh.hhp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> implements Response{
    Result result;
    T value;

    public ResponseDto<T> toResponseDto(){
        return new ResponseDto<>(this.result, this.value);
    }
}
