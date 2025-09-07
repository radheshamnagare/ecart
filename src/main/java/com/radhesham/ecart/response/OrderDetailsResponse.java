package com.radhesham.ecart.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.radhesham.ecart.bean.OrderDetails;
import com.radhesham.ecart.bean.ResponseStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "order-details-response")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDetailsResponse extends ResponseStatus {

    @XmlElement(name = "order-details")
    @JsonProperty("order-details")
    List<OrderDetails> orderDetails;
}
