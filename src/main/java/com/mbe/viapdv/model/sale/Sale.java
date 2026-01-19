package com.mbe.viapdv.model.sale;

import com.mbe.viapdv.enums.OperationStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.mbe.viapdv.model.saleItem.SaleItem;
import com.mbe.viapdv.model.user.User;
import org.hibernate.annotations.UuidGenerator;

import static org.hibernate.annotations.UuidGenerator.Style.AUTO;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "payment_method", length = 50, nullable = true)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column
    private OperationStatus status;

    @Column(name = "active")
    private Boolean isActive;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;
    
    @OneToMany(mappedBy = "sale")
    private List<SaleItem> saleItems;

    @Column(unique = true, updatable = false)
    private UUID uuid;


    public Sale() {}

    public Sale(User user, UUID uuid, String paymentMethod, BigDecimal totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.uuid = uuid;
        this.paymentMethod = paymentMethod;
        this.created = LocalDateTime.now();
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
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
    
    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return isActive;
    }
}
