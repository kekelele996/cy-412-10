package com.smartestate.dto;

import lombok.Data;

@Data
public class PaymentGenerateRequest {
    private Long userId;
    private String month;
    private String feeType;
}
