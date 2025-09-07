package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@Table(name = "m_temp_cart_items")
@Entity
public class TempCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="product_id")
    private long productId;

    @Column(name="product_title")
    private String productTitle;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_image")
    private String productImage;

    @Column(name="description")
    private String description;

    @Column(name="status",columnDefinition = "varchar(30) default 'pending'")
    private String status;

    @Column(name="unit_price")
    private double unitPrice;

    @Column(name="unit_stock")
    private int unitStock;

    @Column(name="quantity")
    private int quantity;

    @Column(name="user-id")
    private int userId;

    @CreationTimestamp
    @Column(name="insert_time",updatable =false)
    private Date insertTime;

    @UpdateTimestamp
    @Column(name="update_time",insertable = false)
    private Date updateTime;
}
