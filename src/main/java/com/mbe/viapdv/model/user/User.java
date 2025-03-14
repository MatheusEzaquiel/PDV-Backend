package com.mbe.viapdv.model.user;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.model.user.dto.CreateUserDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Table(name = "users")
@Entity(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;
	private Boolean active;
	
	@JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private LocalDateTime created;
	
	@JsonFormat(pattern = "dd-MM-yyy'T'HH:mm:ss")
	private LocalDateTime updated;

	@ManyToMany
	@JoinTable(name = "user_roles", // Tabela de relação
		joinColumns = @JoinColumn(name = "user_id"), // Chave estrangeira para user
		inverseJoinColumns = @JoinColumn(name = "role_id") // Chave estrangeira para role
	)
	private Set<Role> roles;
	
	public User() {}

	public User(CreateUserDTO data) {
		this.name = data.name();
		this.email = data.email();
		this.password = "pdv";
		this.active = true;
		this.created = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}
