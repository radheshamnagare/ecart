package com.radhesham.ecart.bean;

import lombok.Data;

import java.io.InputStream;
import java.util.List;

@Data
public class SendEmailBean {

    private String subject;

    private String body;

    private List<String> toEmail;

    private List<String> ccEmail;

    private List<String> bccEmail;

    private InputStream attachments;

    private String fileName;

    private String remark;


    @Override
    public String toString() {
        return "SendEmailBean{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", toEmail=" + toEmail +
                ", ccEmail=" + ccEmail +
                ", bccEmail=" + bccEmail +
                ", attachments=" + attachments +
                ", fileName='" + fileName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
