package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table(name = "m_product_notification")
@Entity
public class ProductNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "product_id")
    int productId;

    @Column(name = "user_id")
    int userId;

    @Column(name="status",columnDefinition = "varchar(55) default 'pending'")
    String status;

    @Column(name = "insert_time", updatable = false)
    Date insertTime;

    @Column(name = "update_time", insertable = false)
    Date updateTime;
}
