package com.mbe.viapdv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbe.viapdv.model.category.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByActiveTrue();

	Optional<Category> findByName(String categoryName);

	Optional<Category> findByCode(String code);
	
}
