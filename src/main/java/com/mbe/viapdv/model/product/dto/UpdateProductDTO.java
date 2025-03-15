package com.mbe.viapdv.model.product.dto;

import java.math.BigDecimal;

import com.mbe.viapdv.model.category.dto.ListCategoryDTO;
import com.mbe.viapdv.model.product.Product;

public record UpdateProductDTO(Long id, String name, BigDecimal price, String sku, String barcode, Integer stockQty, Long categoryId) {
    
    public UpdateProductDTO(Product product) {
        this(
        		product.getId(),
        		product.getName(),
        		product.getPrice(),
        		product.getSku(),
        		product.getBarcode(),
        		product.getStockQuantity(),
        		product.getCategory().getId()
        );
    }
}