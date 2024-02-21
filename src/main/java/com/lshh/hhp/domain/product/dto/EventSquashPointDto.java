package com.lshh.hhp.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true, fluent = true)
@Getter
@Setter
public class EventSquashPointDto {
    long userId;

    public static EventSquashPointDto of(long userId){
        EventSquashPointDto eventSquashPointDto = new EventSquashPointDto();
        eventSquashPointDto.userId = userId;
        return eventSquashPointDto;
    }

}
