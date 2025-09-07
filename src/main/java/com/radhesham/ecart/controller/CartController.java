package com.radhesham.ecart.controller;

import com.radhesham.ecart.bean.CartItemBean;
import com.radhesham.ecart.bean.ResponseStatus;
import com.radhesham.ecart.model.ManageCart;
import com.radhesham.ecart.response.CartResponse;
import com.radhesham.ecart.service.CartService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cart")
public class CartController {

    private static Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    CartController(CartService cartService,ProductService productService,UserService userService){
        this.cartService=cartService;
        this.productService=productService;
        this.userService=userService;
    }

    @PostMapping(value = "/items",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CartResponse> cartDetails(){
        CartResponse response=null;
        try{
            ManageCart manageCart = new ManageCart();
            injectService(manageCart);
            response=manageCart.manageCartDetails();
        }catch (Exception e){
            logger.error("Exception in cartItemDetails():",e.toString());
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(value = "/items/update",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CartResponse> cartItemDetailsUpdateRemove(@RequestBody CartItemBean cartItem){
        CartResponse response=null;
        try{
            logger.info("Entry in cartItemDetails()");
            ManageCart manageCart = new ManageCart();
            injectService(manageCart);
            response = manageCart.manageCart(cartItem);
            logger.info("Exit from cartItemDetails()");
        }catch (Exception e){
            logger.error("Exception in cartItemDetailsAdd():",e.toString());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/remove",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CartResponse> cartItemRemove(@RequestBody CartItemBean cartItem){
        CartResponse cartResponse=null;
        try{
            logger.info("Entry in cartItemRemove()");
            ManageCart manageCart = new ManageCart();
            injectService(manageCart);
            cartResponse=manageCart.manageCart(cartItem);
            logger.info("Exit from cartItemRemove()");
        }catch (Exception e){
            logger.error("Exception in cartItemRemove():",e);
        }
        return new ResponseEntity<>(cartResponse,HttpStatus.OK);
    }

    private void injectService(ManageCart manageCart){
        try{
            logger.info("Entry in injectService()");
            manageCart.setCartService(cartService);
            manageCart.setProductService(productService);
            manageCart.setUserService(userService);
            logger.info("Exit from injectService()");
        }catch (Exception e){
            logger.error("Exception in injectService():",e);
        }
    }
}
