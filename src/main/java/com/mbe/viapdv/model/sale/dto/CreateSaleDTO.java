package com.mbe.viapdv.model.sale.dto;

import java.math.BigDecimal;

import com.mbe.viapdv.model.sale.Sale;
import com.mbe.viapdv.model.user.dto.BasicUserDTO;

public record CreateSaleDTO(BasicUserDTO user, String paymentMethod, BigDecimal totalPrice) {
	
	public CreateSaleDTO(Sale sale) {
		this(
				new BasicUserDTO(sale.getUser()), 
				sale.getPaymentMethod(),
				sale.getTotalPrice()
		);
	}
}
