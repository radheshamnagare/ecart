package com.radhesham.ecart.model;

import com.radhesham.ecart.bean.AddressDetails;
import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.bean.UserProfileDetails;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.common.ErrorConstants;
import com.radhesham.ecart.entity.AddressEntity;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.request.AddressAddRequest;
import com.radhesham.ecart.response.AddressDetailsResponse;
import com.radhesham.ecart.response.DefaultResponse;
import com.radhesham.ecart.response.UserProfileResponse;
import com.radhesham.ecart.service.AddressService;
import com.radhesham.ecart.service.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;

@Data
public class ManageProfile {

    private static final Logger logger = LoggerFactory.getLogger(ManageProfile.class);
    private UserService userService;
    private AddressService addressService;

    private UserProfileResponse getUserProfileResponse(SystemError error, UserProfileDetails userProfileDetails) {
        UserProfileResponse response = new UserProfileResponse();
        try {
            response.setAddressDetails(userProfileDetails.getAddressDetails());
            response.setId(userProfileDetails.getId());
            response.setName(userProfileDetails.getName());
            response.setEmail(userProfileDetails.getEmail());
            response.setPhone(userProfileDetails.getPhone());
            //error
            response.setErrorCode(error.getErrorCode());
            response.setErrorStatus(error.getErrorStatus());
            response.setErrorDescription(error.getErrorDescription());
        } catch (Exception e) {
            logger.error("Exception in getUserProfileResponse():", e);
        }
        return response;
    }

