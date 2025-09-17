package com.mbe.viapdv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mbe.viapdv.model.product.Product;


@Repository
public interface IProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByActiveTrue();
	
	@Query("SELECT p FROM Product p WHERE p.active = true AND LOWER(p.name) ILIKE LOWER(CONCAT(:name, '%'))")
	Optional<List<Product>> findByName(@Param("name") String name);

	@Query("SELECT p FROM Product p WHERE p.active = true AND LOWER(p.sku) ILIKE LOWER(CONCAT(:sku, '%'))")
	List<Product> searchBySku(@Param("sku") String sku);
	
	Optional<Product> findBySku(@Param("sku") String sku);

	Boolean existsBySku(@Param("sku") String sku);
	Boolean existsByBarcode(@Param("barcode") String barcode);
	Boolean existsByName(@Param("name") String name);

	@Query("SELECT p FROM Product p " +
			"WHERE p.active = TRUE " +
			"AND ( :name IS NULL " +
			"   OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
			"   OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :name, '%')) " +
			"   OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :name, '%')) )")
	Optional<List<Product>> search(@Param("name") String name);


}
