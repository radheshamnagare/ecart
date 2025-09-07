package com.radhesham.ecart.common;

import com.radhesham.ecart.bean.ResponseStatus;
import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.entity.SystemErrorEntity;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.response.DefaultResponse;
import com.radhesham.ecart.service.SystemErrorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class CommonService {
    private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
    private static SystemErrorService systemErrorService;

    @Autowired
    public CommonService(SystemErrorService systemErrorService) {
        CommonService.systemErrorService = systemErrorService;
    }

    public static boolean isSuccessErrorCode(SystemError error) {
        return Objects.nonNull(error) && error.getErrorCode().equals(ConstantsPool.error_code_success);
    }

    public static SystemError setErrorMessage(String errorCode, String firstReplacer, String secondReplacer) {
        SystemError error = new SystemError();
        try {
            SystemErrorEntity errorInfo = systemErrorService.getSystemErrorDetailsByErrorCode(errorCode.trim());
            if (errorInfo != null) {
                String errorDesc = errorInfo.getErrorDescription().replace(ConstantsPool.first_constant, firstReplacer).replace(ConstantsPool.second_constant, secondReplacer);
                error.setErrorCode(errorCode);
                error.setErrorStatus(errorInfo.getErrorStatus());
                error.setErrorDescription(errorDesc);
            } else {
                error.setErrorCode(ConstantsPool.error_code_unknown);
                error.setErrorStatus(ConstantsPool.unknown);
                error.setErrorDescription(ConstantsPool.unknown);
            }
        } catch (Exception e) {
            logger.error("Exception in setErrorMessage() :", e);
        }
        return error;
    }

    public static void setErrorMessage(SystemError error, String errorCode, String firstReplacer, String secondReplacer) {
        try {
            SystemErrorEntity errorInfo = systemErrorService.getSystemErrorDetailsByErrorCode(errorCode.trim());
            if (errorInfo != null && error != null) {
                String errorDesc = errorInfo.getErrorDescription().replace(ConstantsPool.first_constant, firstReplacer).replace(ConstantsPool.second_constant, secondReplacer);
                error.setErrorCode(errorCode);
                error.setErrorStatus(errorInfo.getErrorStatus());
                error.setErrorDescription(errorDesc);
            } else {
                if (error == null) error = new SystemError();
                error.setErrorCode(ConstantsPool.error_code_unknown);
                error.setErrorStatus(ConstantsPool.unknown);
                error.setErrorDescription(ConstantsPool.unknown);
            }
        } catch (Exception e) {
            logger.error("Exception in setErrorMessage() :", e);
        }
    }

    public static DefaultResponse getDefaultResponse(SystemError error) {
        DefaultResponse response = new DefaultResponse();
        try {
            ResponseStatus status = getResponseStatus(error);
            response.setStatus(status);
        } catch (Exception e) {
            logger.error("Exception in CommonService::getDefaultResponse(): ", e);
        }
        return response;
    }

    public static ResponseStatus getResponseStatus(SystemError error) {
        ResponseStatus status = new ResponseStatus();
        try {
            status.setErrorCode(error.getErrorCode());
            status.setErrorStatus(error.getErrorStatus());
            status.setErrorDescription(error.getErrorDescription());
        } catch (Exception e) {
            logger.error("Exception in getResponseStatus() ", e);
        }
        return status;
    }

    public static UserEntity currentUserSession() {
        UserEntity currentUser = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            currentUser = (UserEntity) authentication.getPrincipal();
        } catch (Exception e) {
            logger.error("Exception in CommonService::currentUserSession():", e);
        }
        return currentUser;
    }

    public static String getUniqueId(){
        return UUID.randomUUID().toString();
    }
}
