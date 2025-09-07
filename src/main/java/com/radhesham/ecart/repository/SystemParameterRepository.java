package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.SystemParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemParameterRepository extends JpaRepository< SystemParameterEntity,Integer> {

    SystemParameterEntity findByKey(String key);
}
