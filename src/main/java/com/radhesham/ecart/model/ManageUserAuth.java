package com.radhesham.ecart.model;

import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.common.ErrorConstants;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.ViewUserNameId;
import com.radhesham.ecart.request.LoginUserRequest;
import com.radhesham.ecart.request.RegisterUserRequest;
import com.radhesham.ecart.request.ForgetPasswordRequest;
import com.radhesham.ecart.request.ResetUserPasswordRequest;
import com.radhesham.ecart.response.DefaultResponse;
import com.radhesham.ecart.response.LoginResponse;
import com.radhesham.ecart.service.AuthenticationService;
import com.radhesham.ecart.service.ForgetPasswordService;
import com.radhesham.ecart.service.JwtService;
import com.radhesham.ecart.service.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Data
public class ManageUserAuth {

    private static final Logger logger = LoggerFactory.getLogger(ManageUserAuth.class);
    private JwtService jwtService;
    private AuthenticationService authenticationService;
    private UserService userService;
    private ForgetPasswordService forgetPasswordService;

    private SystemError isValidName(String name) {
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            if(CommonValidator.isEmpty(name)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid, ErrorConstants.DESC_NAME,name);
            }else if (CommonValidator.isNumeric(name)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_NAME,name);
            }
        } catch (Exception e) {
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            logger.error("Exception in isValidName() ", e);
        }
        return error;
    }

    private SystemError isValidEmail(String email) {
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            if(CommonValidator.isEmpty(email)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_EMAIL,email);
            }else if(!CommonValidator.isValidEmail(email)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_EMAIL,email);
            }
        } catch (Exception e) {
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            logger.error("Exception in isValidName() ", e);
        }
        return error;
    }



    private SystemError isValidPassword(String password) {
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            if(CommonValidator.isEmpty(password)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_PASSWORD,password);
            }else if(CommonValidator.isNumeric(password)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_PASSWORD,password);
            }
        } catch (Exception e) {
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            logger.error("Exception in isValidName() ", e);
        }
        return error;
    }

    private SystemError isValidPhone(String phone) {
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            if(!CommonValidator.isValidMobileNo(phone)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_PHONE,phone);
            }
        } catch (Exception e) {
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            logger.error("Exception in isValidName() ", e);
        }
        return error;
    }


    private SystemError isValidUserSignUpRequest(RegisterUserRequest registerUserRequest){
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try{
            error = isValidName(registerUserRequest.getName());
            if(!CommonService.isSuccessErrorCode(error))
                return error;
            error = isValidEmail(registerUserRequest.getEmail());
            if(!CommonService.isSuccessErrorCode(error))
                return error;
            boolean emailExist= userService.isEmailExist(registerUserRequest.getEmail());
            if(!emailExist){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_EMAIL, registerUserRequest.getEmail());
                return error;
            }
            error = isValidPhone(registerUserRequest.getPhone());
            if(!CommonService.isSuccessErrorCode(error))
                return error;
            boolean isPhoneExists =userService.isPhoneNoExist(registerUserRequest.getPhone());
            if(!isPhoneExists){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_PHONE, registerUserRequest.getPhone());
                return error;
            }
            error=isValidPassword(registerUserRequest.getPassword());
            if(!CommonService.isSuccessErrorCode(error))
                return error;
        }catch (Exception e){
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            logger.error("Exception in isValidUserSignUpRequest() ",e);
        }
        return error;
    }

    public DefaultResponse manageUserSignUp(RegisterUserRequest registerUserRequest) {
        DefaultResponse response = null;
        SystemError error=CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try {
            logger.info("Entry in manageUserSignUp()");
            error = isValidUserSignUpRequest(registerUserRequest);
            if(CommonService.isSuccessErrorCode(error)){
                UserEntity registeredUser = authenticationService.signup(registerUserRequest);
                if(Objects.isNull(registeredUser)){
                    CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
                }
            }
            response = CommonService.getDefaultResponse(error);
            logger.info("Exit from manageUserSignUp()");
        } catch (Exception e) {
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            response = CommonService.getDefaultResponse(error);
            logger.error("Exception in manageUserSignUp(): ",e);
        }
        return response;
    }


    private LoginResponse getLoginResponse(String jwtToken,long tokenExpirationTime,SystemError error){
        LoginResponse loginResponse = new LoginResponse();
        try{
           loginResponse.setToken(jwtToken);
           loginResponse.setExpiresIn(tokenExpirationTime);
           loginResponse.setStatus(CommonService.getResponseStatus(error));
        }catch (Exception e){
            logger.error("Exception in getLoginResponse() ",e);
        }
        return loginResponse;
    }

    public LoginResponse manageLogin(LoginUserRequest loginUserRequest){
        LoginResponse loginResponse = null;
        String jwtToken = "";
        long expiredTime=0;
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try{
            logger.info("Entry in manageLogin()");
            UserEntity authenticatedUser = authenticationService.authenticate(loginUserRequest);
            if(Objects.nonNull(authenticatedUser) && authenticatedUser.getId()>0){
                jwtToken = jwtService.generateToken(authenticatedUser);
                expiredTime =jwtService.getExpirationTime();
            }else{
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_USER,"");
            }
            loginResponse = getLoginResponse(jwtToken,expiredTime,error);
            logger.info("Exit from manageLogin()");
        }catch (Exception e){
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            loginResponse = getLoginResponse(jwtToken,expiredTime,error);
            logger.error("Exception in manageLogin(): ",e);
        }
        return loginResponse;
    }

    private SystemError isValidRequestForResetPassword(ForgetPasswordRequest forgetPasswordRequest){
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
       try{
           if(CommonValidator.isEmpty(forgetPasswordRequest.getEmailId())){
               CommonService.setErrorMessage(error,ConstantsPool.error_code_required,ErrorConstants.DESC_USER, forgetPasswordRequest.getEmailId());
           }else{
              boolean isEmailExists= userService.isEmailExist(forgetPasswordRequest.getEmailId());
              if(!isEmailExists){
                  CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_EMAIL, forgetPasswordRequest.getEmailId());
              }
           }
       }catch (Exception e){
           logger.error("Exception in isValidRequestForResetPassword() ",e);
       }
       return error;
    }

    public DefaultResponse manageForgetPassword(ForgetPasswordRequest forgetPasswordRequest){
        DefaultResponse response=null;
        SystemError error=CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        int total=0;
        try{
            error=isValidRequestForResetPassword(forgetPasswordRequest);
            if(Objects.nonNull(error) && CommonService.isSuccessErrorCode(error)){
                ViewUserNameId user = userService.getUserDetailsByEmail(forgetPasswordRequest.getEmailId());
                if(Objects.nonNull(user)){
                    total = forgetPasswordService.saveForgetPasswordRequest(user.getId());
                    if(total<=0){
                        CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_FORGET_PASSWORD,"null");
                    }
                }else{
                    CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_USER,"null");
                }
            }
            response = CommonService.getDefaultResponse(error);
        }catch (Exception e){
            CommonService.setErrorMessage(error,ConstantsPool.error_code_unknown,"","");
            response = CommonService.getDefaultResponse(error);
            logger.error("Exception in manageResetPassword() ",e);
        }
        return response;
    }


    private SystemError isValidResetPasswordRequest(ResetUserPasswordRequest resetUserPasswordRequest){
        SystemError error=CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try{
            logger.info("Entry in  isValidResetPasswordRequest()");
            if(CommonValidator.isEmpty(resetUserPasswordRequest.getUserToken())){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_required,ErrorConstants.DESC_PASSWORD,resetUserPasswordRequest.getUserToken());
                return error;
            }

            int userId = forgetPasswordService.findUserIdByToken(resetUserPasswordRequest.getUserToken());
            if(userId<=0){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_USER,String.valueOf(userId));
                return error;
            }else{
                resetUserPasswordRequest.setUserId(userId);
            }

            if(CommonValidator.isEmpty(resetUserPasswordRequest.getNewPassword())){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_required,ErrorConstants.DESC_NEW_PASSWORD,resetUserPasswordRequest.getNewPassword());
                return error;
            }

            if(CommonValidator.isEmpty(resetUserPasswordRequest.getConfirmPassword())){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_required,ErrorConstants.DESC_CONFIRM_PASSWORD,resetUserPasswordRequest.getConfirmPassword());
                return error;
            }

            if(!resetUserPasswordRequest.getNewPassword().equals(resetUserPasswordRequest.getConfirmPassword())){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_NEW_AND_CONFIRM_PASSWORD_NOT_MATCH,resetUserPasswordRequest.getNewPassword());
                return error;
            }

            if(forgetPasswordService.isSameOldPasswordAndNewPassword(resetUserPasswordRequest.getNewPassword() ,userId)){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_PASSWORD,resetUserPasswordRequest.getNewPassword());
                return error;
            }

            logger.info("Exit from  isValidResetPasswordRequest()");
        }catch (Exception e){
            logger.error("Exception in  isValidResetPasswordRequest():",e);
        }
        return error;
    }

    public DefaultResponse manageResetPassword(ResetUserPasswordRequest resetUserPasswordRequest){
        DefaultResponse response=null;
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try{
            logger.info("Entry in manageResetPassword()");
            error = isValidResetPasswordRequest(resetUserPasswordRequest);
            if(error.getErrorCode().equals(ConstantsPool.error_code_success)){
                int total =forgetPasswordService.changeUserPassword(resetUserPasswordRequest);
                if(total<=0){
                    CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_PASSWORD,"");
                }
            }
            logger.info("Exit from manageResetPassword()");
        }catch (Exception e){
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            logger.error("Exception in manageResetPassword():",e);
        }
        return response;
    }
}
