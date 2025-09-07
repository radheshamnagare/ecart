package com.radhesham.ecart.service;

import com.radhesham.ecart.bean.AddressDetails;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.controller.AddressController;
import com.radhesham.ecart.entity.AddressEntity;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
    @Autowired
    private AddressRepository addressRepository;

    public List<AddressDetails> getUserAddress(int userId) {
        List<AddressDetails> addressDetails = new ArrayList<>();
        try {
            logger.info("Entry in getUserAddress()");
            AddressEntity addressEntity = new AddressEntity();
            UserEntity user = new UserEntity();
            user.setId(userId);
            addressEntity.setUserEntity(user);
            List<AddressEntity> addressEntityList = addressRepository.findByUserEntity_Id(user.getId());
            addressEntityList.forEach(addr -> {
                AddressDetails bean = new AddressDetails();
                BeanUtils.copyProperties(addr, bean);
                addressDetails.add(bean);
            });
            logger.info("Exit from getUserAddress()");
        } catch (Exception e) {
            logger.error("Exception in getUserAddress():", e);
        }
        return addressDetails;
    }

    public boolean isAddressExists(AddressEntity addressEntity,int userId) {
        boolean res = false;
        try {

            Example e = Example.of(addressEntity);
            Optional<AddressEntity> address =addressRepository.findOne(e);
            if(address.isPresent()){
                if(address.get().getUserEntity().getId()==userId){
                    res=true;
                }
            }
        } catch (Exception e) {
            logger.error("Exception in isAddressExists():", e);
        }
        return res;
    }

    public boolean isAddressExists(int addressId,int userId) {
        boolean res = true;
        try {
            res=addressRepository.existsByIdAndUserEntityId(addressId,userId);
        } catch (Exception e) {
            logger.error("Exception in isAddressExists():", e);
        }
        return res;
    }

    public int saveAddress(List<AddressEntity> addressUpdateInsert,List<AddressEntity> addressDelete) {
        int total = 0;
        try {
            if(addressUpdateInsert.size()>0){
                Iterable<AddressEntity> iterableAdd = addressUpdateInsert;
                List<AddressEntity> saveAddress = addressRepository.saveAll(iterableAdd);
                if(saveAddress.size()>0){
                    total+=saveAddress.size();
                }

            }

            if(addressDelete.size()>0){
                Iterable<AddressEntity> iterableDelete = addressDelete;
                addressRepository.deleteAllInBatch(iterableDelete);
                total+=1;
            }

        }
        catch (Exception e) {
            logger.error("Exception in saveAddress():", e);
        }
        return total;
    }

    public boolean isAddressIdExists(int id){
        return addressRepository.getAddressEntityById(id).getId()>0;
    }
}
