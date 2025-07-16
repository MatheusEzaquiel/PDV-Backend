package com.mbe.viapdv.service;

import java.util.List;
import java.util.Optional;

import com.mbe.viapdv.model.category.dto.ListCategoryDTO;
import com.mbe.viapdv.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.category.Category;
import com.mbe.viapdv.repository.ICategoryRepository;


@Service
public class CategoryService {
	
	@Autowired
	ICategoryRepository categoryRepos;
	
	
	public ResponseDTO listActive() {
		List<ListCategoryDTO> categoryList = categoryRepos.findByActiveTrue()
				.stream().map(ListCategoryDTO::new)
				.toList();

		return new ResponseDTO(HttpStatus.CREATED.value(), categoryList, "Categorias de Produto retornadas com sucesso!");
	}
	
	public Category getByCode(String code) {
		
		Optional<Category> categoryOpt = categoryRepos.findByCode(code);
		
		if(categoryOpt.isEmpty())
			throw new RuntimeException("Categoria não existe");
		
		return categoryOpt.get();
	}
	

}
