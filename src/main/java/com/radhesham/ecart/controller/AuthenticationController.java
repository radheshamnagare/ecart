package com.radhesham.ecart.controller;

import com.radhesham.ecart.model.ManageUserAuth;
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
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ForgetPasswordService forgetPasswordService;

    @Autowired
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserService userService,ForgetPasswordService forgetPasswordService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService=userService;
        this.forgetPasswordService=forgetPasswordService;
    }

    @PostMapping(value = "/signup",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DefaultResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
       DefaultResponse apiResponse = null;
       try{
           logger.info("Entry in registerUser()");
           ManageUserAuth manageUserAuth = new ManageUserAuth();
           injectServices(manageUserAuth);
           apiResponse = manageUserAuth.manageUserSignUp(registerUserRequest);
           logger.info("Exit from registerUser()");
       }catch (Exception e){
            logger.error("Exception in registerUser(): ",e);
       }
       return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/signin",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserRequest loginUserRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try{
            ManageUserAuth manageUserAuth = new ManageUserAuth();
            injectServices(manageUserAuth);
            loginResponse = manageUserAuth.manageLogin(loginUserRequest);
        }catch (Exception e){
            logger.error("Exception in authenticate() ",e);
        }
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(value = "/forget/password",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DefaultResponse> forgetUserPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        DefaultResponse response=null;
        try{
            logger.info("Entry in forgetUserPassword()");
            ManageUserAuth manageUserAuth = new ManageUserAuth();
            injectServices(manageUserAuth);
            response = manageUserAuth.manageForgetPassword(forgetPasswordRequest);
            logger.info("Exit from forgetUserPassword()");
        }catch (Exception e){
            logger.error("Exception in forgetUserPassword(): ",e);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(value = "/reset/password",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DefaultResponse> resetUserPassword(@RequestBody ResetUserPasswordRequest resetUserPasswordRequest){
        DefaultResponse response=null;
        try{
            logger.info("Entry in resetUserPassword()");
            ManageUserAuth manageUserAuth = new ManageUserAuth();
            injectServices(manageUserAuth);
            response = manageUserAuth.manageResetPassword(resetUserPasswordRequest);
            logger.info("Exit from resetUserPassword()");
        }catch (Exception e){
            logger.error("Exception in resetUserPassword():",e);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void injectServices(ManageUserAuth manageUserAuth){
        try{
            manageUserAuth.setAuthenticationService(authenticationService);
            manageUserAuth.setJwtService(jwtService);
            manageUserAuth.setUserService(userService);
            manageUserAuth.setForgetPasswordService(forgetPasswordService);
        }catch (Exception e){
            logger.error("Exception in injectServices() ",e);
        }
    }
}
