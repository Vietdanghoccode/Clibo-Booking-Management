package com.clibo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class VerifyBody {
    private String phone;
    private String cid;
}
