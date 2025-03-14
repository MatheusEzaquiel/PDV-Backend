package com.mbe.viapdv.model.role;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mbe.viapdv.model.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Table(name="roles")
@Entity(name = "Role")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private Boolean active;
	
	@JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private LocalDateTime created;
	
	@JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
	private LocalDateTime updated;

	@ManyToMany(mappedBy = "roles") // O lado inverso da relação
	private Set<User> users; // Conjunto de usuários
	
	public Role() {}
	
	public Role(Long id, String name, String description, Boolean active, LocalDateTime created, LocalDateTime updated,
			Set<User> users) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.active = active;
		this.created = created;
		this.updated = updated;
		this.users = users;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
}
