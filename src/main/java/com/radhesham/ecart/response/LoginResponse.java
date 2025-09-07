package com.radhesham.ecart.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radhesham.ecart.bean.ResponseStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "login-response")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    @XmlElement(name = "status")
    @JsonProperty("status")
    ResponseStatus status;

    @XmlElement(name = "token")
    @JsonProperty("token")
    private String token;

    @XmlElement(name = "expires-in")
    @JsonProperty("expires-in")
    private long expiresIn;

}
