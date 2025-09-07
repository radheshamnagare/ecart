package com.radhesham.ecart.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radhesham.ecart.bean.OrderItemDetails;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name="place-order-request")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceOrderRequest {

    @XmlElement(name = "order-id")
    @JsonProperty("order-id")
    private Integer orderId;

    @XmlElement(name = "order-tracking-number")
    @JsonProperty("order-tracking-number")
    private String orderTrackingNum;

    @XmlElement(name = "razorpay-order-id")
    @JsonProperty("razorpay-order-id")
    private String razorPayOrderId;

    @XmlElement(name = "razorpay-payment-id")
    @JsonProperty("razorpay-payment-id")
    private String razorPayPaymentId;

    @XmlElement(name="payment-status")
    @JsonProperty("payment-status")
    private String paymentStatus;

    @XmlElement(name = "order-status")
    @JsonProperty("order-status")
    private String orderStatus;

    @XmlElement(name = "total-price")
    @JsonProperty("total-price")
    private Double totalPrice;

    @XmlElement(name = "total-quantity")
    @JsonProperty("total-quantity")
    private Integer totalQuantity;

    @XmlElement(name = "address-details")
    @JsonProperty("address-details")
    private int addressId;

    @XmlElement(name="user-id")
    @JsonProperty("user-id")
    private int userId;

    @XmlElement(name="order-items")
    @JsonProperty("order-items")
    private List<OrderItemDetails> orderItemDetails;
}
