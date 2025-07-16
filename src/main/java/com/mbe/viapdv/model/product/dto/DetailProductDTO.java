package com.mbe.viapdv.model.product.dto;

import com.mbe.viapdv.model.category.dto.ListCategoryDTO;
import com.mbe.viapdv.model.product.Product;

import java.math.BigDecimal;

public record DetailProductDTO(long id, String name, String sku, BigDecimal price, String barcode, Integer stockQuantity, ListCategoryDTO category) {

    public DetailProductDTO(Product product) {
        this(product.getId(), product.getName(), product.getSku(), product.getPrice(), product.getBarcode(), product.getStockQuantity(),
             product.getCategory() != null ? new ListCategoryDTO(product.getCategory()) : null);
    }
}