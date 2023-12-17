package com.lshh.hhp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto<T> implements Response{
    Result result;
    T value;

    public ResultDto(T value){
        this.result = Result.OK;
        this.value = value;
    }
    public ResultDto(Result result, T value){
        this.result = result;
        this.value = value;
    }

    public ResponseDto<T> toResponseDto(){
        return new ResponseDto<>(this.result, this.value);
    }
}
