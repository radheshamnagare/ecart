package com.radhesham.ecart.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "order-item-filter-request")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemFilterRequest {

    @XmlElement(name = "order-id")
    @JsonProperty("order-id")
    int orderId;

    @XmlElement(name = "order-tracking-no")
    @JsonProperty("order-tracking-no")
    String orderTrackingNo;

    @XmlElement(name="user_id")
    @JsonProperty("user_id")
    int userId;

}
