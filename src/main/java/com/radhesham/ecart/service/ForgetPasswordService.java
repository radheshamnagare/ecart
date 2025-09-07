package com.radhesham.ecart.service;

import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.ForgetPasswordEntity;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.ForgetPasswordRepository;
import com.radhesham.ecart.repository.UserRepository;
import com.radhesham.ecart.request.ResetUserPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class ForgetPasswordService {
  private static final Logger logger= LoggerFactory.getLogger(ForgetPasswordService.class);

  @Autowired
  ForgetPasswordRepository forgetPasswordRepository;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  UserRepository userRepository;
  @Autowired
  NotificationService notificationService;

    public int updateForgetPasswordDetails(ForgetPasswordEntity forgetPasswordEntity){
        int total=0;
        try{
            logger.info("Entry in updateForgetPasswordDetails()");
             ForgetPasswordEntity updatedForgetPassword =   forgetPasswordRepository.save(forgetPasswordEntity);
             if(updatedForgetPassword.getId()>0)
                 total++;
             logger.info("Exit from updateForgetPasswordDetails()");
        }catch (Exception e){
            logger.error("Exception in updateForgetPasswordDetails()",e);
        }
        return total;
    }

    public int saveForgetPasswordRequest(int userId){
       int total=0;
       try{
           logger.info("Entry in saveForgetPasswordRequest()");
           ForgetPasswordEntity forgetPasswordEntity = new ForgetPasswordEntity();
           forgetPasswordEntity.setUserId(userId);
           forgetPasswordEntity.setToken(CommonService.getUniqueId());
           forgetPasswordEntity.setStatus(ConstantsPool.STATUS_PENDING);

           ForgetPasswordEntity savedForgetPasswordEntity = forgetPasswordRepository.save(forgetPasswordEntity);
           if(Objects.nonNull(savedForgetPasswordEntity))
               total++;
           logger.info("Exit from saveForgetPasswordRequest()");
       }catch (Exception e){
           logger.error("Exception in saveForgetPasswordRequest():",e);
       }
       return total;
    }

    public int findUserIdByToken(String token){
        int uniqueUserId=0;
        try{
            logger.info("Entry in findUserIdByToken()");
            ForgetPasswordEntity forgetPasswordEntity = forgetPasswordRepository.findByToken(token);
            if(Objects.nonNull(token) && forgetPasswordEntity.getUserId()>0){
                uniqueUserId = forgetPasswordEntity.getUserId();
            }
            logger.info("Entry in findUserIdByToken()");
        }catch (Exception e){
            logger.error("Exception in findUserIdByToken():",e);
        }
        return uniqueUserId;
    }

    public boolean isSameOldPasswordAndNewPassword(String newPassword,int userId){
        boolean res=false;
        try{
            logger.info("Entry in isSameOldPasswordAndNewPassword()");
            Optional<UserEntity> user = userRepository.findById(userId);
            if(user.isPresent()){
                if(user.get().getPassword().equals(passwordEncoder.encode(newPassword))){
                    res=true;
                }
            }
            logger.info("Exit from isSameOldPasswordAndNewPassword()");
        }catch (Exception e){
            logger.error("Exception in isSameOldPasswordAndNewPassword():",e);
        }
        return res;
    }

    public int changeUserPassword(ResetUserPasswordRequest resetUserPasswordRequest){
        int total=0;
        try{
            logger.info("Entry in changeUserPassword()");
            if(Objects.nonNull(resetUserPasswordRequest)){
                Optional<UserEntity> user = userRepository.findById(resetUserPasswordRequest.getUserId());
                if(user.isPresent()){
                    UserEntity updateUserEntity = user.get();
                    updateUserEntity.setPassword(passwordEncoder.encode(resetUserPasswordRequest.getConfirmPassword()));
                    updateUserEntity = userRepository.save(updateUserEntity);
                    if(updateUserEntity.getPassword().length()>0){
                        total+=1;
                        notificationService.sendResetPasswordEmail(updateUserEntity);
                    }
                }
            }
            logger.info("Exit from changeUserPassword()");
        }catch (Exception e){
            logger.error("Exception in changeUserPassword():",e);
        }
        return total;
    }

    public List<ForgetPasswordEntity> getForgetPasswordList(){
        return forgetPasswordRepository.findByStatus(ConstantsPool.STATUS_PENDING);
    }
}

