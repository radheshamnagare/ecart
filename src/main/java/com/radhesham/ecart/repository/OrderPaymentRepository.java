package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.RazorpayOrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPaymentRepository extends JpaRepository<RazorpayOrdersEntity,Integer> {

    RazorpayOrdersEntity findByOrderId(String orderId);
}
