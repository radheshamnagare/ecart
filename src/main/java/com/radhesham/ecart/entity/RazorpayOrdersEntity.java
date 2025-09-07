package com.radhesham.ecart.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@Table(name = "t_razorpay_orders")
@Entity
public class RazorpayOrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "order_id")
    String orderId;

    @Column(name = "entity")
    String entity;

    @Column(name = "amount")
    int amount;

    @Column(name = "amount_paid")
    int amountPaid;

    @Column(name = "amount_due")
    int amountDue;

    @Column(name = "currency")
    String currency;

    @Column(name = "receipt")
    String receipt;

    @Column(name = "status")
    String status;

    @Column(name = "attempts")
    int attempts;

    @Column(name="remarks",columnDefinition = "varchar(255) default ''")
    String remarks;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    Date updatedAt;
}
