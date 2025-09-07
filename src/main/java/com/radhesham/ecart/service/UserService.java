package com.radhesham.ecart.service;

import com.radhesham.ecart.bean.AddressDetails;
import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.bean.UserProfileDetails;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.common.ErrorConstants;
import com.radhesham.ecart.entity.AddressEntity;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.AddressRepository;
import com.radhesham.ecart.repository.UserRepository;
import com.radhesham.ecart.repository.ViewUserNameId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean isEmailExist(String email) {
        try {
            Optional<UserEntity> userDetail = userRepository.findByEmail(email);
            if (userDetail.isPresent()) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Exception in isEmailExist(): ", e);
        }
        return false;
    }

    public boolean isPhoneNoExist(String phone) {
        try {
            Optional<UserEntity> userDetails = userRepository.findByPhone(phone);
            if (userDetails.isPresent()) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Exception in isPhoneNoExist():",e);
        }
        return false;
    }

    public UserProfileDetails getUserProfileDetails(String userId) {
        UserProfileDetails userProfileDetails = new UserProfileDetails();
        try {
            Optional<UserEntity> user = userRepository.findByEmail(userId.trim());
            if (user.isPresent()) {
                userProfileDetails.setId(user.get().getId());
                userProfileDetails.setName(user.get().getName());
                userProfileDetails.setEmail(user.get().getEmail());
                userProfileDetails.setPhone(user.get().getPhone());
                //address details
                List<AddressEntity> addressList = addressRepository.findByUserEntity_Id(user.get().getId());
                List<AddressDetails> allAddress = new ArrayList<>();
                addressList.forEach(address -> {
                    AddressDetails addressHolder = new AddressDetails();
                    BeanUtils.copyProperties(address, addressHolder);
                    allAddress.add(addressHolder);
                });
                userProfileDetails.setAddressDetails(allAddress);
            }
        } catch (Exception e) {
            logger.error("Exception in getUserProfileDetails(): ", e);
        }
        return userProfileDetails;
    }

    public ViewUserNameId getUserDetailsByEmail(String email){
        return userRepository.getUserEntityByEmail(email);
    }
}
