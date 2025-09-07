package com.radhesham.ecart.model;

import com.radhesham.ecart.bean.CartItemBean;
import com.radhesham.ecart.bean.OrderDetails;
import com.radhesham.ecart.bean.OrderItemDetails;
import com.radhesham.ecart.bean.SystemError;
import com.radhesham.ecart.common.CommonService;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.common.ErrorConstants;
import com.radhesham.ecart.entity.UserEntity;
import com.radhesham.ecart.repository.ViewUserNameId;
import com.radhesham.ecart.request.OrderFilterRequest;
import com.radhesham.ecart.request.OrderItemFilterRequest;
import com.radhesham.ecart.request.PlaceOrderRequest;
import com.radhesham.ecart.response.OrderDetailsResponse;
import com.radhesham.ecart.response.OrderItemResponse;
import com.radhesham.ecart.response.PlaceOrderResponse;
import com.radhesham.ecart.service.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@Data
public class ManageOrder {
    private static final Logger logger = LoggerFactory.getLogger(ManageOrder.class);
    private UserService userService;
    private OrderService orderService;
    private AddressService addressService;
    private ProductService productService;
    private CartService cartService;
    private OrderPaymentService orderPaymentService;

    private OrderDetailsResponse getOrderDetailsResponse(List<OrderDetails> orderDetails, SystemError error) {
        OrderDetailsResponse response = new OrderDetailsResponse();
        try {
            logger.info("Entry in getOrderDetailsResponse()");
            response.setOrderDetails(orderDetails);
            response.setErrorCode(error.getErrorCode());
            response.setErrorStatus(response.getErrorStatus());
            response.setErrorDescription(response.getErrorDescription());
            logger.info("Exit from getOrderDetailsResponse()");
        } catch (Exception e) {
            logger.error("Exception in getOrderDetailsResponse():", e);
        }
        return response;
    }

    private PlaceOrderResponse getPlaceOrderResponse(OrderDetails orderDetails, SystemError error) {
        PlaceOrderResponse response = new PlaceOrderResponse();
        try {
            logger.info("Entry in getPlaceOrderResponse()");
            response.setOrderDetails(orderDetails);
            response.setErrorCode(error.getErrorCode());
            response.setErrorStatus(response.getErrorStatus());
            response.setErrorDescription(response.getErrorDescription());
            logger.info("Exit from getPlaceOrderResponse()");
        } catch (Exception e) {
            logger.error("Exception in getPlaceOrderResponse():", e);
        }
        return response;
    }


