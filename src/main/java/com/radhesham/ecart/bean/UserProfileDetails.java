package com.radhesham.ecart.bean;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileDetails {
    List<AddressDetails> addressDetails;
    private int id;
    private String name;
    private String email;
    private String phone;
}
