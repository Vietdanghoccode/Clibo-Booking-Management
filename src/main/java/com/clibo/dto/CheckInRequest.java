package com.clibo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter

public class CheckInRequest {
    private Long appointmentId;
    private String identifyCitizen;
    private String phone;
}
