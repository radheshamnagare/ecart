package com.radhesham.ecart.controller;

import com.radhesham.ecart.model.ManageProfile;
import com.radhesham.ecart.request.AddressAddRequest;
import com.radhesham.ecart.response.AddressDetailsResponse;
import com.radhesham.ecart.response.DefaultResponse;
import com.radhesham.ecart.service.AddressService;
import com.radhesham.ecart.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
private static final Logger logger= LoggerFactory.getLogger(AddressController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    @PostMapping(value="/lookup",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AddressDetailsResponse> getAddressDetails(){
        AddressDetailsResponse response =null;
        try{
            logger.info("Entry in getAddressDetails()");
            ManageProfile manageProfile = new ManageProfile();
            manageProfile.setUserService(userService);
            manageProfile.setAddressService(addressService);
            response=manageProfile.manageAddressDetails();
            logger.info("Exit from getAddressDetails()");
        }catch (Exception e){
            logger.error("Exception in getAddressDetails():",e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/update",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DefaultResponse> addAddress(@RequestBody AddressAddRequest addressAddRequest){
        DefaultResponse response=null;
        try{
            logger.info("Entry in addAddress()");
            ManageProfile manageProfile = new ManageProfile();
            manageProfile.setUserService(userService);
            manageProfile.setAddressService(addressService);
            response = manageProfile.manageSaveAddress(addressAddRequest);
            logger.info("Exit from addAddress()");
        }catch (Exception e){
              logger.error("Exception in addAddress():",e);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
