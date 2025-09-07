package com.radhesham.ecart.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name="register-user-dto")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterUserRequest {

    @XmlElement(name="name")
    @JsonProperty("name")
    private String name;

    @XmlElement(name="email")
    @JsonProperty("email")
    private String email;

    @XmlElement(name="password")
    @JsonProperty("password")
    private String password;

    @XmlElement(name="phone")
    @JsonProperty("phone")
    private String phone;

}
