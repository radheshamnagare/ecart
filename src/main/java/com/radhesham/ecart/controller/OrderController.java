package com.radhesham.ecart.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.model.ManageOrder;
import com.radhesham.ecart.request.OrderFilterRequest;
import com.radhesham.ecart.request.OrderItemFilterRequest;
import com.radhesham.ecart.request.PlaceOrderRequest;
import com.radhesham.ecart.response.OrderDetailsResponse;
import com.radhesham.ecart.response.OrderItemResponse;
import com.radhesham.ecart.response.PlaceOrderResponse;
import com.radhesham.ecart.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final UserService userService;
    private final OrderService orderService;
    private final AddressService addressService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderPaymentService orderPaymentService;
    @Value("${rezorpay.secrete.key}")
    private String RAZORPAY_SECRET;

    @Autowired
    public OrderController(UserService userService, OrderService orderService, AddressService addressService, ProductService productService, CartService cartService, OrderPaymentService orderPaymentService) {
        this.userService = userService;
        this.orderService = orderService;
        this.addressService = addressService;
        this.productService = productService;
        this.cartService = cartService;
        this.orderPaymentService = orderPaymentService;
    }

    @PostMapping(value = "/lookup", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<OrderDetailsResponse> getAllProductDetails(@RequestBody OrderFilterRequest orderFilterRequest) {
        OrderDetailsResponse response = null;
        try {
            logger.info("Entry in getAllProductDetails()");
            ManageOrder manageOrder = new ManageOrder();
            injectService(manageOrder);
            response = manageOrder.manageOrderDetails(orderFilterRequest);
            logger.info("Exit from getAllProductDetails()");
        } catch (Exception e) {
            logger.error("Exception in getAllProductDetails():", e.toString());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "item/lookup", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<OrderItemResponse> getOrderItemDetails(@RequestBody OrderItemFilterRequest oderOrderItemFilterRequest) {
        OrderItemResponse response = null;
        try {
            logger.info("Entry in getOrderItemDetails()");
            ManageOrder manageOrder = new ManageOrder();
            injectService(manageOrder);
            response = manageOrder.manageOrderItems(oderOrderItemFilterRequest);
            logger.info("Exit from getOrderItemDetails()");
        } catch (Exception e) {
            logger.error("Exception in getOrderItemDetails()", e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/place", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PlaceOrderResponse> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest) {
        PlaceOrderResponse response = null;
        try {
            logger.info("Entry in placeOrder()");
            ManageOrder manageOrder = new ManageOrder();
            injectService(manageOrder);
            response = manageOrder.managePlaceOrder(placeOrderRequest);
            logger.info("Exit from placeOrder()");
        } catch (Exception e) {
            logger.error("Exception in placeOrder():", e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/payment/details", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> handleCallback(@RequestParam Map<String, String> formParams) {
        try{
            ManageOrder manageOrder=new ManageOrder();
            injectService(manageOrder);
            if (formParams.containsKey("razorpay_payment_id")) {
                // ✅ SUCCESS case
                String paymentId = formParams.get("razorpay_payment_id");
                String orderId = formParams.get("razorpay_order_id");
                String signature = formParams.get("razorpay_signature");

                if (verifySignature(orderId, paymentId, signature, RAZORPAY_SECRET)) {
                    manageOrder.paymentSuccess(orderId,paymentId);
                    return ResponseEntity.ok("Payment verified successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
                }

            } else if (formParams.containsKey("error[code]")) {
                String errorCode = formParams.get("error[code]");
                String errorDesc = formParams.get("error[description]");
                String orderId = formParams.get("razorpay_order_id");
                manageOrder.failPaymentDetails(errorCode,errorDesc,orderId);
                return ResponseEntity.ok("Payment failed: " + errorDesc);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid callback data");
            }
        }catch (Exception e){
            logger.error("",e.toString());
        }
        return   ResponseEntity.ok("Payment verified successfully");
    }

    private boolean verifySignature(String orderId, String paymentId, String razorpaySignature, String secret) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            String generatedSignature = Base64.getEncoder().encodeToString(mac.doFinal(payload.getBytes()));
            return generatedSignature.equals(razorpaySignature);
        } catch (Exception e) {
            return false;
        }
    }

    private void injectService(ManageOrder manageOrder) {
        try {
            logger.info("Entry in injectService()");
            manageOrder.setUserService(userService);
            manageOrder.setOrderService(orderService);
            manageOrder.setAddressService(addressService);
            manageOrder.setProductService(productService);
            manageOrder.setCartService(cartService);
            manageOrder.setOrderPaymentService(orderPaymentService);
            logger.info("Exit from injectService()");
        } catch (Exception e) {
            logger.error("Exception in injectService():", e);
        }
    }
}
