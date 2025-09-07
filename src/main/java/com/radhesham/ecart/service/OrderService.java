package com.radhesham.ecart.service;

import com.radhesham.ecart.bean.AddressDetails;
import com.radhesham.ecart.bean.OrderDetails;
import com.radhesham.ecart.bean.OrderItemDetails;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.entity.*;
import com.radhesham.ecart.repository.*;
import com.radhesham.ecart.request.OrderFilterRequest;
import com.radhesham.ecart.request.OrderItemFilterRequest;
import com.radhesham.ecart.request.PlaceOrderRequest;
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
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CartRepository cartRepository;

    private OrderDetails setOrderDetails(Optional<OrderEntity> order) {
        OrderDetails orderDetails = new OrderDetails();
        try {
            if (order.isPresent()) {
                BeanUtils.copyProperties(order.get(), orderDetails);
                Optional<AddressEntity> address = addressRepository.findById(order.get().getAddressEntity().getId());
                if (address.isPresent()) {
                    AddressDetails addressDetails = new AddressDetails();
                    BeanUtils.copyProperties(address.get(), addressDetails);
                    orderDetails.setAddressDetails(addressDetails);
                } else {
                    logger.warn("getOrderDetails() address details not found for orderId {}", order.get().getOrderId());
                }
            }
        } catch (Exception e) {
            logger.error("Exception in setOrderDetails():", e);
        }
        return orderDetails;
    }

    public List<OrderDetails> getOrderDetails(OrderFilterRequest orderFilterRequest) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        try {
            logger.info("Entry in getOrderDetails()");
            if (orderFilterRequest.getOrderId() > 0 || !CommonValidator.isEmpty(orderFilterRequest.getOrderTrackingNo())) {
                if (orderFilterRequest.getOrderId() > 0 && CommonValidator.isEmpty(orderFilterRequest.getOrderTrackingNo())) {
                    Optional<OrderEntity> order = orderRepository.findByOrderIdAndUserId(orderFilterRequest.getOrderId(), orderFilterRequest.getUserId());
                    OrderDetails orderDetails = setOrderDetails(order);
                    orderDetailsList.add(orderDetails);
                } else if (orderFilterRequest.getOrderId() <= 0 && !CommonValidator.isEmpty(orderFilterRequest.getOrderTrackingNo())) {
                    Optional<OrderEntity> order = orderRepository.findByOrderTrackingNumAndUserId(orderFilterRequest.getOrderTrackingNo(), orderFilterRequest.getUserId());
                    OrderDetails orderDetails = setOrderDetails(order);
                    orderDetailsList.add(orderDetails);
                } else {
                    List<OrderEntity> orderDetails = orderRepository.findAll();
                    orderDetails.forEach(orderInfo -> {
                        Optional<OrderEntity> order = Optional.ofNullable(orderInfo);
                        OrderDetails orderDetail = setOrderDetails(order);
                        orderDetailsList.add(orderDetail);
                    });
                }
            }
            logger.info("Exit from getOrderDetails()");
        } catch (Exception e) {
            logger.error("Exception in getOrderDetails():", e);
        }
        return orderDetailsList;
    }

    private List<OrderItemDetails> setOrderItems(int orderId) {
        List<OrderItemDetails> orderItemDetails = new ArrayList<>();
        try {
            logger.info("Entry in setOrderItems()");
            List<OrderItemEntity> orderDetails = orderItemRepository.findByOrderOrderId(orderId);
            orderDetails.forEach(orderItem -> {
                OrderItemDetails orderItemBean = new OrderItemDetails();
                BeanUtils.copyProperties(orderItem, orderItemBean);
                Optional<ProductEntity> product = productsRepository.findById(orderItem.getProduct().getProductId());
                if(product.isPresent()){
                    orderItemBean.setProductId(product.get().getProductId());
                    orderItemBean.setProductName(product.get().getName());
                }
                orderItemDetails.add(orderItemBean);
            });
            logger.info("Exit from setOrderItems()");
        } catch (Exception e) {
            logger.error("Exception in setOrderItems():", e);
        }
        return orderItemDetails;
    }

    public List<OrderItemDetails> getOrderItemDetails(OrderItemFilterRequest orderItemFilterRequest) {
        List<OrderItemDetails> orderItemDetails = null;
        try {
            logger.info("Entry in getOrderItemDetails()");
            if (Objects.nonNull(orderItemFilterRequest)) {
                if (orderItemFilterRequest.getOrderId() > 0 && !CommonValidator.isEmpty(orderItemFilterRequest.getOrderTrackingNo()) && orderItemFilterRequest.getUserId() > 0) {
                    Optional<OrderEntity> order = orderRepository.findByOrderIdAndUserId(orderItemFilterRequest.getOrderId(), orderItemFilterRequest.getUserId());
                    if (order.isPresent()) {
                        OrderEntity orderInfo = order.get();
                        orderItemDetails = setOrderItems(orderInfo.getOrderId());
                    } else {
                        logger.info("getOrderItemDetails() order details not found");
                    }
                } else if (orderItemFilterRequest.getOrderId() <= 0 && !CommonValidator.isEmpty(orderItemFilterRequest.getOrderTrackingNo()) && orderItemFilterRequest.getUserId() > 0) {
                    Optional<OrderEntity> order = orderRepository.findByOrderTrackingNumAndUserId(orderItemFilterRequest.getOrderTrackingNo(), orderItemFilterRequest.getUserId());
                    if (order.isPresent()) {
                        OrderEntity orderInfo = order.get();
                        orderItemDetails = setOrderItems(orderInfo.getOrderId());
                    } else {
                        logger.info("getOrderItemDetails() order details not found by orderTrackingNumber and userId");
                    }
                } else if (orderItemFilterRequest.getOrderId() <= 0 && !CommonValidator.isEmpty(orderItemFilterRequest.getOrderTrackingNo()) && orderItemFilterRequest.getUserId() <= 0) {
                    Optional<OrderEntity> order = orderRepository.findByOrderTrackingNum(orderItemFilterRequest.getOrderTrackingNo());
                    if (order.isPresent()) {
                        OrderEntity orderInfo = order.get();
                        orderItemDetails = setOrderItems(orderInfo.getOrderId());
                    } else {
                        logger.info("getOrderItemDetails() order details not found by orderTrackingNumber and userId by orderTrackingNum");
                    }
                }
            }
            logger.info("Exit from getOrderItemDetails()");
        } catch (Exception e) {
            logger.error("Exception in getOrderItemDetails():", e);
        }
        return orderItemDetails.isEmpty() ? new ArrayList<>() : orderItemDetails;
    }

    public OrderDetails placeOrder(PlaceOrderRequest placeOrderRequest) {
        OrderDetails placeOrderInfo = new OrderDetails();
        try {
            logger.info("Entry in placeOrder()");
            OrderEntity order = new OrderEntity();
            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setId(placeOrderRequest.getAddressId());
            UserEntity userEntity = new UserEntity();
            userEntity.setId(placeOrderRequest.getUserId());
            BeanUtils.copyProperties(placeOrderRequest, order);
            order.setAddressEntity(addressEntity);
            order.setUser(userEntity);
            order.setOrderId(null);
            OrderEntity saveOrder = orderRepository.save(order);

            if (saveOrder.getOrderId() > 0) {
                for (OrderItemDetails item : placeOrderRequest.getOrderItemDetails()) {
                    OrderItemEntity orderItem = new OrderItemEntity();
                    BeanUtils.copyProperties(item, orderItem);
                    orderItem.setOrder(saveOrder);
                    Optional<ProductEntity> product = productsRepository.findById(item.getProductId());
                    if(product.isPresent()){
                        ProductEntity tmpProd = product.get();
                        orderItem.setProduct(tmpProd);
                        if(tmpProd.getUnitsInStock()>= orderItem.getQuantity()){
                            int tempCartItemId=orderItem.getItemId();
                            orderItem.setItemId(null);
                            OrderItemEntity saveOrderItem = orderItemRepository.save(orderItem);
                            if(saveOrderItem!=null && tempCartItemId>0){
                                Optional<TempCartEntity> tempCart = cartRepository.findById(tempCartItemId);
                                cartRepository.delete(tempCart.get());
                            }
                        }
                    }
                }
            }
            BeanUtils.copyProperties(saveOrder, placeOrderInfo);
            logger.info("Exit from placeOrder()");
        } catch (Exception e) {
            logger.info("Exception in placeOrder():", e);
        }
        return placeOrderInfo;
    }


}
