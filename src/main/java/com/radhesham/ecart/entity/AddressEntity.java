package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@Table(name = "m_user_address")
@Entity
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "insert_time", updatable = false)
    @CreationTimestamp()
    private Date insertTime;

    @Column(name = "update_time", updatable = false)
    @UpdateTimestamp
    private Date updateTime;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private String houseNum;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country")
    private String country;

    @Column(name="is_default",columnDefinition = "INT default 0")
    private int defaultAddress;

}
