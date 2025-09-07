package com.radhesham.ecart.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.Date;

@Data
@XmlRootElement(name="order-details")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetails {

    @XmlElement(name = "order-id")
    @JsonProperty("order-id")
    private int orderId;

    @XmlElement(name="customer-name")
    @JsonProperty("customer-name")
    private String customerName;

    @XmlElement(name="phone-no")
    @JsonProperty("phone-no")
    private String phoneNo;

    @XmlElement(name = "email")
    @JsonProperty("email")
    private String email;

    @XmlElement(name = "order-tracking-number")
    @JsonProperty("order-tracking-number")
    private String orderTrackingNum;

    @XmlElement(name = "razorpay-order-id")
    @JsonProperty("razorpay-order-id")
    private String razorPayOrderId;

    @XmlElement(name = "razorpay-payment-id")
    @JsonProperty("razorpay-payment-id")
    private String razorPayPaymentId;

    @XmlElement(name = "order-status")
    @JsonProperty("order-status")
    private String orderStatus;

    @XmlElement(name = "total-price")
    @JsonProperty("total-price")
    private Double totalPrice;

    @XmlElement(name = "total-quantity")
    @JsonProperty("total-quantity")
    private Integer totalQuantity;

    @XmlElement(name = "delivery-date")
    @JsonProperty("delivery-date")
    private Date deliveryDate;

    @XmlElement(name = "address-details")
    @JsonProperty("address-details")
    private AddressDetails addressDetails;

    @XmlElement(name = "date-created")
    @JsonProperty("date-created")
    private Date dateCreated;

    @XmlElement(name = "last-updated")
    @JsonProperty("last-updated")
    private Date lastUpdated;
}
