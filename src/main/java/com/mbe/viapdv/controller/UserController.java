package com.mbe.viapdv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbe.viapdv.model.user.dto.CreateUserDTO;
import com.mbe.viapdv.model.user.dto.UpdateUserDTO;
import com.mbe.viapdv.service.RoleService;
import com.mbe.viapdv.service.UserService;
import com.mbe.viapdv.util.ResponseDTO;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@GetMapping
	public ResponseEntity<ResponseDTO> list() {
		ResponseDTO response = userService.listActiveUsers();
		return ResponseEntity.status(response.status()).body(response);
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> get(@PathVariable("id") Long id) {
		ResponseDTO response = userService.getById(id); 
		return ResponseEntity.status(response.status()).body(response);
    }
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@RequestBody CreateUserDTO data) {
		ResponseDTO response = userService.create(data);
		return ResponseEntity.status(response.status()).body(response);
	}
	
	@PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> get(@PathVariable("id") Long id, @RequestBody UpdateUserDTO data) {
		ResponseDTO response = userService.update(id, data); 
		return ResponseEntity.status(response.status()).body(response);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") Long id) {
		ResponseDTO response = userService.deleteUserById(id);
		return ResponseEntity.status(response.status()).body(response);
    }
	
	@GetMapping("/{id}/role")
	public ResponseEntity<ResponseDTO> getRoles(@PathVariable("id") Long id) {
		ResponseDTO response = userService.getRolesByUser(id);
		return ResponseEntity.status(response.status()).body(response);
	}
	
	@PostMapping("/{id}/role/{roleId}")
	public ResponseEntity<ResponseDTO> addRole(@PathVariable("id") Long userId, @PathVariable("roleId") Long roleId) {
		ResponseDTO response = userService.addRoleForUser(userId, roleId);
		return ResponseEntity.status(response.status()).body(response);
	}
	
	@DeleteMapping("/{id}/role/{roleId}")
	public ResponseEntity<ResponseDTO> removeRole(@PathVariable("id") Long userId, @PathVariable("roleId") Long roleId) {
		ResponseDTO response = userService.disableRoleForUser(userId, roleId);
		return ResponseEntity.status(response.status()).body(response);
	}
	
}
