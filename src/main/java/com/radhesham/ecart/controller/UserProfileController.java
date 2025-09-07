package com.radhesham.ecart.controller;


import com.radhesham.ecart.model.ManageProfile;
import com.radhesham.ecart.response.UserProfileResponse;
import com.radhesham.ecart.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    private final UserService userService;

    public UserProfileController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(value = "/profile" ,produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserProfileResponse> getUserProfileDetails(){
        UserProfileResponse response =null;
        try{
            logger.info("Entry in getUserProfileDetails()");
            ManageProfile manageProfile = new ManageProfile();
            manageProfile.setUserService(userService);
            response = manageProfile.manageUserProfileResponse();
            logger.info("Exit from getUserProfileDetails()");
        }catch (Exception e){
            logger.error("Exception in getUserProfileDetails() ",e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
