package com.clibo.dto;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PayDepositBody {
    private Long appointmentId;
    private String bankInfo;
}
