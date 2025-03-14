package com.mbe.viapdv.model.userRole;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Table(name = "user_roles")
@Entity
public class UserRole {

	@EmbeddedId
	private UserRoleId id;

	@Column(nullable = false)
	private Boolean active = true;

	@Column(nullable = false, updatable = false)
	private LocalDateTime created;

	@Column(nullable = false)
	private LocalDateTime updated;

	@PrePersist
	protected void onCreate() {
		created = LocalDateTime.now();
		updated = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updated = LocalDateTime.now();
	}
	


	public UserRole() {}

	public UserRole(UserRoleId id, Boolean active, LocalDateTime created, LocalDateTime updated) {
		this.id = id;
		this.active = active;
		this.created = created;
		this.updated = updated;
	}
	
	public UserRole(UserRoleId id) {
		this.id = id;
		this.active = true;
		this.created = LocalDateTime.now();
		this.updated = null;
	}

	// Getters e Setters
	public UserRoleId getId() {
		return id;
	}

	public void setId(UserRoleId id) {
		this.id = id;
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
