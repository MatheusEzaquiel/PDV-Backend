package com.mbe.viapdv.model.sale.dto;

import java.math.BigDecimal;

import com.mbe.viapdv.model.sale.Sale;
import com.mbe.viapdv.model.user.dto.BasicUserDTO;

public record ListSaleDTO(BasicUserDTO user, String paymentMethod, BigDecimal totalPrice) {
	
	public ListSaleDTO(Sale sale) {
		this(
				new BasicUserDTO(sale.getUser()), 
				sale.getPaymentMethod(),
				sale.getTotalPrice()
		);
	}
}
