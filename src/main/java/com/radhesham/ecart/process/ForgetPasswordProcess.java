package com.radhesham.ecart.process;

import com.radhesham.ecart.bean.SendEmailBean;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.ForgetPasswordEntity;
import com.radhesham.ecart.entity.SystemParameterEntity;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.SystemParameterRepository;
import com.radhesham.ecart.repository.UserRepository;
import com.radhesham.ecart.service.ForgetPasswordService;
import com.radhesham.ecart.service.SendEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ForgetPasswordProcess implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ForgetPasswordProcess.class);
    private final ForgetPasswordService forgetPasswordService;
    private final SendEmailService sendEmailService;
    private final UserRepository userService;
    private final SystemParameterRepository parameterRepository;
    @Value("${domain}")
    private String domain;

    public ForgetPasswordProcess(UserRepository userService, ForgetPasswordService forgetPasswordService, SendEmailService sendEmailService, SystemParameterRepository parameterRepository) {
        this.forgetPasswordService = forgetPasswordService;
        this.sendEmailService = sendEmailService;
        this.parameterRepository = parameterRepository;
        this.userService = userService;
    }

    @Override
    public void run() {
        try {
            logger.info("Entry in ForgetPasswordProcess()");
            while (isServiceInjected()) {
                List<ForgetPasswordEntity> forgetPasswordDetails = forgetPasswordService.getForgetPasswordList();
                if (CommonValidator.isEmpty(forgetPasswordDetails)) {
                    int totalSendMail = sendForgetPasswordLink(forgetPasswordDetails);
                    logger.info("ForgetPasswordProcess():forgetPasswordDetails total main send {}", totalSendMail);
                } else {
                    logger.info("ForgetPasswordProcess():forgetPasswordDetails not found");
                    Thread.sleep(1000L);
                }
            }
            logger.info("Exit from ForgetPasswordProcess()");
        } catch (Exception e) {
            logger.error("Exception in ForgetPasswordProcess::run()", e);
        }
    }

    private boolean isServiceInjected() {
        return userService != null && forgetPasswordService != null && sendEmailService != null && parameterRepository != null;
    }

    private int sendForgetPasswordLink(List<ForgetPasswordEntity> forgetPasswordEntityList) {
        int total = 0;
        try {
            logger.info("Entry in ForgetPasswordProcess::sendForgetPasswordLink()");
            SystemParameterEntity forgetPasswordLnk = parameterRepository.findByKey("reset_password_url");
            SystemParameterEntity forgetPasswordEmailTemplate = parameterRepository.findByKey("email_reset_password");
            if (Objects.isNull(forgetPasswordLnk) && Objects.isNull(forgetPasswordEmailTemplate)) {
                logger.info("ForgetPasswordProcess::sendForgetPasswordLink() parameter is getting null");
                return total;
            }

            String url = forgetPasswordLnk.getValue().trim();
            url = url.replace("{domain}", domain);
            String emailTemplate = forgetPasswordEmailTemplate.getValue();
            for (ForgetPasswordEntity forgetPasswordEntity : forgetPasswordEntityList) {
                Optional<UserEntity> user = userService.findById(forgetPasswordEntity.getUserId());
                if (user.isPresent()) {
                    UserEntity usr = user.get();
                    String withTokenUrl = url.replace("{token}", forgetPasswordEntity.getToken());
                    emailTemplate = emailTemplate.replace("{{CUSTOMER_NAME}}", usr.getName());
                    emailTemplate = emailTemplate.replace("{{RESET_LINK}}", withTokenUrl);
                    emailTemplate = emailTemplate.replace("{{EXPIRY_HOURS}}", "24");
                    emailTemplate = emailTemplate.replace("{{SUPPORT_URL}}", "");

                    SendEmailBean sendEmailBean = new SendEmailBean();
                    sendEmailBean.setSubject(usr.getName() + " forget password details " + (new Date()));
                    sendEmailBean.setToEmail(Collections.singletonList(usr.getEmail()));
                    sendEmailBean.setBody(emailTemplate);
                    int success = sendEmailService.saveEmailDetails(sendEmailBean);
                    if (success <= 0) {
                        logger.warn("ForgetPasswordProcess::sendForgetPasswordLink() email not saved {}", sendEmailBean);
                    }
                    forgetPasswordEntity.setStatus(ConstantsPool.STATUS_PROCESSED);
                    int updatedCount = forgetPasswordService.updateForgetPasswordDetails(forgetPasswordEntity);
                    logger.info("ForgetPasswordProcess::sendForgetPasswordLink() {}", updatedCount);

                }

            }
            logger.info("Exit from ForgetPasswordProcess::sendForgetPasswordLink()");
        } catch (Exception e) {
            logger.error("ForgetPasswordProcess::sendForgetPasswordLink() ", e);
        }
        return total;
    }
}
