package com.lshh.hhp.dto;

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
        this.result = Result.OK;
        this.value = value;
    }

    @JsonCreator
    public ResultDto(@JsonProperty("result") Result result, @JsonProperty("value") T value){
        this.result = result;
        this.value = value;
    }
}
