package com.radhesham.ecart.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name="reset-user-password-request")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResetUserPasswordRequest {

    @XmlElement(name="user-token")
    @JsonProperty("user-token")
    private String userToken;

    @XmlElement(name="new-password")
    @JsonProperty("new-password")
    private String newPassword;

    @XmlElement(name="confirm-password")
    @JsonProperty("confirm-password")
    private String confirmPassword;

    @XmlElement(name="user_id")
    @JsonProperty("user_id")
    private int userId;
}
