package com.lshh.hhp.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto<T> implements Response{
    Result result;
    T value;

    public ResultDto(T value){
        this.result = Result.SUCCESS;
        this.value = value;
    }

    @JsonCreator
    public ResultDto(@JsonProperty("result") Result result, @JsonProperty("value") T value){
        this.result = result;
        this.value = value;
    }

    public static <T> ResultDto<T> ok(){
        return new ResultDto<>(Result.OK, null);
    }
    public static <T> ResultDto<T> ok(T data){
        return new ResultDto<>(Result.OK, data);
    }
    public static <T> ResultDto<T> success(){
        return new ResultDto<>(Result.SUCCESS, null);
    }
    public static <T> ResultDto<T> success(T data){
        return new ResultDto<>(Result.SUCCESS, data);
    }

    public static <T> ResultDto<T> fail(){
        return new ResultDto<>(Result.FAIL, null);
    }
}
