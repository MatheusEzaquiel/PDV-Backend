package com.mbe.viapdv.model.category.dto;

import com.mbe.viapdv.model.category.Category;

public record ListCategoryDTO(long id, String name) {
    
    public ListCategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}