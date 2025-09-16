package com.mbe.viapdv.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mbe.viapdv.enums.OperationStatus;
import com.mbe.viapdv.enums.PaymentType;
import com.mbe.viapdv.exception.ConsistencySaleException;
import com.mbe.viapdv.exception.ItemNotFoundException;
import com.mbe.viapdv.model.sale.dto.CreateSaleCompleteDTO;
import com.mbe.viapdv.model.user.User;
import com.mbe.viapdv.repository.ISaleItemRepository;
import com.mbe.viapdv.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	IProductRepository productRepository;

	@Autowired
	ISaleItemRepository saleItemRepository;

	@Autowired
	IUserRepository userRepository;
	
	@Autowired
	SaleItemService saleItemService;

	private static final Logger logger = LoggerFactory.getLogger(SaleService.class);



	public ResponseDTO listActive() {

    	List<ListSaleDTO> saleList = saleRepos.findByIsActiveTrue().stream()
    			.map(ListSaleDTO::new)
    			.toList();
    	
    	return new ResponseDTO(HttpStatus.OK.value(), saleList, "Lista de Vendas Ativos");
	};
	
	public ResponseDTO getById(Long id) {
		
		Optional<Sale> optSale = saleRepos.findById(id); 
		
		if(optSale.isEmpty()) {
			logger.warn("Sale not found ID: {}", optSale.get().getId());
			throw new ItemNotFoundException("Sale Not Found");
		}
		
		return new ResponseDTO(HttpStatus.OK.value(), optSale.get(), "Venda encontrada!");
		
	};
	
	@Transactional
	public ResponseDTO create(CreateSaleDTO data) {
		
    	Sale sale = new Sale();
    	sale.setPaymentMethod(null);
    	sale.setCreated(LocalDateTime.now());
    	sale.setActive(true);
    	
		saleRepos.save(sale);
		return new ResponseDTO(HttpStatus.OK.value(), null, "Venda Criada!");
	}

	@Transactional
	public ResponseDTO createCompleteSale(CreateSaleCompleteDTO data) {

		Optional<User> userSale = userRepository.findById(data.sale().userId());
		if (userSale.isEmpty()) {
			logger.warn("Product not found ID: {}", userSale.get().getId());
			throw new ItemNotFoundException("User Not Found");
		}


		PaymentType paymentType = (data.sale().paymentType() != null)
				? PaymentType.valueOf(data.sale().paymentType().toString())
				: PaymentType.NOT_INFORMED;

		ListSaleDTO listSaleDTO = null;
		UUID uuid = (UUID.fromString(data.sale().uuid()));
		Sale sale = new Sale(userSale.get(), uuid,  paymentType.name(), data.sale().total());
		sale.setStatus(OperationStatus.ACTIVE);
		sale = saleRepos.save(sale);


		BigDecimal calcTotal = BigDecimal.ZERO;
		// SaleItems
		for(CreateSaleItemDTO saleItemDTO : data.saleItemList()) {
			Optional<Product> optProduct = productRepository.findById(saleItemDTO.productId());
			if(optProduct.isEmpty()) {
				logger.warn("Product not found ID: {}", saleItemDTO.productId());
				throw new ItemNotFoundException("Product Not Found");
			}

			Product product = optProduct.get();
			BigDecimal totalSaleItem = product.getPrice().multiply(new BigDecimal(saleItemDTO.quantity()));

			// Add Product in SaleItem
			SaleItem saleItem = new SaleItem(product, product.getPrice(), saleItemDTO.quantity(), totalSaleItem, sale);
			SaleItem saleItemSaved = saleItemRepository.save(saleItem);
			listSaleDTO = new ListSaleDTO(saleItemSaved.getSale());

			calcTotal = calcTotal.add(totalSaleItem);
		}

		if(calcTotal.compareTo(sale.getTotalPrice()) != BigDecimal.ZERO.intValue())
			throw new ConsistencySaleException("Price of sale doesn't match with price of ItemSale sum");

		return new ResponseDTO(HttpStatus.OK.value(), listSaleDTO, "Venda Criada!");
	}
	
}
