package com.mbe.viapdv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.category.Category;
import com.mbe.viapdv.repository.ICategoryRepository;


@Service
public class CategoryService {
	
	@Autowired
	ICategoryRepository categoryRepos;
	
	
	public List<Category> listActive() {
		return categoryRepos.findByActiveTrue();
	}
	
	public Category getByCode(String code) {
		
		Optional<Category> categoryOpt = categoryRepos.findByCode(code);
		
		if(categoryOpt.isEmpty())
			throw new RuntimeException("Categoria não existe");
		
		return categoryOpt.get();
	}
	

}
