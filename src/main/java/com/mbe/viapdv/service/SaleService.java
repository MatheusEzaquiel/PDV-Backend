package com.mbe.viapdv.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.product.Product;
import com.mbe.viapdv.model.sale.Sale;
import com.mbe.viapdv.model.sale.dto.CreateSaleDTO;
import com.mbe.viapdv.model.sale.dto.ListSaleDTO;
import com.mbe.viapdv.model.saleItem.SaleItem;
import com.mbe.viapdv.model.saleItem.dto.CreateSaleItemDTO;
import com.mbe.viapdv.repository.IProductRepository;
import com.mbe.viapdv.repository.ISalesRepository;
import com.mbe.viapdv.util.ResponseDTO;

import jakarta.transaction.Transactional;

@Service
public class SaleService {

	@Autowired
	ISalesRepository saleRepos;
	
	@Autowired
	IProductRepository productRepos;
	
	@Autowired
	SaleItemService saleItemService;
	
	
	public ResponseDTO listActive() {

    	List<ListSaleDTO> saleList = saleRepos.findByIsActiveTrue().stream()
    			.map(ListSaleDTO::new)
    			.toList();
    	
    	return new ResponseDTO(HttpStatus.OK.value(), saleList, "Lista de Vendas Ativos");
	};
	
	public ResponseDTO getById(Long id) {
		
		Optional<Sale> optSale = saleRepos.findById(id); 
		
		if(optSale.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda Não encontrada");
		
		return new ResponseDTO(HttpStatus.OK.value(), optSale.get(), "Venda encontrada!");
		
	};
	
	@Transactional
	public ResponseDTO create(CreateSaleDTO data) {
		
    	Sale sale = new Sale();
    	sale.setPaymentMethod(null);
    	sale.setCreated(LocalDateTime.now());
    	sale.setActive(true);
    	
		saleRepos.save(sale);
		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda Criada!");
	}
	
}
