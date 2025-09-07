package com.radhesham.ecart.service;

import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.SystemErrorEntity;
import com.radhesham.ecart.repository.SystemErrorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemErrorService {

    private static final Logger logger = LoggerFactory.getLogger(SystemErrorService.class);
    @Autowired
    SystemErrorRepository systemErrorRepository;

    public SystemErrorEntity getSystemErrorDetailsByErrorCode(String errorCode){
        SystemErrorEntity error = new SystemErrorEntity();
        try{
            Optional<SystemErrorEntity> errorDetail = systemErrorRepository.findByErrorCode(errorCode);
            if(errorDetail.isPresent()){
               error = errorDetail.get();
            }else{
                error.setErrorCode(ConstantsPool.error_code_unknown);
                error.setErrorStatus(ConstantsPool.unknown);
                error.setErrorDescription(ConstantsPool.unknown);
            }
        }catch (Exception e){
            logger.error("Exception in getSystemErrorDetailsByErrorCode() :",e);
        }
        return error;
    }
}
