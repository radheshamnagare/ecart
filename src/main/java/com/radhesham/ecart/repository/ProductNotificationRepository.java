package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.ProductNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductNotificationRepository extends JpaRepository<ProductNotificationEntity,Integer> {

    List<ProductNotificationEntity> findByStatus(String status);
}
