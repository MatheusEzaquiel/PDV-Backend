package com.mbe.viapdv.controller;

import com.mbe.viapdv.model.category.Category;
import com.mbe.viapdv.service.CategoryService;
import com.mbe.viapdv.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        ResponseDTO response = categoryService.listActive();
        return ResponseEntity.status(response.status()).body(response);
    }
}
