package com.mbe.viapdv.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("sales")
public class SaleController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	SaleService saleService;
	
	@Autowired
	SaleItemService saleItemService;
    
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

	@PatchMapping("/{saleId}/skuCode/{skuCode}")
	public ResponseEntity<ResponseDTO> addSalesItem(
			@PathVariable("saleId") Long saleId,
    		@PathVariable("skuCode") String skuCode,
    		@RequestBody CreateSaleItemDTO data) {
		ResponseDTO response = saleService.addSalesItemInSale(saleId, skuCode, data); 
		return ResponseEntity.status(response.status()).body(response);
	}
}
