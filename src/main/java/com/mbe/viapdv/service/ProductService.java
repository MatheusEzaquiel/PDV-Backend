package com.mbe.viapdv.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.mbe.viapdv.exception.ConsistencySaleException;
import com.mbe.viapdv.model.product.dto.DetailProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.category.Category;
import com.mbe.viapdv.model.product.Product;
import com.mbe.viapdv.model.product.dto.BasicProductDTO;
import com.mbe.viapdv.model.product.dto.UpdateProductDTO;
import com.mbe.viapdv.model.product.dto.CreateProductDTO;
import com.mbe.viapdv.repository.ICategoryRepository;
import com.mbe.viapdv.repository.IProductRepository;
import com.mbe.viapdv.util.ResponseDTO;


@Service
public class ProductService {

	@Autowired
	IProductRepository productRepos;
	
	@Autowired
	ICategoryRepository categoryRepos;


	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	public ResponseDTO listActive() {

		List<BasicProductDTO> productList = productRepos.findByActiveTrue().stream()
				.map(BasicProductDTO::new).toList();

		return new ResponseDTO(HttpStatus.OK.value(), productList, "Lista de Produtos Ativos");
	}

	public ResponseDTO getById(Long id) {
		Optional<Product> optProduct = productRepos.findById(id);

		if (optProduct.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Produto não encontrado");

		DetailProductDTO basicProductDTO = new DetailProductDTO(optProduct.get());
		return new ResponseDTO(HttpStatus.OK.value(), basicProductDTO, "Produto Encontrado");
	}

	public ResponseDTO getBySKU(String sku) {
		
		Optional<Product> optProduct = productRepos.findBySku(sku);
		
		if(optProduct.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Produto não encontrado");
		
		return new ResponseDTO(HttpStatus.OK.value(), optProduct.get(), "Produto Encontrado");
		
	}
	
	public ResponseDTO create(CreateProductDTO data) {

		Product newProduct = new Product();

		Boolean existName = productRepos.existsByName(data.name());
		Boolean existSku = productRepos.existsBySku(data.sku());
		Boolean existBarcode = productRepos.existsByBarcode(data.sku());

		if (existName.booleanValue())
			return new ResponseDTO(HttpStatus.CONFLICT.value(), null, "Produto com este Nome já existe!");

		if (existSku.booleanValue())
			return new ResponseDTO(HttpStatus.CONFLICT.value(), null, "Produto com este SKU já existe!");

		if (existBarcode.booleanValue())
			return new ResponseDTO(HttpStatus.CONFLICT.value(), null, "Produto com este Código de Barra já existe!");


		if (data.name() != null && !data.name().isBlank())
			newProduct.setName(data.name());

		if (data.price() != null && (data.price().compareTo(BigDecimal.ZERO) == 1))
			newProduct.setPrice(data.price());

		if (data.sku() != null && !data.sku().isBlank())
			newProduct.setSku(data.sku());

		if (data.barcode() != null && !data.barcode().isBlank())
			newProduct.setBarcode(data.barcode());

		if (data.stockQuantity() != null && data.stockQuantity() > 0)
			newProduct.setStockQuantity(data.stockQuantity());

		BasicProductDTO dto = new BasicProductDTO(productRepos.save(newProduct));
		return new ResponseDTO(HttpStatus.CREATED.value(), dto, "Produto Criado com Sucesso!");
	}
	
	public ResponseDTO update(Long id, UpdateProductDTO data) {

		Optional<Product> productOpt = productRepos.findById(id);

		if (productOpt.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Produto não encontrado!");
		
		Product productToUpdt = productOpt.get();

		if (data.name() != null && !data.name().isBlank() && !data.name().equals(productToUpdt.getName()))
			productToUpdt.setName(data.name());

		if (data.price() != null &&
				(data.price().compareTo(BigDecimal.ZERO) == 1) &&
				!(data.price().compareTo(productToUpdt.getPrice()) == 0))
			productToUpdt.setPrice(data.price());

		if (data.sku() != null && !data.sku().isBlank() && !data.sku().equals(productToUpdt.getSku()))
			productToUpdt.setSku(data.sku());

		if (data.barcode() != null && !data.barcode().isBlank() && !data.barcode().equals(productToUpdt.getBarcode()))
			productToUpdt.setBarcode(data.barcode());

		if (data.stockQty() != null && data.stockQty() > 0 && data.stockQty() != productToUpdt.getStockQuantity())
			productToUpdt.setStockQuantity(data.stockQty());
		
		if (data.categoryId() != null && data.categoryId() > 0) {
			Optional<Category> optCategory = categoryRepos.findById(data.categoryId());

			if (optCategory.isEmpty())
				return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Categoria do Produto não encontrada!");
			
			productToUpdt.setCategory(optCategory.get());
		}

		BasicProductDTO dto = new BasicProductDTO(productRepos.save(productToUpdt));
		return new ResponseDTO(HttpStatus.OK.value(), dto, "Produto Atualizado com Sucesso!");
	}

	public ResponseDTO delete(Long id) {
		
		Optional<Product> optProduct = productRepos.findById(id);

		if (optProduct.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Produto não encontrado!");
		
		// Disable
		optProduct.get().setActive(false);
		optProduct.get().setUpdated(LocalDateTime.now());
		BasicProductDTO dto = new BasicProductDTO(productRepos.save(optProduct.get()));

		return new ResponseDTO(HttpStatus.CREATED.value(), dto, "Produto Desabilitado!");
	}

	public ResponseDTO search(String name, String code) {


		if(name != null && name.isBlank()
				&& code != null && code.isBlank()) {
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Pesquise por um identificador válido!");
		}

		// Código é prioridade de pesquisa por ser específico
		Optional<List<Product>> optProductList = null;
		if(code != null) {
			optProductList = productRepos.searchByCode(code);
		} else {
			optProductList = productRepos.searchByName(name);
		}

		if (optProductList.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Produto não encontrado!");

		List<BasicProductDTO> productDTOList = optProductList.get().stream()
				.map(BasicProductDTO::new)
				.toList();

		return new ResponseDTO(HttpStatus.ACCEPTED.value(), productDTOList, "Opções de Produto Encontrado!");
	}

	public void decreaseStock(Product product, int quantity) {
		if (product.getStockQuantity() < quantity) {
			logger.warn("Insufficient stock for the requested product");
			throw new ConsistencySaleException("Requested quantity exceeds available stock");
		}
		product.setStockQuantity(product.getStockQuantity() - quantity);
		productRepos.save(product);
	}

}
