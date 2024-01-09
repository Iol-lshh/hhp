<<<<<<<< HEAD:src/main/java/com/lshh/hhp/dto/origin/PointDto.java
package com.lshh.hhp.dto.origin;
========
package com.lshh.hhp.point.dto;
>>>>>>>> origin/ch3st1_aws:src/main/java/com/lshh/hhp/point/dto/PointDto.java

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointDto {
    @JsonProperty
    Long id;
    @JsonProperty
    Integer count;
    @JsonProperty
    Long userId;
    @JsonProperty
    Integer fromType;   // PointService.PointType
    @JsonProperty
    Long fromId;    // PaymentId or PurchaseId
}
