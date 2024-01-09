<<<<<<<< HEAD:src/main/java/com/lshh/hhp/dto/origin/UserDto.java
package com.lshh.hhp.dto.origin;
========
package com.lshh.hhp.user.dto;
>>>>>>>> origin/ch3st1_aws:src/main/java/com/lshh/hhp/user/dto/UserDto.java

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
public class UserDto {
    @JsonProperty
    Long id;
    @JsonProperty
    String name;
}
