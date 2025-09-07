package com.radhesham.ecart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;

@Setter
@Getter
@Table(name = "t_send_email_details")
@Entity
public class SendEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="subject")
    private String subject;

    @Column(name="body",columnDefinition = "text")
    private String body;

    @Column(name="to_emails",columnDefinition = "text")
    private String toEmail;

    @Column(name="cc_emails",columnDefinition = "text")
    private String ccEmail;

    @Column(name="bcc_emails",columnDefinition = "text")
    private String bccEmail;

    @Lob
    @Column(name = "attachment",columnDefinition = "MEDIUMBLOB")
    private byte[] attachments;

    @Column(name="filename")
    private String fileName;

    @Column(name="status",columnDefinition = "varchar(30) default 'pending'")
    private String status;

    @Column(name="remark",columnDefinition = "varchar(255)")
    private String remark;
}
