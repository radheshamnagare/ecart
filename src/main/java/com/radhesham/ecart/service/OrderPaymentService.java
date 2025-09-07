package com.radhesham.ecart.service;

import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.OrderEntity;
import com.radhesham.ecart.entity.OrderItemEntity;
import com.radhesham.ecart.entity.ProductEntity;
import com.radhesham.ecart.entity.RazorpayOrdersEntity;
import com.radhesham.ecart.repository.OrderItemRepository;
import com.radhesham.ecart.repository.OrderPaymentRepository;
import com.radhesham.ecart.repository.OrderRepository;
import com.radhesham.ecart.repository.ProductsRepository;
import com.radhesham.ecart.request.PlaceOrderRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderPaymentService {
    private static final Logger logger = LoggerFactory.getLogger(OrderPaymentService.class);

    @Autowired
    OrderPaymentRepository orderPaymentRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    NotificationService notificationService;

    @Value("${rezorpay.key.id}")
    private String razorpayKey;
    @Value("${rezorpay.secrete.key}")
    private String razorpaySecreteKey;
    private RazorpayClient razorpayClient;

    public void createRezorpayOrder(PlaceOrderRequest placeOrderRequest) {
        try {
            logger.info("Entry in createRezorpayOrder()");
            JSONObject order = new JSONObject();
            order.put("amount", placeOrderRequest.getTotalPrice() * 100);
            order.put("currency", "INR");
            order.put("receipt", placeOrderRequest.getOrderTrackingNum());
            razorpayClient = new RazorpayClient(razorpayKey, razorpaySecreteKey);
            Order razorpayOrder = razorpayClient.orders.create(order);

            placeOrderRequest.setRazorPayOrderId(razorpayOrder.get("id"));
            placeOrderRequest.setOrderStatus(razorpayOrder.get("status"));

            RazorpayOrdersEntity razorpayOrdersEntity = new RazorpayOrdersEntity();
            razorpayOrdersEntity.setOrderId(razorpayOrder.get("id"));
            razorpayOrdersEntity.setStatus(razorpayOrder.get("status"));
            razorpayOrdersEntity.setEntity(razorpayOrder.get("entity"));
            razorpayOrdersEntity.setAmount(razorpayOrder.get("amount"));
            razorpayOrdersEntity.setAmountPaid(razorpayOrder.get("amount_paid"));
            razorpayOrdersEntity.setAmountDue(razorpayOrder.get("amount_due"));
            razorpayOrdersEntity.setCurrency(razorpayOrder.get("currency"));
            razorpayOrdersEntity.setReceipt(razorpayOrder.get("receipt"));
            razorpayOrdersEntity.setAttempts(razorpayOrder.get("attempts"));
            Date createdAt = razorpayOrder.get("created_at");
            razorpayOrdersEntity.setCreatedAt(createdAt);
            RazorpayOrdersEntity savedOrder = orderPaymentRepository.save(razorpayOrdersEntity);
            logger.info("Exit from createRezorpayOrder()");
        } catch (Exception e) {
            logger.error("Exception in createRezorpayOrder()", e);
        }
    }

    public int updatePaymentSuccess(String razorPayOrderId, String razorpayPaymentId) {
        int total = 0;
        try {
            logger.info("Entry in updatePaymentSuccess()");
            RazorpayOrdersEntity razorpayOrdersEntity = orderPaymentRepository.findByOrderId(razorPayOrderId);
            razorpayOrdersEntity.setAmountDue(0);
            razorpayOrdersEntity.setAmountPaid(razorpayOrdersEntity.getAmount());
            razorpayOrdersEntity.setStatus(ConstantsPool.STATUS_SUCCESS);
            razorpayOrdersEntity.setAttempts(1);
            razorpayOrdersEntity.setRemarks("Payment Successful");
            orderPaymentRepository.save(razorpayOrdersEntity);

            Optional<OrderEntity> order = orderRepository.findByRazorPayOrderId(razorPayOrderId);
            if (order.isPresent()) {
                OrderEntity orderInfo = order.get();
                orderInfo.setOrderStatus(ConstantsPool.STATUS_SUCCESS);
                orderInfo.setRazorPayPaymentId(razorpayPaymentId);
                orderRepository.save(orderInfo);
                int orderId = orderInfo.getOrderId();
                List<OrderItemEntity> items =orderItemRepository.findByOrderOrderId(orderId);
                for(OrderItemEntity item : items){
                  Optional<ProductEntity> product =  productsRepository.findById(item.getProduct().getProductId()) ;
                  if(product.isPresent()){
                      ProductEntity productInfo = product.get();
                      productInfo.setUnitsInStock(productInfo.getUnitsInStock()- item.getQuantity());
                      productsRepository.save(productInfo);
                  }
                }
                notificationService.sendOrderDetails(orderInfo);
            }
            logger.info("Exit from updatePaymentSuccess()");
        } catch (Exception e) {
            logger.error("Exception in updatePaymentSuccess(),[%1$s]", e.toString());
        }
        return total;
    }

    public int updatePaymentFail(String errorCode, String errorDescription, String razorpayOrderId) {
        int total = 0;
        try {
            logger.info("Entry in updatePaymentFail()");
            RazorpayOrdersEntity razorpayOrdersEntity = orderPaymentRepository.findByOrderId(razorpayOrderId);
            razorpayOrdersEntity.setStatus(ConstantsPool.STATUS_FAILED);
            razorpayOrdersEntity.setAttempts(1);
            razorpayOrdersEntity.setRemarks("Payment Failed :" + errorCode + " " + errorDescription);
            orderPaymentRepository.save(razorpayOrdersEntity);

            Optional<OrderEntity> order = orderRepository.findByRazorPayOrderId(razorpayOrderId);
            if (order.isPresent()) {
                OrderEntity orderInfo = order.get();
                orderInfo.setOrderStatus(ConstantsPool.STATUS_FAILED);
                orderRepository.save(orderInfo);
                notificationService.sendOrderDetails(orderInfo);
            }
            logger.info("Exit from updatePaymentFail()");
        } catch (Exception e) {
            logger.error("Exception in updatePaymentFail(),[%1$s]", e.toString());
        }
        return total;
    }
}