    public UserProfileResponse manageUserProfileResponse() {
        UserProfileResponse response;
        UserProfileDetails userProfileDetails;
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            logger.info("Entry in manageUserProfileResponse()");
            UserEntity currentUserSession = CommonService.currentUserSession();
            if (Objects.nonNull(currentUserSession)) {
                userProfileDetails = userService.getUserProfileDetails(currentUserSession.getUsername());
            } else {
                userProfileDetails = new UserProfileDetails();
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "");
            }
            response = getUserProfileResponse(error, userProfileDetails);
            logger.info("Exit from manageUserProfileResponse()");
        } catch (Exception e) {
            CommonService.setErrorMessage(error, ConstantsPool.error_code_failed, "", "");
            response = getUserProfileResponse(error, new UserProfileDetails());
            logger.error("Exception in manageUserProfileResponse(): ", e);
        }
        return response;
    }

    private AddressDetailsResponse getAddressDetailsResponse(List<AddressDetails> addressDetails, SystemError error) {
        AddressDetailsResponse addressDetailsResponse = new AddressDetailsResponse();
        try {
            addressDetailsResponse.setErrorCode(error.getErrorCode());
            addressDetailsResponse.setErrorStatus(error.getErrorStatus());
            addressDetailsResponse.setErrorDescription(manageUserProfileResponse().getErrorDescription());
            addressDetailsResponse.setAddressDetails(addressDetails);
        } catch (Exception e) {
            logger.error("Exception getAddressDetailsResponse():", e);
        }
        return addressDetailsResponse;
    }

    public AddressDetailsResponse manageAddressDetails() {
        AddressDetailsResponse response;
        List<AddressDetails> addressDetails = new ArrayList<>();
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            logger.info("Entry in manageAddressDetails()");
            UserEntity currentUserSession = CommonService.currentUserSession();
            if (Objects.nonNull(currentUserSession)) {
                if (Objects.nonNull(currentUserSession)) {
                    addressDetails.addAll(addressService.getUserAddress(currentUserSession.getId()));
                } else {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "");
                }
            } else {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "");
            }
            response = getAddressDetailsResponse(addressDetails, error);
            logger.info("Exit from manageAddressDetails()");
        } catch (Exception e) {
            CommonService.setErrorMessage(error, ConstantsPool.error_code_failed, "", "");
            response = getAddressDetailsResponse(addressDetails, error);
            logger.error("Exception manageAddressDetails():", e);
        }
        return response;
    }

    private boolean isValidStatus(String status){
        boolean res=true;
        try{
            if(CommonValidator.isEmpty(status)){
                return false;
            }
            else if(!status.equals(ConstantsPool.STATE_ONE) && !status.equals(ConstantsPool.STATE_ZERO)){
               return false;
            }
        }catch (Exception e){
            logger.error("Exception in isValidStatus()",e);
        }
        return res;
    }
    private boolean isValidRequestForAddAddress(AddressAddRequest addressAddRequest, List<AddressEntity> validInsertList, List<AddressEntity> validDeleteList, int userId, Map<AddressDetails, SystemError> invalidData) {
        SystemError error = null;
        try {
            logger.info("Entry in isValidRequestForAddAddress()");
            for (AddressDetails addressDetails : addressAddRequest.getAddressDetails()) {
                if (addressDetails.getId() > 0 && !addressService.isAddressIdExists(addressDetails.getId())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_ID, String.valueOf(addressDetails.getId()));
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isEmpty(addressDetails.getCity())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_required, ErrorConstants.DESC_CITY, addressDetails.getCity());
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isNumeric(addressDetails.getCity())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_CITY, addressDetails.getCity());
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isEmpty(addressDetails.getStreet())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_required, ErrorConstants.DESC_STREET, addressDetails.getState());
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isNumeric(addressDetails.getStreet())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_STREET, addressDetails.getState());
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isEmpty(addressDetails.getHouseNum())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_required, ErrorConstants.DESC_HOUSE_NO, addressDetails.getHouseNum());
                    invalidData.put(addressDetails, error);
                } else if (!CommonValidator.isNumeric(addressDetails.getHouseNum())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_HOUSE_NO, addressDetails.getHouseNum());
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isEmpty(addressDetails.getCountry())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_required, ErrorConstants.DESC_COUNTRY, addressDetails.getHouseNum());
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isNumeric(addressDetails.getCountry())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_COUNTRY, addressDetails.getHouseNum());
                    invalidData.put(addressDetails, error);
                } else if (CommonValidator.isEmpty(addressDetails.getZipCode())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_required, ErrorConstants.DESC_ZIP_CODE, addressDetails.getZipCode());
                    invalidData.put(addressDetails, error);
                } else if (!CommonValidator.isNumeric(addressDetails.getZipCode())) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_ZIP_CODE, addressDetails.getZipCode());
                    invalidData.put(addressDetails, error);
                } else if (!isValidStatus(String.valueOf(addressDetails.getDefaultAddress()))) {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_STATUS, String.valueOf(addressDetails.getDefaultAddress()));
                    invalidData.put(addressDetails, error);
                } else {
                    AddressEntity addressEntity = new AddressEntity();
                    BeanUtils.copyProperties(addressDetails, addressEntity);
                    if (addressDetails.getAction().equals(ConstantsPool.ACTION_ADD) && addressService.isAddressExists(addressEntity, userId)) {
                        CommonService.setErrorMessage(error, ConstantsPool.error_code_duplicate, ErrorConstants.DESC_ADDRESS, addressEntity.toString());
                        invalidData.put(addressDetails, error);
                    } else {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(userId);
                        addressEntity.setUserEntity(userEntity);
                        if (addressDetails.getAction().equals(ConstantsPool.ACTION_ADD))
                            validInsertList.add(addressEntity);
                        else if (addressDetails.getAction().equals(ConstantsPool.ACTION_DELETE)) {
                            validDeleteList.add(addressEntity);
                        }
                    }
                }
            }
            logger.info("Exit from isValidRequestForAddAddress()");
        } catch (Exception e) {
            logger.error("Exception in isValidRequestForAddAddress():", e);
        }
        return !validInsertList.isEmpty() || !validDeleteList.isEmpty();
    }

    public DefaultResponse manageSaveAddress(AddressAddRequest addressAddRequest) {
        DefaultResponse response;
        List<AddressEntity> validInsertList = new ArrayList<>();
        List<AddressEntity> validDeleteList = new ArrayList<>();
        Map<AddressDetails, SystemError> invalidData = new HashMap<>();
        SystemError error=CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try {
            logger.info("Entry in manageSaveAddress()");
            UserEntity user = CommonService.currentUserSession();
            if (Objects.nonNull(user)) {
                if (Objects.nonNull(user) && isValidRequestForAddAddress(addressAddRequest, validInsertList, validDeleteList,user.getId(), invalidData)) {
                    int total = addressService.saveAddress(validInsertList, validDeleteList);
                    if(total<=0)
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_INVALID_RECORD, "");
                } else {
                    error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_INVALID_RECORD, "");
                }
            } else {
                error = CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "");
            }
            response = CommonService.getDefaultResponse(error);
            logger.info("Exit from manageSaveAddress()");
        } catch (Exception e) {
            error = CommonService.setErrorMessage(ConstantsPool.error_code_failed, "", "");
            response = CommonService.getDefaultResponse(error);
            logger.error("Exception in manageSaveAddress():", e);
        }
        return response;
    }
}
