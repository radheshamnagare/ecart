package com.radhesham.ecart.controller;

import com.radhesham.ecart.model.ManageNotification;
import com.radhesham.ecart.request.NotificationRequest;
import com.radhesham.ecart.response.DefaultResponse;
import com.radhesham.ecart.service.NotificationService;
import com.radhesham.ecart.service.ProductService;
import com.radhesham.ecart.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
  private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public NotificationController(NotificationService notificationService,UserService userService,ProductService productService){
        this.notificationService =notificationService;
        this.userService =userService;
        this.productService=productService;
    }

    @PostMapping(value = "/notification/add" ,produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DefaultResponse> pushProductNotification(@RequestBody NotificationRequest notificationRequest){
        DefaultResponse response =null;
        try{
            logger.info("Entry in pushProductNotification()");
            ManageNotification manageNotification = new ManageNotification();
            injectServices(manageNotification);
            response = manageNotification.manageNotificationAdd(notificationRequest);
            logger.info("Exit from pushProductNotification()");
        }catch (Exception e){
            logger.error("Exception in pushProductNotification():",e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void injectServices(ManageNotification manageNotification){
        try{
            manageNotification.setNotificationService(notificationService);
            manageNotification.setProductService(productService);
            manageNotification.setUserService(userService);
        }catch (Exception e){
            logger.error("Exception in injectServices() ",e);
        }
    }
}
