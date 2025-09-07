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
@XmlRootElement(name = "product-detail")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetail {

    @XmlElement(name = "product-id")
    @JsonProperty("product-id")
    private Long productId;

    @XmlElement(name = "product-name")
    @JsonProperty("product-name")
    private String name;

    @XmlElement(name = "title")
    @JsonProperty("title")
    private String title;

    @XmlElement(name = "description")
    @JsonProperty("description")
    private String description;

    @XmlElement(name = "product-image")
    @JsonProperty("product-image")
    private String imageUrl;

    @XmlElement(name = "unit-price")
    @JsonProperty("unit-price")
    private Double unitPrice;

    @XmlElement(name = "active")
    @JsonProperty("active")
    private boolean active;

    @XmlElement(name = "units-stock")
    @JsonProperty("units-stock")
    private int unitsInStock;

    @XmlElement(name = "insert-time")
    @JsonProperty("insert-time")
    private Date insertTime;

    @XmlElement(name = "update-time")
    @JsonProperty("update-time")
    private Date updateTime;
}
