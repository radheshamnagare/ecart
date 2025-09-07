package com.radhesham.ecart.model;

import com.radhesham.ecart.bean.ProductDetail;
import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.request.ProductFilterRequest;
import com.radhesham.ecart.response.ProductDetailsResponse;
import com.radhesham.ecart.service.ProductService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Data
public class ManageProduct {
    private static final Logger logger = LoggerFactory.getLogger(ManageProduct.class);
    private ProductService productService;

    private ProductDetailsResponse getProductDetailsResponse(SystemError error, List<ProductDetail> productDetails){
        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();
        try{
            productDetailsResponse.setErrorCode(error.getErrorCode());
            productDetailsResponse.setErrorStatus(error.getErrorStatus());
            productDetailsResponse.setErrorDescription(error.getErrorDescription());
            productDetailsResponse.setProductDetails(productDetails);
        }catch (Exception e){
            logger.error("Exception in getProductDetailsResponse():",e);
        }
        return productDetailsResponse;
    }

    public ProductDetailsResponse manageProductDetails(ProductFilterRequest productFilterRequest){
        ProductDetailsResponse productDetailsResponse =null;
        SystemError error= CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try{
            List<ProductDetail> productDetails = productService.getAllProductsByFilter(productFilterRequest);
            productDetailsResponse = getProductDetailsResponse(error,productDetails);
        }catch (Exception e){
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            productDetailsResponse = getProductDetailsResponse(error,new ArrayList<>());
            logger.error("Exception in manageProductDetails():",e);
        }
        return productDetailsResponse;
    }
}
