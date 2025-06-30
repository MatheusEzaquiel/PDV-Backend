package com.mbe.viapdv.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.product.Product;
import com.mbe.viapdv.model.sale.Sale;
import com.mbe.viapdv.model.sale.dto.ListSaleDTO;
import com.mbe.viapdv.model.saleItem.SaleItem;
import com.mbe.viapdv.model.saleItem.dto.CreateSaleItemDTO;
import com.mbe.viapdv.model.user.User;
import com.mbe.viapdv.repository.IProductRepository;
import com.mbe.viapdv.repository.ISaleItemRepository;
import com.mbe.viapdv.repository.ISalesRepository;
import com.mbe.viapdv.repository.IUserRepository;
import com.mbe.viapdv.util.ResponseDTO;

import jakarta.transaction.Transactional;

@Service
public class SaleItemService {
	
	@Autowired
	ISaleItemRepository saleItemRepos;
	
	@Autowired
	ISalesRepository saleRepos;
	
	@Autowired
	IProductRepository productRepos;
	
	@Autowired
	IUserRepository userRepos;
	
	
	public boolean save(SaleItem saleItem) {
		
		SaleItem saleItemOpt = saleItemRepos.save(saleItem);
		if(saleItemOpt != null)
			return true;
		else 
			return false;
		
	}
	
	@Transactional
	public ResponseDTO addSalesItemInSale(CreateSaleItemDTO data) {
		
		Sale currentSale = new Sale();
    	SaleItem saleItem = new SaleItem();
    	Product product = new Product();
    	
    	// Resgatar Venda
    	Optional<Sale> optCurrentSale = saleRepos.findById(data.saleId());
    	
    	if (optCurrentSale.isPresent()){
    		currentSale = optCurrentSale.get();
    	} else if (data.saleId()== 0){
    		
    		Optional<User> userOpt = userRepos.findById(data.userId());
    		if(userOpt.isEmpty())
    			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Produto Não encontrado");
    		
    		currentSale.setPaymentMethod("NÂO INFORMADO");
    		currentSale.setTotalPrice(BigDecimal.ZERO);
    		currentSale.setCreated(LocalDateTime.now());
    		currentSale.setUser(userOpt.get());
    		currentSale.setActive(true);
    		saleRepos.save(currentSale);
    	} else if(optCurrentSale.isEmpty()) {
    		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda Não encontrada");
    	}
    	
    	
        	// Resgatar Produto pelo Código
        	Optional<Product> optProduct = productRepos.findById(data.productId());
        	if(optProduct.isEmpty()) 
        		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Produto Não encontrada");
        	
        	product = optProduct.get();
        	
        	// Product Price x Product Qty
			BigDecimal totalSaleItem = product.getPrice().multiply(new BigDecimal(data.quantity()));

			// Add Product in SaleItem
			saleItem.setProduct(product);
			saleItem.setQuantity(data.quantity());
			saleItem.setUnitPrice(product.getPrice());
			saleItem.setTotalPrice(totalSaleItem);
			saleItem.setSale(currentSale);
			saleItem.setCreated(LocalDateTime.now());
			saleItem.setActive(true);
			SaleItem recordSaved = saleItemRepos.save(saleItem);
			

			if (recordSaved != null) {
				System.out.println("Item " + saleItem.getProduct().getName() + " - R$" + saleItem.getUnitPrice() + " X " + saleItem.getQuantity());
				boolean isUpdatedSale = updatePrice(saleItem.getSale().getId(), saleItem.getTotalPrice());
				
				if(isUpdatedSale)
					return new ResponseDTO(HttpStatus.CREATED.value(), new ListSaleDTO(saleItem.getSale()), "Pedido adicionado à Venda!");
			}
			
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda não Atualizada!");
	}
	
	@Transactional
	public boolean updatePrice(Long id, BigDecimal totalSaleItem) {
		
		BigDecimal currentTotal = BigDecimal.ZERO;
		BigDecimal newTotal = BigDecimal.ZERO;
		
		Optional<Sale> optSale = saleRepos.findById(id); 
		
		if(optSale.isEmpty())
			return false;
		
		// Atualizar o totalPrice
		if(optSale.get() != null) {
		    currentTotal = optSale.get().getTotalPrice() != null ? optSale.get().getTotalPrice() : BigDecimal.ZERO;
		    newTotal = currentTotal.add(totalSaleItem);
		    optSale.get().setTotalPrice(newTotal);
		}
		
		System.out.println("Total Venda atualizado de " + currentTotal + " para " + newTotal);
		
		if(saleRepos.save(optSale.get()) != null)
			return true;


		return false;
	}
	
}
