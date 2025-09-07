package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.SendEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendEmailRepository extends JpaRepository<SendEmailEntity,Integer> {

    List<SendEmailEntity> findByStatus(String status);
}
