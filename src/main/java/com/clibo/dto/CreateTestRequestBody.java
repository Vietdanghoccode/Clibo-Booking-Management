package com.clibo.dto;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class CreateTestRequestBody {
    private List<Long> testCatalogId;
    private Long appointmentId;
}
