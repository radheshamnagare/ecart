package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@Table(name="m_orders")
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer orderId;

    @Column(name="order_tracking_no")
    private String orderTrackingNum;

    @Column(name="rezorpay_order_id")
    private String razorPayOrderId;

    @Column(name="rezorpay_payment_id")
    private String razorPayPaymentId;

    @Column(name="order_status")
    private String orderStatus;

    @Column(name="total_price")
    private Double totalPrice;

    @Column(name="total_quantity")
    private Integer totalQuantity;

    @Column(name="delivery_date")
    private Date deliveryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    @Column(name="insert_time",updatable = false)
    @CreationTimestamp
    private Date dateCreated;

    @Column(name="update_time",insertable = false)
    @UpdateTimestamp
    private Date lastUpdated;
}
