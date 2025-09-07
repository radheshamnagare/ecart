package com.radhesham.ecart.repository;

import com.radhesham.ecart.entity.AddressEntity;
import com.radhesham.ecart.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity,Integer> {

    ViewAddressId getAddressEntityById(int id);

    boolean existsByIdAndUserEntityId(int addressId,int userId);

    List<AddressEntity> findByUserEntity(UserEntity userEntity);

    // OR: Find by user ID (preferred and cleaner)
    List<AddressEntity> findByUserEntity_Id(int userId);
}
