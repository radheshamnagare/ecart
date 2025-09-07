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
@XmlRootElement(name="order-item-details")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDetails {

    @XmlElement(name="item-id")
    @JsonProperty("item-id")
    private int itemId;

    @XmlElement(name="order-id")
    @JsonProperty("order-id")
    private int orderId;

    @XmlElement(name="product-id")
    @JsonProperty("product-id")
    private long productId;

    @XmlElement(name="product-name")
    @JsonProperty("product-name")
    private String productName;

    @XmlElement(name="unit-price")
    @JsonProperty("unit-price")
    private Double unitPrice;

    @XmlElement(name="quantity")
    @JsonProperty("quantity")
    private Integer quantity;

    @XmlElement(name="insert-time")
    @JsonProperty("insert-time")
    private Date insertTime;
}
