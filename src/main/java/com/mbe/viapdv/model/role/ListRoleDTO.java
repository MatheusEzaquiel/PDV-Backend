package com.mbe.viapdv.model.role;

public record ListRoleDTO(Long id, String name) {

    public ListRoleDTO(Role role) {
        this(role.getId(), role.getName());
    }
}
