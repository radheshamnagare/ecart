package com.radhesham.ecart.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radhesham.ecart.bean.ProductDetail;
import com.radhesham.ecart.bean.ResponseStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name="product-details-response")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailsResponse extends ResponseStatus {

    @XmlElement(name = "product-details")
    @JsonProperty("product-details")
    private List<ProductDetail> productDetails;
}
