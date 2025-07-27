package com.mbe.viapdv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.mbe.viapdv.model.product.dto.UpdateProductDTO;
import com.mbe.viapdv.model.product.dto.CreateProductDTO;
import com.mbe.viapdv.repository.IProductRepository;
import com.mbe.viapdv.service.CategoryService;
import com.mbe.viapdv.service.ProductService;
import com.mbe.viapdv.util.ResponseDTO;


@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@GetMapping
	public ResponseEntity<ResponseDTO> list() {
		ResponseDTO response = productService.listActive();
		return ResponseEntity.status(response.status()).body(response);
	}

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> get(@PathVariable("id") Long id) {
    	ResponseDTO response = productService.getById(id);
		return ResponseEntity.status(response.status()).body(response);
    }
    
    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody CreateProductDTO data) {
    	ResponseDTO response = productService.create(data);
		return ResponseEntity.status(response.status()).body(response);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable("id") Long id, @RequestBody UpdateProductDTO data) { 
    	ResponseDTO response = productService.update(id, data);
		return ResponseEntity.status(response.status()).body(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") Long id) {
    	ResponseDTO response = productService.delete(id);
		return ResponseEntity.status(response.status()).body(response);
    }

	@GetMapping("/search")
	public ResponseEntity<ResponseDTO> search(@RequestParam(required = false)  String name) {
		ResponseDTO response = productService.search(name);
		return ResponseEntity.status(response.status()).body(response);
	}
    
}
