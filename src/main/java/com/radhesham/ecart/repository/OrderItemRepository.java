package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity,Integer> {

   List<OrderItemEntity> findByOrderOrderId(int orderId);
}
