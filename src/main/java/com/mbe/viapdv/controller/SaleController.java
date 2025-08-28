package com.mbe.viapdv.controller;

import com.mbe.viapdv.model.product.Product;
import com.mbe.viapdv.model.sale.dto.CreateSaleCompleteDTO;
import com.mbe.viapdv.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbe.viapdv.model.sale.dto.CreateSaleDTO;
import com.mbe.viapdv.model.saleItem.dto.CreateSaleItemDTO;
import com.mbe.viapdv.service.ProductService;
import com.mbe.viapdv.service.SaleItemService;
import com.mbe.viapdv.service.SaleService;
import com.mbe.viapdv.util.ResponseDTO;

import java.util.Optional;

@RestController
@RequestMapping("sales")
public class SaleController {
	
	@Autowired
	SaleService saleService;

	@Autowired
	IProductRepository productRepository;
	
	
	@GetMapping
	public ResponseEntity<ResponseDTO> listActive() {
		ResponseDTO response = saleService.listActive(); 
		return ResponseEntity.status(response.status()).body(response);
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@RequestBody CreateSaleDTO data) {
		ResponseDTO response = saleService.create(data); 
		return ResponseEntity.status(response.status()).body(response);
	}

	@PostMapping("/complete-sale")
	public ResponseEntity<ResponseDTO> createCompleteSale(@RequestBody CreateSaleCompleteDTO data) {
		System.out.println(data.sale());
		System.out.println(data.saleItemList());
		ResponseDTO response = saleService.createCompleteSale(data);

		return ResponseEntity.status(response.status()).body(response);
	}
}
