package com.mbe.viapdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbe.viapdv.model.product.Product;
import com.mbe.viapdv.model.saleItem.SaleItem;

@Repository
public interface ISaleItemRepository extends JpaRepository<SaleItem, Long>{

	List<Product> findByActiveTrue();
	
}
