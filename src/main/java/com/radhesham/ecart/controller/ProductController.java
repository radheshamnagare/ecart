package com.radhesham.ecart.controller;

import com.radhesham.ecart.model.ManageProduct;
import com.radhesham.ecart.request.ProductFilterRequest;
import com.radhesham.ecart.response.ProductDetailsResponse;
import com.radhesham.ecart.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;

    @PostMapping(value = "/lookup",produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ProductDetailsResponse> findAllProductDetails(@RequestBody ProductFilterRequest productFilterRequest){
        ProductDetailsResponse productDetailsResponse = null;
        try{
            ManageProduct manageProduct =new ManageProduct();
            manageProduct.setProductService(productService);
            productDetailsResponse = manageProduct.manageProductDetails(productFilterRequest);
        }catch (Exception e){
            logger.error("Exception in findAllProductDetails():",e);
        }
        return new ResponseEntity<>(productDetailsResponse, HttpStatus.OK);
    }
}
