package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity p " + "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " + "OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " + "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProductEntity> searchByKeyword(@Param("keyword") String keyword);

    ViewProductStock getProductEntityByProductId(long id);
}
