package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.PaymentDto;

public interface PaymentBiz1 {
    ResultDto<PaymentDto> exchange(long userId, int toNeed) throws Exception;
}
