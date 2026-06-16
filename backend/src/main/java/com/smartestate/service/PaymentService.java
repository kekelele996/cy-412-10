package com.smartestate.service;

import com.smartestate.dto.PaymentGenerateRequest;
import com.smartestate.entity.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> list(Long userId, String role);
    Payment pay(Long id, String role);
    Payment generate(String role, PaymentGenerateRequest request);
}
