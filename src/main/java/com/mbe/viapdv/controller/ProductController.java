package com.mbe.viapdv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    /*
    @GetMapping("/products/category/{category}")
    public String delete(@PathVariable("category") String code) {

		List<Product> productsByCategory = null;
		List<Product> activeProductsByCategory = null;
		
		List<Category> categories = categoryService.listActive();
		
		// Search Category by Code
    	Category currentCategory = categoryService.getByCode(code);
    	productsByCategory = currentCategory.getProducts();
    	
    	
    	activeProductsByCategory = productsByCategory.stream()
    			.filter(filterProduct -> filterProduct.getActive())
    			.collect(Collectors.toList());
    	
    		
		 return "Removido";
    }
    
    @GetMapping("/products/search/{name}")
    public ResponseEntity<List<BasicProductDTO>> searchByName(@PathVariable("name") String name) {
    	
    	Product product = null;
    	List<Product> productList = null;
    	
    	
        Optional<List<Product>> productsOpt = productRepository.findByName(name);

        if(productsOpt.isPresent()) {
        	
        	productList = productsOpt.get();
        }
        
        List<BasicProductDTO> data = productList
        	.stream()
        	.map(p -> new BasicProductDTO(p.getId(), p.getName(), p.getSku(), null))
        	.toList();
        
       return ResponseEntity.ok(data);
    }
    
    @GetMapping("/products/search/sku/{sku}")
    public ResponseEntity<List<BasicProductDTO>> searchBySku(@PathVariable("sku") String sku) {
    	
    	List<Product> productSkuList = productRepository.searchBySku(sku);
        
        if(productSkuList.size() > 0) {
        	
        	  List<BasicProductDTO> data = productSkuList
        	        	.stream()
        	        	.map(p -> new BasicProductDTO(p.getId(), p.getName(), p.getSku(), null))
        	        	.toList();
        	        
        	       return ResponseEntity.ok(data);
        }

       
       return ResponseEntity.notFound().build();
    }
    */
    
}