    private OrderItemResponse getOrderItemResponse(List<OrderItemDetails> orderItemDetails, SystemError error) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        try {
            logger.info("Entry in getOrderItemResponse()");
            orderItemResponse.setOrderItemDetails(orderItemDetails);
            orderItemResponse.setErrorCode(error.getErrorCode());
            orderItemResponse.setErrorStatus(error.getErrorStatus());
            orderItemResponse.setErrorDescription(error.getErrorDescription());
            logger.info("Exit from getOrderItemResponse()");
        } catch (Exception e) {
            logger.error("Entry in getOrderItemResponse():", e);
        }
        return orderItemResponse;
    }

    public OrderDetailsResponse manageOrderDetails(OrderFilterRequest orderFilterRequest) {
        OrderDetailsResponse response = null;
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        try {
            logger.info("Entry in manageOrderDetails()");
            UserEntity sessionUser = CommonService.currentUserSession();
            if (!Objects.isNull(sessionUser)) {
               if (Objects.nonNull(sessionUser)) {
                    orderFilterRequest.setUserId(sessionUser.getId());
                    orderDetailsList = orderService.getOrderDetails(orderFilterRequest);
                } else {
                    logger.info("manageOrderDetails() user details not found for sessionUser");
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "null");
                }
            } else {
                logger.info("manageOrderDetails() user details not found");
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "null");
            }
            response = getOrderDetailsResponse(orderDetailsList, error);
            logger.info("Exit from manageOrderDetails()");
        } catch (Exception e) {
            logger.error("Exception in manageOrderDetails():", e);
            CommonService.setErrorMessage(error, ConstantsPool.error_code_failed, "", "");
            response = getOrderDetailsResponse(orderDetailsList, error);
        }
        return response;
    }

    public OrderItemResponse manageOrderItems(OrderItemFilterRequest orderItemFilterRequest) {
        OrderItemResponse response = null;
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        List<OrderItemDetails> orderItemDetails = null;
        try {
            logger.info("Entry in manageOrderItems()");
            UserEntity sessionUser = CommonService.currentUserSession();
            if (!Objects.isNull(sessionUser)) {
                if (sessionUser.getId()>0) {
                    orderItemFilterRequest.setUserId(sessionUser.getId());
                    orderItemDetails = orderService.getOrderItemDetails(orderItemFilterRequest);
                } else {
                    logger.info("manageOrderItems() userDetails not found");
                    CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "null");
                    orderItemDetails = new ArrayList<>();
                }
            } else {
                logger.info("manageOrderItems() sessionUser details is null");
                CommonService.setErrorMessage(ConstantsPool.error_code_invalid, ErrorConstants.DESC_USER, "null");
                orderItemDetails = new ArrayList<>();
            }
            response = getOrderItemResponse(orderItemDetails, error);
            logger.info("Exit from manageOrderItems()");
        } catch (Exception e) {
            logger.error("Exception in manageOrderItems():", e);
            CommonService.setErrorMessage(error, ConstantsPool.error_code_failed, "", "");
        }
        return response;
    }

    private SystemError isValidStatus(String status) {
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            logger.info("Entry in isValidPaymentStatus()");
            if (CommonValidator.isEmpty(status)) {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_STATUS, status);
            } else if (!status.toLowerCase().equals(ConstantsPool.STATUS_CREATED) && status.toLowerCase().equals(ConstantsPool.STATUS_PENDING) && !status.toLowerCase().equals(ConstantsPool.STATUS_SUCCESS) && !status.toLowerCase().equals(ConstantsPool.STATUS_FAILED)) {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_STATUS, status);
            }
            logger.info("Exit from isValidPaymentStatus()");
        } catch (Exception e) {
            logger.error("Exception in isValidPaymentStatus():", e);
            CommonService.setErrorMessage(error, ConstantsPool.error_code_failed, "", "");
        }
        return error;
    }

    private SystemError isValidPlaceOrderRequest(PlaceOrderRequest placeOrderRequest, int userId) {
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success, "", "");
        try {
            logger.info("Entry in isValidPlaceOrderRequest()");

            if(CommonValidator.isEmpty(placeOrderRequest.getOrderTrackingNum())){
                CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_ORDER_TRACKING_NO, "");
                return error;
            }
            if (CommonValidator.isEmpty(placeOrderRequest.getRazorPayOrderId())) {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_required, ErrorConstants.DESC_REZORPAY_ORDERID, "");
                return error;
            }

            error = isValidStatus(placeOrderRequest.getOrderStatus());
            if (!error.getErrorCode().equals(ConstantsPool.error_code_success)) return error;

            if (placeOrderRequest.getTotalPrice() <= 0) {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_TOTAL_PRICE, placeOrderRequest.getTotalPrice().toString());
                return error;
            }

            if (placeOrderRequest.getTotalQuantity() <= 0) {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_TOTAL_QUANTITY, placeOrderRequest.getTotalQuantity().toString());
                return error;
            }

            if (!addressService.isAddressExists(placeOrderRequest.getAddressId(), userId)) {
                CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_ADDRESS, String.valueOf(placeOrderRequest.getAddressId()));
                return error;
            }

            for (OrderItemDetails orderItemDetails : placeOrderRequest.getOrderItemDetails()) {
                if (orderItemDetails.getProductId() <= 0) {
                    CommonService.setErrorMessage(error, ConstantsPool.error_code_invalid, ErrorConstants.DESC_PRODUCT, String.valueOf(orderItemDetails.getProductId()));
                    return error;
                }
                error = productService.isProductExistById(orderItemDetails.getProductId());
                if (!error.getErrorCode().equals(ConstantsPool.error_code_success)) return error;

                if(orderItemDetails.getQuantity()<=0){
                    CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_TOTAL_QUANTITY,String.valueOf(orderItemDetails.getQuantity()));
                    return error;
                }
                if(orderItemDetails.getUnitPrice()<=0){
                    CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_UNIT_PRICE,String.valueOf(orderItemDetails.getQuantity()));
                    return error;
                }
            }
            logger.info("Exit from isValidPlaceOrderRequest()");
        } catch (Exception e) {
            logger.error("Exception in isValidPlaceOrderRequest():", e);
        }
        return error;
    }

    private void setOrderInfo(PlaceOrderRequest placeOrderRequest,int userId){
        try{
            logger.info("Entry in setOrderInfo()");
            placeOrderRequest.setOrderTrackingNum(CommonService.getUniqueId());
            placeOrderRequest.setUserId(userId);
            setCartDetails(placeOrderRequest,userId);
            logger.info("Exit in setOrderInfo()");
        }catch (Exception e){
            logger.error("Exception in setOrderInfo()",e);
        }
    }

    private void setCartDetails(PlaceOrderRequest placeOrderRequest,int userId){
        try{
            List<OrderItemDetails> placeOrderDetails =new ArrayList<>();
            List<CartItemBean> cartItemBeanList = cartService.getAllCartItemByUser(userId);
            int totalItems=0;
            double totalPrice=0;
            for(CartItemBean item:cartItemBeanList) {
                OrderItemDetails orderItem = new OrderItemDetails();
                orderItem.setItemId(item.getId());
                BeanUtils.copyProperties(item, orderItem);
                placeOrderDetails.add(orderItem);
                totalItems+=1;
                totalPrice+=(orderItem.getUnitPrice())* orderItem.getQuantity();
            }
            placeOrderRequest.setTotalQuantity(totalItems);
            placeOrderRequest.setTotalPrice(totalPrice);
            placeOrderRequest.setOrderItemDetails(placeOrderDetails);
        }catch (Exception e){
            logger.error("Exception in setCartDetails()",e);
        }
    }
    public PlaceOrderResponse managePlaceOrder(PlaceOrderRequest placeOrderRequest) {
        PlaceOrderResponse response = null;
        OrderDetails orderPlaceDetail=null;
        SystemError error = CommonService.setErrorMessage(ConstantsPool.error_code_success,"","");
        try {
            logger.info("Entry in managePlaceOrder()");
            UserEntity sessionUser = CommonService.currentUserSession();
            if(Objects.nonNull(sessionUser)){
               if(Objects.nonNull(sessionUser)){
                    setOrderInfo(placeOrderRequest, sessionUser.getId());
                    orderPaymentService.createRezorpayOrder(placeOrderRequest);
                    error = isValidPlaceOrderRequest(placeOrderRequest, sessionUser.getId());
                    if(error.getErrorCode().equals(ConstantsPool.error_code_success)){
                        orderPlaceDetail = orderService. placeOrder(placeOrderRequest);
                        if(Objects.nonNull(orderPlaceDetail)){
                            orderPlaceDetail.setCustomerName(sessionUser.getName());
                            orderPlaceDetail.setEmail(sessionUser.getEmail());
                            orderPlaceDetail.setPhoneNo(sessionUser.getPhone());
                        }
                    }

                }else{
                    CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_USER,"null");
                }
            }else{
                CommonService.setErrorMessage(error,ConstantsPool.error_code_invalid,ErrorConstants.DESC_USER,"null");
            }
            response = getPlaceOrderResponse(orderPlaceDetail,error);
            logger.info("Exit from managePlaceOrder()");
        } catch (Exception e) {
            CommonService.setErrorMessage(error,ConstantsPool.error_code_failed,"","");
            response=getPlaceOrderResponse(orderPlaceDetail,error);
            logger.error("Exception in managePlaceOrder():", e);
        }
        return response;
    }

    public void paymentSuccess(String orderId,String paymentId){
        try{
           logger.info("Entry in paymentSuccess()");
           orderPaymentService.updatePaymentSuccess(orderId,paymentId);
           logger.info("Exit from paymentSuccess()");
        }catch (Exception e){
            logger.error("Exception in paymentSuccess(),[%1$s]",e.toString());
        }
    }

    public void failPaymentDetails(String errorCode,String errorDescription,String orderId){
       try{
           logger.info("Exception in failPaymentDetails()");
            orderPaymentService.updatePaymentFail(errorCode,errorDescription,orderId);
           logger.info("Exit from failPaymentDetails()");
       }catch (Exception e){
           logger.error("Exception in failPaymentDetails(),[%1$s]",e.toString());
       }
    }
}
