package com.mbe.viapdv.model.user.dto;

import com.mbe.viapdv.model.user.User;

public record BasicUserDTO(Long id, String name, String email) {
	
	public BasicUserDTO(User user) {
		this(user.getId(), user.getName(), user.getEmail());
	}

}
