package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.SystemErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemErrorRepository extends JpaRepository< SystemErrorEntity,Integer> {

    Optional<SystemErrorEntity> findByErrorCode(String errorCode);
}

