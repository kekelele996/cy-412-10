package com.smartestate.controller;

import com.smartestate.common.Constants;
import com.smartestate.common.Result;
import com.smartestate.dto.PaymentGenerateRequest;
import com.smartestate.entity.Payment;
import com.smartestate.service.PaymentService;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public Result<List<Payment>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("PaymentController list user_id=%s role=%s", userId, role);
        return Result.ok(paymentService.list(userId, role));
    }

    @PostMapping("/{id}/pay")
    public Result<Payment> pay(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("PaymentController pay payment_id=%s role=%s", id, role);
        return Result.ok(paymentService.pay(id, role));
    }

    @PostMapping("/generate")
    public Result<Payment> generate(@RequestBody PaymentGenerateRequest body, HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        return Result.ok(paymentService.generate(role, body));
    }
}
