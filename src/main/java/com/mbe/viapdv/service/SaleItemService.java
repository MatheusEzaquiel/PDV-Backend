package com.mbe.viapdv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.saleItem.SaleItem;
import com.mbe.viapdv.repository.ISaleItemRepository;

@Service
public class SaleItemService {
	
	@Autowired
	ISaleItemRepository saleItemRepos;
	
	public boolean save(SaleItem saleItem) {
		
		SaleItem saleItemOpt = saleItemRepos.save(saleItem);
		if(saleItemOpt != null)
			return true;
		else 
			return false;
		
	}
	
}
