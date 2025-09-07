package com.radhesham.ecart.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.radhesham.ecart.bean.AddressDetails;
import com.radhesham.ecart.bean.ResponseStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name="user-profile-response")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse extends ResponseStatus {

    @XmlElement(name="id")
    @JsonProperty("id")
    private int id;

    @XmlElement(name="user-name")
    @JsonProperty("user-name")
    private String name;

    @XmlElement(name="email")
    @JsonProperty("email")
    private String email;

    @XmlElement(name="phone")
    @JsonProperty("phone")
    private String phone;

    @XmlElement(name="address-details")
    @JsonProperty("address-details")
    List<AddressDetails> addressDetails;
}
