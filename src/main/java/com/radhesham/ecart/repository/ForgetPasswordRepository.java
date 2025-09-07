package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.ForgetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordEntity,Integer> {

    ForgetPasswordEntity findByToken(String token);

    List<ForgetPasswordEntity> findByStatus(String status);
}
