package com.mbe.viapdv.model.saleItem.dto;

import java.math.BigDecimal;

public record CreateSaleItemDTO(long saleId, long userId, long productId, Integer quantity, BigDecimal total, String saleUUID) {}
