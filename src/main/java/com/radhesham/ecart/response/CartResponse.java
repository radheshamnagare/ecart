package com.radhesham.ecart.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radhesham.ecart.bean.CartItemBean;
import com.radhesham.ecart.bean.ResponseStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "cart-response")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse extends ResponseStatus {

    @XmlElement(name = "cart-items")
    @JsonProperty("cart-items")
    private List<CartItemBean> cartItem;

    @XmlElement(name="total-amount")
    @JsonProperty("total-amount")
    private double totalAmount=0;

    @XmlElement(name="total-items")
    @JsonProperty("total-items")
    private int totalItems=0;
}
