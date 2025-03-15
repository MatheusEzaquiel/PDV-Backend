package com.mbe.viapdv.model.category;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.mbe.viapdv.model.product.Product;

@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100, unique = true)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, length = 200)
	private String code;

	@Column(nullable = false)
	private Boolean active = true;

	@Column(nullable = false, updatable = false)
	private LocalDateTime created;

	@Column(nullable = false)
	private LocalDateTime updated;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Product> products;

	@PrePersist
	protected void onCreate() {
		this.created = LocalDateTime.now();
		this.updated = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updated = LocalDateTime.now();
	}
	
	public Category() {}

	public Category(String name, String description, String code) {
		this.name = name;
		this.description = description;
		this.code = code;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}
