package com.mbe.viapdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.repository.IRoleRepository;

@Service
public class RoleService {
	
	@Autowired
	IRoleRepository roleRepos;
	
	
	public List<Role> listActive() {
		return roleRepos.findByActiveTrue();
	}

}

