package com.radhesham.ecart.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "address-details")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDetails {

    @XmlElement(name = "id")
    @JsonProperty("id")
    private int id;

    @XmlElement(name = "house-no")
    @JsonProperty("house-no")
    private String houseNum;

    @XmlElement(name = "street")
    @JsonProperty("street")
    private String street;

    @XmlElement(name = "city")
    @JsonProperty("city")
    private String city;

    @XmlElement(name = "state")
    @JsonProperty("state")
    private String state;

    @XmlElement(name = "zip-code")
    @JsonProperty("zip-code")
    private String zipCode;

    @XmlElement(name = "country")
    @JsonProperty("country")
    private String country;

    @XmlElement(name = "is-default")
    @JsonProperty("is-default")
    private int defaultAddress;

    @XmlElement(name="action")
    @JsonProperty("action")
    private String action;
}
