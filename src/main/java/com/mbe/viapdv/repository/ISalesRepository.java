package com.mbe.viapdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbe.viapdv.model.sale.Sale;

public interface ISalesRepository extends JpaRepository<Sale, Long>{
	//List<Sale> findByActiveTrue();
	List<Sale> findByIsActiveTrue();
}
