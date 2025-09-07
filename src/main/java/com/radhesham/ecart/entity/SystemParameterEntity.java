package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Setter
@Getter
@Table(name = "m_parameters")
@Entity
public class SystemParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "param_key")
    String key;

    @Column(name = "param_val",columnDefinition = "text")
    String value;

    @Column(name = "insert_by")
    int insertedBy;

    @Column(name = "update_by")
    int updatedBy;

    @Column(name = "insert_time", updatable = false)
    @CreationTimestamp
    Date insertTime;

    @Column(name = "update_time", insertable = false)
    @CreationTimestamp
    Date updateTime;
}
