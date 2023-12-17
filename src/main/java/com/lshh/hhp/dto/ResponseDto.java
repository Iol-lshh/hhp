package com.lshh.hhp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> implements Response{
    private ResultDto<T> result;

    public ResponseDto(T data){
        this.result = new ResultDto<>(Result.OK, data);
    }
    public ResponseDto(Result result, T data) {
        this.result = new ResultDto<>(result, data);
    }

    @JsonIgnore
    public T getData(){
        return this.result.getValue();
    }
}
