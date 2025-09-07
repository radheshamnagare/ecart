package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@Table(name = "m_system_error")
@Entity
public class SystemErrorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int errorKey;

    @Column(name = "error_code")
    String errorCode;

    @Column(name = "error_status")
    String errorStatus;

    @Column(name = "error_description", columnDefinition = "varchar(255) default ''")
    String errorDescription;

    @CreationTimestamp
    @Column(name = "insert_time", updatable = false)
    Date insertTime;

    @UpdateTimestamp
    @Column(name = "update_time", insertable = false)
    Date updateTime;
}
