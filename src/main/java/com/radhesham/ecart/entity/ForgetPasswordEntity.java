package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@Data
@Table(name = "t_forget_password")
@Entity
public class ForgetPasswordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="token")
    private String token;

    @Column(name="user_id")
    private int userId;

    @Column(name="status",columnDefinition = "varchar(30) default 'pending'")
    private String status;

    @CreationTimestamp
    @Column(name="insert_time",updatable = false)
    private Date insertTime;

    @UpdateTimestamp
    @Column(name="update_time",insertable = false)
    private Date updateTime;
}
