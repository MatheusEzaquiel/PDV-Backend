package com.mbe.viapdv.model.sale.dto;

import com.mbe.viapdv.enums.PaymentType;

import java.math.BigDecimal;

public record SaleResumeDTO(String uuid, Long userId, PaymentType paymentType, BigDecimal total) {}
