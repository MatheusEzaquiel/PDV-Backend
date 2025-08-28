package com.mbe.viapdv.model.sale.dto;

import com.mbe.viapdv.model.saleItem.dto.CreateSaleItemDTO;

import java.util.List;

public record CreateSaleCompleteDTO(SaleResumeDTO sale, List<CreateSaleItemDTO> saleItemList) {

}
