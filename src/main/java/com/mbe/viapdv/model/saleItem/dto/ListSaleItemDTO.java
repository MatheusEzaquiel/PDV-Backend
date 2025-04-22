package com.mbe.viapdv.model.saleItem.dto;

import java.math.BigDecimal;

import com.mbe.viapdv.model.saleItem.SaleItem;


public record ListSaleItemDTO(Long id, String name, int quantity, BigDecimal unitPrice, BigDecimal totalPrice, Long saleId) {
	
	public ListSaleItemDTO(SaleItem saleItem) {
		this(saleItem.getId(), saleItem.getProduct().getName(), saleItem.getQuantity(),saleItem.getUnitPrice(), saleItem.getTotalPrice(), saleItem.getSale().getId());
	}

}
