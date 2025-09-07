package com.radhesham.ecart.process;

import com.radhesham.ecart.bean.SendEmailBean;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.ProductEntity;
import com.radhesham.ecart.entity.ProductNotificationEntity;
import com.radhesham.ecart.entity.SystemParameterEntity;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.ProductNotificationRepository;
import com.radhesham.ecart.repository.ProductsRepository;
import com.radhesham.ecart.repository.SystemParameterRepository;
import com.radhesham.ecart.repository.UserRepository;
import com.radhesham.ecart.service.SendEmailService;
import com.radhesham.ecart.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ProductNotifyProcess implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ProductNotifyProcess.class);
    private final ProductsRepository productService;
    private final ProductNotificationRepository productNotificationRepository;
    private final UserRepository userService;
    private final SystemParameterRepository systemParameterRepository;
    private final SendEmailService sendEmailService;

    public ProductNotifyProcess(ProductsRepository productService, ProductNotificationRepository productNotificationRepository, UserRepository userService,SystemParameterRepository systemParameterRepository,SendEmailService sendEmailService) {
        this.productService = productService;
        this.productNotificationRepository = productNotificationRepository;
        this.userService = userService;
        this.systemParameterRepository=systemParameterRepository;
        this.sendEmailService=sendEmailService;
    }

    @Override
    public void run() {
        try {
            logger.info("Entry in ProductNotifyProcess::run()");
            if (!isServicesInjected()) {
                logger.warn("ProductNotifyProcess::run() services not injected");
            }
            while (isServicesInjected()) {
                List<ProductNotificationEntity> productNotifications = productNotificationRepository.findByStatus(ConstantsPool.STATUS_PENDING);
                if (!CommonValidator.isEmpty(productNotifications)) {
                    int totalEmailSend = sendProductNotificationByUser(productNotifications);
                    logger.info("ProductNotifyProcess::run() total email notfication sent {}", totalEmailSend);
                } else {
                    logger.info("ProductNotifyProcess::run() product notification not found");
                    Thread.sleep(1000L);
                }
            }
            logger.info("Exit from ProductNotifyProcess::run()");
        } catch (Exception e) {
            logger.error("Exception in ProductNotifyProcess::run()", e);
        }
    }

    private boolean isServicesInjected() {
        return productService != null && productNotificationRepository != null;
    }

    private int sendProductNotificationByUser(List<ProductNotificationEntity> productNotifications) {
        int total = 0;
        try {
            logger.info("Entry in sendProductNotificationByUser()");
            SystemParameterEntity productNotificationEmailTemplate = systemParameterRepository.findByKey("email_notify_item");
            String emailTemplate = productNotificationEmailTemplate.getValue();
            for (ProductNotificationEntity productNotificationEntity : productNotifications) {
                long productId = productNotificationEntity.getProductId();
                int userId = productNotificationEntity.getUserId();
                Optional<UserEntity> user = userService.findById(userId);
                if(user.isPresent()){
                    emailTemplate = emailTemplate.replace("{{CUSTOMER_NAME}}",user.get().getName());
                    Optional<ProductEntity> product = productService.findById(productId);
                    if(product.isPresent()){
                        emailTemplate = emailTemplate.replace("{{PRODUCT_IMAGE}}",product.get().getImageUrl());
                        emailTemplate =emailTemplate.replace("{{PRODUCT_NAME}}",product.get().getName());
                        emailTemplate = emailTemplate.replace("{{PRODUCT_DESC}}",product.get().getDescription());
                        emailTemplate= emailTemplate.replace("{{PRODUCT_PRICE}}",String.valueOf(product.get().getUnitPrice()));
                        emailTemplate = emailTemplate.replace("{{PRODUCT_URL}}","");
                        emailTemplate=emailTemplate.replace("{SUPPORT_URL}}","");
                    }
                    SendEmailBean sendEmailBean = new SendEmailBean();
                    sendEmailBean.setSubject(product.get().getName()+" is back in stock");
                    sendEmailBean.setBody(emailTemplate);
                    sendEmailBean.setToEmail(Collections.singletonList(user.get().getEmail()));
                    sendEmailBean.setRemark("product notification");
                    if(sendEmailService.saveEmailDetails(sendEmailBean)<=0){
                        logger.info("sendProductNotificationByUser() product notification details not saved");
                    }
                }
            }
            logger.info("Exit from sendProductNotificationByUser()");
        } catch (Exception e) {
            logger.error("Exception in sendProductNotificationByUser()", e);
        }
        return total;
    }
}
