package com.lshh.hhp.domain.user;

import com.lshh.hhp.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    public UserDto toDto(){
        return new UserDto()
                .id(this.id())
                .name(this.name());
    }
}
