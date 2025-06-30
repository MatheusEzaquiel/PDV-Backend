package com.mbe.viapdv.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbe.viapdv.model.product.Product;
import com.mbe.viapdv.model.sale.Sale;
import com.mbe.viapdv.model.sale.dto.ListSaleDTO;
import com.mbe.viapdv.model.saleItem.SaleItem;
import com.mbe.viapdv.model.saleItem.dto.CreateSaleItemDTO;
import com.mbe.viapdv.service.SaleItemService;
import com.mbe.viapdv.util.ResponseDTO;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("saleitem")
public class SaleItemController {

	@Autowired
	SaleItemService saleItemService;
	
	@PostMapping("/addToSale")
	public ResponseEntity<ResponseDTO> addSalesItem(@RequestBody CreateSaleItemDTO data) {
		ResponseDTO response = saleItemService.addSalesItemInSale(data); 
		return ResponseEntity.status(response.status()).body(response);
	}

}
