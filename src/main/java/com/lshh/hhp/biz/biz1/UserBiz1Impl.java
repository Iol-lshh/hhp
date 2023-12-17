package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.origin.UserDto;
import com.lshh.hhp.biz.base.UserBiz;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserBiz1Impl implements UserBiz1 {

    final UserBiz userComponent;
    @Override
    public List<UserDto> findAll() {
        return userComponent.findAll();
    }
}
