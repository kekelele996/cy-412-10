package com.smartestate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartestate.common.ErrorCode;
import com.smartestate.constants.LogTemplates;
import com.smartestate.constants.Messages;
import com.smartestate.constants.UserConstants;
import com.smartestate.dto.PaymentGenerateRequest;
import com.smartestate.entity.HouseMember;
import com.smartestate.entity.Payment;
import com.smartestate.entity.User;
import com.smartestate.mapper.HouseMemberMapper;
import com.smartestate.mapper.PaymentMapper;
import com.smartestate.mapper.UserMapper;
import com.smartestate.service.OperationLogService;
import com.smartestate.service.PaymentService;
import com.smartestate.utils.CommonFormatter;
import com.smartestate.utils.LogUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final UserMapper userMapper;
    private final HouseMemberMapper houseMemberMapper;
    private final OperationLogService operationLogService;

    public PaymentServiceImpl(PaymentMapper paymentMapper, UserMapper userMapper,
                              HouseMemberMapper houseMemberMapper, OperationLogService operationLogService) {
        this.paymentMapper = paymentMapper;
        this.userMapper = userMapper;
        this.houseMemberMapper = houseMemberMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public List<Payment> list(Long userId, String role) {
        LogUtil.info(LogTemplates.PAYMENT_LIST, userId, role);
        if (UserConstants.CO_RESIDENT.equals(role)) {
            User coResident = userMapper.selectById(userId);
            if (coResident != null && coResident.getBuilding() != null) {
                List<Long> houseUserIds = houseMemberMapper.selectList(
                        new LambdaQueryWrapper<HouseMember>()
                                .eq(HouseMember::getBuilding, coResident.getBuilding())
                                .eq(HouseMember::getUnit, coResident.getUnit())
                                .eq(HouseMember::getRoom, coResident.getRoom())
                                .eq(HouseMember::getStatus, "active"))
                        .stream().map(HouseMember::getUserId).collect(Collectors.toList());
                List<Payment> payments = paymentMapper.selectList(new LambdaQueryWrapper<Payment>()
                        .in(Payment::getUserId, houseUserIds).orderByDesc(Payment::getCreatedAt));
                payments.forEach(this::hydrate);
                return payments;
            }
        }
        return paymentMapper.selectList(new LambdaQueryWrapper<Payment>().eq(Payment::getUserId, userId).orderByDesc(Payment::getCreatedAt));
    }

    @Override
    public Payment pay(Long id, String role) {
        Payment payment = paymentMapper.selectById(id);
        if (payment == null) {
            throw new IllegalArgumentException(ErrorCode.PAYMENT_NOT_FOUND.format(id, role));
        }
        if ("paid".equals(payment.getStatus())) {
            operationLogService.record(payment.getUserId(), role, "payment.duplicate", "Payment", id,
                    String.format(LogTemplates.PAYMENT_DUPLICATE, id, role));
            throw new IllegalArgumentException(ErrorCode.PAYMENT_ALREADY_PAID.format(id, role));
        }
        payment.setStatus("paid");
        payment.setPaidAt(LocalDateTime.now());
        paymentMapper.updateById(payment);
        operationLogService.record(payment.getUserId(), role, "payment.pay", "Payment", id,
                String.format(LogTemplates.PAYMENT_PAY, id, payment.getMonth(), role) + " | " +
                        String.format(Messages.BACK_PAYMENT_PAID, id, role) + " | amount=" + CommonFormatter.money(payment.getAmount()));
        return payment;
    }

    @Override
    public Payment generate(String role, PaymentGenerateRequest request) {
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setMonth(request.getMonth());
        payment.setFeeType(request.getFeeType() == null ? "property" : request.getFeeType());
        payment.setAmount(new BigDecimal("426.00"));
        payment.setStatus("unpaid");
        payment.setCreatedAt(LocalDateTime.now());
        paymentMapper.insert(payment);
        operationLogService.record(request.getUserId(), role, "payment.generate", "Payment", payment.getId(),
                String.format(LogTemplates.PAYMENT_GENERATE, payment.getId(), payment.getFeeType(), role));
        return payment;
    }

    private void hydrate(Payment payment) {
        if (payment.getUserId() != null) {
            User user = userMapper.selectById(payment.getUserId());
            if (user != null) user.setPasswordHash(null);
            payment.setUser(user);
        }
    }
}
