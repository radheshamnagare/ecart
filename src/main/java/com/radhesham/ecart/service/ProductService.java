package com.radhesham.ecart.service;

import com.radhesham.ecart.bean.ProductDetail;
import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.common.ErrorConstants;
import com.radhesham.ecart.entity.ProductEntity;
import com.radhesham.ecart.repository.ProductsRepository;
import com.radhesham.ecart.request.ProductFilterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductsRepository productsRepository;

    public List<ProductDetail> getAllProductsByFilter(ProductFilterRequest productFilterRequest) {
        List<ProductDetail> productDetails = new ArrayList<>();
        List<ProductEntity> products=null;
        try {
            if(Objects.nonNull(productDetails) && !CommonValidator.isEmpty(productFilterRequest.getNameTitleDesc())){
                products = productsRepository.searchByKeyword(productFilterRequest.getNameTitleDesc());
            }else{
                products = productsRepository.findAll();
            }

            if(!CommonValidator.isEmpty(products)){
                products.forEach(product->{
                    ProductDetail prod = new ProductDetail();
                    BeanUtils.copyProperties(product,prod);
                    if(Objects.nonNull(prod))
                    productDetails.add(prod);
                });
            }
        } catch (Exception e) {
            logger.error("Exception in getAllProductsByFilter():", e);
        }
        return productDetails;
    }

    public SystemError isProductExistById(long productId){
        SystemError error= CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try{
            Optional<ProductEntity> product = productsRepository.findById(productId);
            if(!product.isPresent()){
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid, ErrorConstants.DESC_PRODUCT,String.valueOf(productId));
            }
        }catch (Exception e){
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            logger.error("Exception in isProductExistById():",e);
        }
      return error;
    }
}
