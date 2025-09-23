package com.mbe.viapdv.service;

import java.util.List;

import com.mbe.viapdv.model.role.ListRoleDTO;
import com.mbe.viapdv.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.repository.IRoleRepository;

@Service
public class RoleService {
	
	@Autowired
	IRoleRepository roleRepos;

	public  ResponseDTO listActive() {
		List<ListRoleDTO> roles = roleRepos.findByActiveTrue().stream()
				.map(ListRoleDTO::new)
				.toList();

		return new ResponseDTO(HttpStatus.OK.value(), roles, "Active Roles");
	}

}

