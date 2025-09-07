package com.radhesham.ecart.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "notification-request")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRequest {

    @XmlElement(name = "product-id")
    @JsonProperty("product-id")
    int productId;

    @XmlElement(name = "user-id")
    @JsonProperty("user-id")
    int userId;

  @Override
  public String toString() {
    return "NotificationRequest{" +
            "productId=" + productId +
            ", userId=" + userId +
            '}';
  }
}
