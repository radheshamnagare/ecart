package com.radhesham.ecart.model;

import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.common.ErrorConstants;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.ViewUserNameId;
import com.radhesham.ecart.request.NotificationRequest;
import com.radhesham.ecart.response.DefaultResponse;
import com.radhesham.ecart.service.NotificationService;
import com.radhesham.ecart.service.ProductService;
import com.radhesham.ecart.service.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;

import java.util.Objects;

@Data
public class ManageNotification {
    private NotificationService notificationService;
    private UserService userService;
    private ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ManageProduct.class);

    private SystemError isValidRequestForAddNotification(NotificationRequest notificationRequest){
      SystemError error=CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
      try{
          if(Objects.isNull(notificationRequest)){
              CommonService.setErrorMessage(ConstantsPool.error_code_invalid,ErrorConstants.DESC_BEAN,"null");
          }else if(notificationRequest.getProductId()<=0){
              CommonService.setErrorMessage(ConstantsPool.error_code_invalid,ErrorConstants.DESC_PRODUCT,"0");
          }else{
              error = productService.isProductExistById(notificationRequest.getProductId());
              if(CommonService.isSuccessErrorCode(error) && notificationService.isNotificationExists(notificationRequest.getUserId() ,notificationRequest.getProductId())){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_duplicate,ErrorConstants.DESC_NOTIFICATION,"");
              }
          }
      }catch (Exception e){
          CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
          logger.error("Exception in isValidRequestForAddNotification():",e);
      }
      return error;
    }
    public DefaultResponse manageNotificationAdd(NotificationRequest notificationRequest){
        DefaultResponse response =null;
        SystemError error=CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try{
            logger.info("Entry in manageNotificationAdd()");
            UserEntity userDetails = CommonService.currentUserSession();
            if(Objects.nonNull(userDetails)){
                if(Objects.nonNull(userDetails)){
                    notificationRequest.setUserId(userDetails.getId());
                    error = isValidRequestForAddNotification(notificationRequest);
                    if(CommonService.isSuccessErrorCode(error) && notificationService.saveNotification(notificationRequest)>0){
                        logger.info("manageNotificationAdd() notification save successfully {}");
                    }else{
                        CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_NOTIFICATION,"not save successfully");
                    }
                }else{
                    CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_USER,"null");
                }
                response =CommonService.getDefaultResponse(error);
            }else{
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER,"null");
            }
            logger.info("Exit from manageNotificationAdd()");
        }catch (Exception e){
            CommonService.setErrorMessage(ConstantsPool.error_code_failed,"","");
            response=CommonService.getDefaultResponse(error);
            logger.error("Exception in manageNotificationAdd():",e);
        }
        return response;
    }
}
