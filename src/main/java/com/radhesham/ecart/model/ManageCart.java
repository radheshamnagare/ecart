package com.radhesham.ecart.model;

import com.radhesham.ecart.bean.CartItemBean;
import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.common.ErrorConstants;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.response.CartResponse;
import com.radhesham.ecart.service.CartService;
import com.radhesham.ecart.service.ProductService;
import com.radhesham.ecart.service.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ManageCart {

    private static final Logger logger = LoggerFactory.getLogger(ManageCart.class);
    private CartService cartService;
    private ProductService productService;
    private UserService userService;

    private double calculateTotalPrice(List<CartItemBean> cartItems) {
        double totalPrice = 0;
        try {
            for (CartItemBean i : cartItems) {
                totalPrice += (i.getUnitPrice() * i.getQuantity());
            }
        } catch (Exception e) {
            logger.error("Exception in calculateTotalPrice()", e);
        }
        return totalPrice;
    }

    private CartResponse getCartResponse(List<CartItemBean> cartItem, SystemError error) {
        CartResponse cartResponse = new CartResponse();
        try {
            logger.info("Entry in getCartResponse()");
            cartResponse.setCartItem(cartItem);
            double totalPrice = calculateTotalPrice(cartItem);
            cartResponse.setTotalAmount(totalPrice);
            cartResponse.setTotalItems(cartItem.size());
            cartResponse.setErrorCode(error.getErrorCode());
            cartResponse.setErrorStatus(error.getErrorStatus());
            cartResponse.setErrorDescription(error.getErrorDescription());
            logger.info("Exit from getCartResponse()");
        } catch (Exception e) {
            logger.error("Exception in getCartResponse():", e.toString());
        }
        return cartResponse;
    }

    private SystemError isValidCartItem(CartItemBean cartItemBean) {
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            logger.info("Entry in isValidCartItem()");
            if (Objects.nonNull(cartItemBean)) {
                if (cartItemBean.getAction().equalsIgnoreCase(ConstantsPool.ACTION_DELETE)) {
                    if (!cartService.isValidCartItemId(cartItemBean.getId())) {
                        CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_ID, "");
                        return error;
                    }
                }

                error = productService.isProductExistById(cartItemBean.getProductId());
                if (!error.getErrorCode().equals(ConstantsPool.error_code_success)) {
                    return error;
                }

                if (CommonValidator.isEmpty(cartItemBean.getProductTitle())) {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_PRODUCT_TITLE, "");
                    return error;
                }

                if (CommonValidator.isEmpty(cartItemBean.getProductName())) {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_PRODUCT_NAME, "");
                    return error;
                }

                if (CommonValidator.isEmpty(cartItemBean.getProductImage())) {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_PRODUCT_IMAGE, "");
                    return error;
                }

                if (CommonValidator.isEmpty(cartItemBean.getDescription())) {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_DESCRIPTION, "");
                    return error;
                }


                if (cartItemBean.getUnitPrice() <= 0) {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_UNIT_PRICE, "");
                    return error;
                }

                if (cartItemBean.getQuantity() <= 0) {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_QUANTITY, "");
                    return error;
                }

            } else {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_BEAN, "");
            }
            logger.info("Exit from isValidCartItem()");
        } catch (Exception e) {
            logger.error("Exception in isValidCartItem()");
        }
        return error;
    }

    public CartResponse manageCart(CartItemBean cartItem) {
        CartResponse cartResponse = null;
        List<CartItemBean> cartItems = new ArrayList<>();
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            logger.info("Entry in manageCart()");
            UserEntity userInfo = CommonService.currentUserSession();
            if (Objects.nonNull(userInfo)) {
                if (userInfo.getId() > 0) {
                    cartItem.setUserId(userInfo.getId());
                    if (cartItem.getAction().equalsIgnoreCase(ConstantsPool.ACTION_DELETE) || cartItem.getAction().equalsIgnoreCase(ConstantsPool.ACTION_DELETE_ALL)) {
                        error = isValidCartItem(cartItem);
                        if (error.getErrorCode().equals(ConstantsPool.error_code_success))
                            cartItems = cartService.removeItems(cartItem);
                        else
                            CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_CART_ITEM, "");
                    } else if (cartItem.getAction().equalsIgnoreCase(ConstantsPool.ACTION_ADD)) {
                        error = isValidCartItem(cartItem);
                        if (error.getErrorCode().equals(ConstantsPool.error_code_success))
                            cartItems = cartService.saveItems(cartItem);
                        else
                            CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_CART_ITEM, "");
                    }
                } else {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_USER, "");
                }
                cartResponse = getCartResponse(cartItems, error);
            } else {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_USER, "");
            }
            logger.info("Exit from manageCart()");
        } catch (Exception e) {
            CommonService.setErrorMessage(error, ConstantsPool.error_code_failed, "", "");
            cartResponse = getCartResponse(cartItems, error);
            logger.error("Exception in manageCart():", e);
        }
        return cartResponse;
    }

    public CartResponse manageCartDetails() {
        CartResponse cartResponse = new CartResponse();
        List<CartItemBean> cartItems = new ArrayList<>();
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            logger.info("Entry in manageCartDetails()");
            UserEntity userInfo = CommonService.currentUserSession();
            if (Objects.nonNull(userInfo)) {
                if (userInfo.getId() > 0) {
                    cartItems = cartService.getAllCartItemByUser(userInfo.getId());
                } else {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_USER, "");
                }
                cartResponse = getCartResponse(cartItems, error);
            } else {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_USER, "");
            }
            logger.info("Exit from manageCartDetails()");
        } catch (Exception e) {
            CommonService.setErrorMessage(error, ConstantsPool.error_code_failed, "", "");
            cartResponse = getCartResponse(cartItems, error);
            logger.error("Exception in manageCartDetails():", e);
        }
        return cartResponse;
    }
}
