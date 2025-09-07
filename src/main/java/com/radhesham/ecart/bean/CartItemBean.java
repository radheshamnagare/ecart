package com.radhesham.ecart.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name="cart-item")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemBean {

    @XmlElement(name="id")
    @JsonProperty("id")
    private int id;

    @XmlElement(name="product-id")
    @JsonProperty("product-id")
    private long productId;

    @XmlElement(name="product-title")
    @JsonProperty("product-title")
    private String productTitle;

    @XmlElement(name="product-name")
    @JsonProperty("product-name")
    private String productName;

    @XmlElement(name="product-image")
    @JsonProperty("product-image")
    private String productImage;

    @XmlElement(name="description")
    @JsonProperty("description")
    private String description;

    @XmlElement(name="unit-price")
    @JsonProperty("unit-price")
    private double unitPrice;


    @XmlElement(name="quantity")
    @JsonProperty("quantity")
    private int quantity;

    @XmlElement(name="user-id")
    @JsonProperty("user-id")
    private int userId;

    @XmlElement(name="action")
    @JsonProperty("action")
    private String action;
}
