package com.mbe.viapdv.model.product.dto;

import java.math.BigDecimal;

import com.mbe.viapdv.model.product.Product;

public record CreateProductDTO(String name, BigDecimal price, String sku, String barcode, Integer stockQuantity, Long categoryId) {
	
	public CreateProductDTO(Product product) {
        this(
        		product.getName(),
        		product.getPrice(),
        		product.getSku(),
        		product.getBarcode(),
        		product.getStockQuantity(),
        		product.getCategory().getId()
        );
    }
	
}
