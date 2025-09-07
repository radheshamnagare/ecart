package com.radhesham.ecart.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "response-status")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseStatus {

    @XmlElement(name = "error-code")
    @JsonProperty("error-code")
    String errorCode;

    @XmlElement(name = "error-status")
    @JsonProperty("error-status")
    String errorStatus;

    @XmlElement(name = "error-description")
    @JsonProperty("error-description")
    String errorDescription;

}
