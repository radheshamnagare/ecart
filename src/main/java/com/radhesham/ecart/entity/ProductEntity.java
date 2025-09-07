package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Table(name="m_product")
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long productId;

    @Column(name = "product_name")
    private String name;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="unit_price")
    private Double unitPrice;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="active")
    private boolean active;

    @Column(name="unit_stock")
    private int unitsInStock;

    @Column(name="insert_time",updatable = false)
    @CreationTimestamp
    private Date insertTime;

    @Column(name="update_time",insertable = false)
    @UpdateTimestamp
    private Date updateTime;

    @Override
    public String toString() {
        return "ProductEntity{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", imageUrl='" + imageUrl + '\'' +
                ", active=" + active +
                ", unitsInStock=" + unitsInStock +
                ", insertTime=" + insertTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
