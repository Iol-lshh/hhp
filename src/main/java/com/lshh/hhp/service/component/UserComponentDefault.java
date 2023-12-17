package com.lshh.hhp.service.component;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.orm.entity.User;
import com.lshh.hhp.orm.repository.UserRepository;
import com.lshh.hhp.service.UserServiceDefault;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserComponentDefault implements UserComponent{

    final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto save(UserDto dto) throws Exception {
        User entity;

        if (Optional.ofNullable(dto.id()).isEmpty()) {
            entity = UserComponent.toEntity(dto);
        } else {
            entity = userRepository.findById(dto.id())
                    .orElseThrow(Exception::new);
            entity.name(dto.name() != null ? dto.name() : entity.name());
        }
        entity = userRepository.save(entity);
        return UserComponent.toDto(entity);
    }

    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(UserComponent::toDto)
                .toList();
    }

    @Override
    @Transactional
    public Optional<UserDto> find(long id) {
        return userRepository
                .findById(id)
                .map(UserComponent::toDto);
    }
}
