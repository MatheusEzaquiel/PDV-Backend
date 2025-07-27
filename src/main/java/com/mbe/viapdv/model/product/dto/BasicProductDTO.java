package com.mbe.viapdv.model.product.dto;

import com.mbe.viapdv.model.category.dto.ListCategoryDTO;
import com.mbe.viapdv.model.product.Product;

import java.math.BigDecimal;

public record BasicProductDTO(long id, String name, String sku, ListCategoryDTO category, BigDecimal price) {
    
    public BasicProductDTO(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getSku(),
                (product.getCategory() != null ? new ListCategoryDTO(product.getCategory()) : null),
                product.getPrice()
        );
    }
}