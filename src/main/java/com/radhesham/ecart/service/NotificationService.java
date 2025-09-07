package com.radhesham.ecart.service;

import com.radhesham.ecart.bean.SendEmailBean;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.*;
import com.radhesham.ecart.repository.*;
import com.radhesham.ecart.request.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    ProductNotificationRepository productNotificationRepository;
    @Autowired
    SendEmailService emailService;
    @Autowired
    SystemParameterRepository systemParameterRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    UserRepository userRepository;

    public boolean isNotificationExists(int userId, int productId) {
        boolean res = false;
        try {
            logger.info("Entry in isNotificationExists()");
            logger.info("userId {} and productId {}", userId, productId);
            ProductNotificationEntity productNotificationEntity = new ProductNotificationEntity();
            productNotificationEntity.setProductId(productId);
            productNotificationEntity.setUserId(userId);
            productNotificationEntity.setStatus(ConstantsPool.STATUS_PENDING);
            Example example = Example.of(productNotificationEntity);
            Optional<ProductNotificationEntity> notification = productNotificationRepository.findOne(example);
            if (notification.isPresent()) {
                res = true;
            }
            logger.info("Exit from isNotificationExists()");
        } catch (Exception e) {
            res = false;
            logger.error("Exception in isNotificationExists():", e);
        }
        return res;
    }

    public int saveNotification(NotificationRequest notificationRequest) {
        int total = 0;
        try {
            logger.info("Entry in saveNotification()");
            ProductNotificationEntity productNotificationEntity = new ProductNotificationEntity();
            productNotificationEntity.setProductId(notificationRequest.getProductId());
            productNotificationEntity.setUserId(notificationRequest.getUserId());
            productNotificationEntity.setStatus(ConstantsPool.STATUS_PENDING);
            ProductNotificationEntity saveNotification = productNotificationRepository.save(productNotificationEntity);
            if (Objects.nonNull(saveNotification)) {
                total++;
            }
            logger.info("Exit from saveNotification()");
        } catch (Exception e) {
            logger.error("Exception in saveNotification():", e);
        }
        return total;
    }

    public int sendResetPasswordEmail(UserEntity userEntity) {
        int total = 0;
        try {
            logger.info("Entry in sendResetPasswordEmail()");
            SystemParameterEntity emailTemplate = systemParameterRepository.findByKey("reset_password_success");
            String emailHtmlTemplate = emailTemplate.getValue();
            if (!Objects.isNull(emailTemplate)) {
                emailHtmlTemplate = emailHtmlTemplate.replace("YourStore", "Original Trombones");
                emailHtmlTemplate = emailHtmlTemplate.replace("{{CUSTOMER_NAME}}", userEntity.getName());
                emailHtmlTemplate = emailHtmlTemplate.replace("{{RESET_URL}}", "");
                emailHtmlTemplate = emailHtmlTemplate.replace("{{SUPPORT_URL}}", "#");

                SendEmailBean sendEmailBean = new SendEmailBean();
                sendEmailBean.setSubject("Password Reset Successfully - " + userEntity.getName());
                sendEmailBean.setToEmail(Collections.singletonList(userEntity.getEmail()));
                sendEmailBean.setBody(emailHtmlTemplate);
                sendEmailBean.setRemark("Password reset successfully");

                if (emailService.saveEmailDetails(sendEmailBean) <= 0) {
                    logger.warn("sendResetPasswordEmail() password reset email notification not saved");
                } else {
                    total++;
                }
            }
            logger.info("Exit from sendResetPasswordEmail()");
        } catch (Exception e) {
            logger.error("Exception in sendResetPasswordEmail()", e);
        }
        return total;
    }

    public int sendOrderDetails(OrderEntity order) {
        int total = 0;
        try {
            logger.info("Entry in sendOrderDetails()");
            SystemParameterEntity orderTemplate = systemParameterRepository.findByKey("email_order_details");
            SystemParameterEntity orderItemTemplate = systemParameterRepository.findByKey("email_order_items");
            if (Objects.nonNull(orderTemplate) && Objects.nonNull(orderItemTemplate)) {
                String orderHtmlTemplate = orderTemplate.getValue();
                String orderItemHtmlTemplate = orderItemTemplate.getValue();
                UserEntity userEntity = userRepository.findById(order.getUser().getId()).orElseGet(null);

                orderHtmlTemplate = orderHtmlTemplate.replace("{{CUSTOMER_NAME}}", userEntity.getName());
                orderHtmlTemplate = orderHtmlTemplate.replace("{{ORDER_STATUS}}", order.getOrderStatus());
                orderHtmlTemplate = orderHtmlTemplate.replace("{{ORDER_ID}}", order.getOrderTrackingNum());
                orderHtmlTemplate = orderHtmlTemplate.replace("{{ORDER_DATE}}", order.getRazorPayPaymentId());
                orderHtmlTemplate = orderHtmlTemplate.replace("{{GRAND_TOTAL}}", String.valueOf(order.getTotalPrice()));
                orderHtmlTemplate = orderHtmlTemplate.replace("{{YEAR}}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

                //address details
                Optional<AddressEntity> address = addressRepository.findById(order.getAddressEntity().getId());
                if (address.isPresent()) {
                    orderHtmlTemplate = orderHtmlTemplate.replace("{{SHIP_NAME}}", userEntity.getName());
                    orderHtmlTemplate = orderHtmlTemplate.replace("{{SHIP_LINE1}}", address.get().getStreet() + "," + address.get().getHouseNum());
                    orderHtmlTemplate = orderHtmlTemplate.replace("{{SHIP_CITY}}", address.get().getCity());
                    orderHtmlTemplate = orderHtmlTemplate.replace("{{SHIP_STATE}}", address.get().getState() + "," + address.get().getCountry());
                    orderHtmlTemplate = orderHtmlTemplate.replace("{{SHIP_PIN}}", address.get().getZipCode());
                    orderHtmlTemplate = orderHtmlTemplate.replace("{{SHIP_PHONE}}", userEntity.getPhone());
                }

                //item
                List<OrderItemEntity> orderItems = orderItemRepository.findByOrderOrderId(order.getOrderId());
                if (!CommonValidator.isEmpty(orderItems)) {
                    StringBuilder itemsDetails = new StringBuilder("");
                    for(OrderItemEntity orderItem : orderItems){
                        Optional<ProductEntity> product = productsRepository.findById(orderItem.getProduct().getProductId());
                        if(product.isPresent()){
                            String tempItem = orderItemHtmlTemplate.replace("{{product_ulr}}",product.get().getImageUrl());
                            tempItem=tempItem.replace("{{product_name}}",product.get().getName());
                            tempItem = tempItem.replace("{{product_desc}}",product.get().getDescription());
                            tempItem=tempItem.replace("{qty}",String.valueOf(orderItem.getQuantity()));
                            tempItem=tempItem.replace("{{price}}",String.valueOf(orderItem.getUnitPrice()));
                            itemsDetails.append(tempItem);
                        }
                    }
                    orderHtmlTemplate = orderHtmlTemplate.replace(" {{items}}",itemsDetails.toString());
                    SendEmailBean sendEmailBean=new SendEmailBean();
                    sendEmailBean.setSubject("Order "+order.getOrderId()+" details "+Calendar.getInstance().toString());
                    sendEmailBean.setBody(orderHtmlTemplate);
                    sendEmailBean.setToEmail(Collections.singletonList(userEntity.getEmail()));
                    sendEmailBean.setRemark("Order confirmation details");
                    if(emailService.saveEmailDetails(sendEmailBean)<=0){
                        logger.warn("sendOrderDetails() order email details not saved");
                    }else total++;
                }
            }
            logger.info("Exit from sendOrderDetails()");
        } catch (Exception e) {
            logger.error("Exception in sendOrderDetails()", e);
        }
        return total;
    }
}
