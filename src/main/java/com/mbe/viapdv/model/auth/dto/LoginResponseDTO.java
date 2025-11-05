package com.mbe.viapdv.model.auth.dto;

public record LoginResponseDTO(String accessToken, Long expiresIn) {
}
