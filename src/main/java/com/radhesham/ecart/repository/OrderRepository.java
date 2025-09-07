package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {

    Optional<OrderEntity> findByOrderTrackingNumAndUserId(String orderTrackingNumber,int userId);

    Optional<OrderEntity> findByOrderTrackingNum(String orderTrackingNumber);

    Optional<OrderEntity> findByOrderIdAndUserId(int id,int userId);

    Optional<OrderEntity> findByRazorPayOrderId(String razorpayOrderId);
}
