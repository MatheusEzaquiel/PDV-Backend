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
    	sale.setIsActive(true);
    	
		saleRepos.save(sale);
		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda Criada!");
	}
	
	@Transactional
	public ResponseDTO addSalesItemInSale(Long saleId, String skuCode, CreateSaleItemDTO data) {
		
    	Optional<Sale> optCurrentSale = null;
    	SaleItem saleItem = new SaleItem();
    	Product product = new Product();
    	
    	// Resgatar Venda Atual da Sessão
    	optCurrentSale = saleRepos.findById(saleId);
    	
    	if(optCurrentSale.isEmpty()) 
    		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda Não encontrada");
    	
    	
        	// Resgatar Produto pelo Código e Quantidade da Sessão
        	Optional<Product> optProduct = productRepos.findBySku(skuCode);
        	if(optProduct.isEmpty()) 
        		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda Não encontrada");
        	
        	// Product Price x Product Qty
			BigDecimal totalSaleItem = product.getPrice().multiply(new BigDecimal(data.quantity()));

			// Add Product in SaleItem
			saleItem.setProduct(product);
			saleItem.setQuantity(data.quantity());
			saleItem.setUnitPrice(product.getPrice());
			saleItem.setTotalPrice(totalSaleItem);
			saleItem.setSale(optCurrentSale.get());
			saleItem.setCreated(LocalDateTime.now());
			saleItem.setActive(true);

			if (saleItemService.save(saleItem)) {
				System.out.println("Item " + saleItem.getProduct().getName() + " - R$" + saleItem.getUnitPrice() + " X " + saleItem.getQuantity());
				boolean isUpdatedSale = updatePrice(saleItem.getSale().getId(), saleItem.getTotalPrice());
				
				if(isUpdatedSale)
					return new ResponseDTO(HttpStatus.CREATED.value(), new ListSaleDTO(saleItem.getSale()), "Pedido adicionado à Venda!");
			}
			
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Venda não Atualizada!");
	}
	
	@Transactional
	private boolean updatePrice(Long id, BigDecimal totalSaleItem) {
		
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
