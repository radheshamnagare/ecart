package com.radhesham.ecart.service;

import com.radhesham.ecart.bean.SendEmailBean;
import com.radhesham.ecart.common.CommonValidator;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.SendEmailEntity;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
public class SendEmailService {
    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);
    @Autowired
    JavaMailSender mailSender;

    public boolean sendEmail(SendEmailBean sendEmailBean) {
        boolean flag = true;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // true = multipart message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("radheshamnagare@gmail.com");
            helper.setSubject(sendEmailBean.getSubject());
            helper.setText(sendEmailBean.getBody(), true);

            // To addresses
            List<String> toList = sendEmailBean.getToEmail();
            if (toList != null && !toList.isEmpty()) {
                helper.setTo(toList.toArray(new String[0]));
            }

            // CC addresses
            List<String> ccList = sendEmailBean.getCcEmail();
            if (ccList != null && !ccList.isEmpty()) {
                helper.setCc(ccList.toArray(new String[0]));
            }

            // BCC addresses
            List<String> bccList = sendEmailBean.getBccEmail();
            if (bccList != null && !bccList.isEmpty()) {
                helper.setBcc(bccList.toArray(new String[0]));
            }

            // Attachments
            InputStream attachment = sendEmailBean.getAttachments();
            if (Objects.nonNull(attachment)) {
                if (attachment != null) {
                    InputStreamSource source = new ByteArrayResource(attachment.readAllBytes());
                    helper.addAttachment(sendEmailBean.getFileName(), source);
                }

            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("Exception in sendEmail(): ", e);
            flag = false;
        }

        return flag;
    }


    public int saveEmailDetails(SendEmailBean sendEmailBean) {
        int total = 0;
        try {
            logger.info("Entry  in saveEmailDetails()");
            SendEmailEntity sendEmailEntity = new SendEmailEntity();

            sendEmailEntity.setSubject(sendEmailEntity.getSubject());
            sendEmailEntity.setBody(sendEmailBean.getBody());
            sendEmailEntity.setFileName(sendEmailEntity.getFileName());
            if (sendEmailBean.getAttachments() != null)
                sendEmailEntity.setAttachments(sendEmailBean.getAttachments().readAllBytes());

            if (CommonValidator.isEmpty(sendEmailBean.getToEmail())) {
                String toEmails = String.join(sendEmailEntity.getToEmail(), ",");
                sendEmailEntity.setToEmail(toEmails);
            }

            if (CommonValidator.isEmpty(sendEmailBean.getBccEmail())) {
                String bccEmails = String.join(sendEmailEntity.getBccEmail(), ",");
                sendEmailEntity.setBccEmail(bccEmails);
            }

            if (CommonValidator.isEmpty(sendEmailBean.getCcEmail())) {
                String ccEmails = String.join(sendEmailEntity.getCcEmail(), ",");
                sendEmailEntity.setCcEmail(ccEmails);
            }

            sendEmailEntity.setStatus(ConstantsPool.STATUS_PENDING);


            logger.info("Exit from saveEmailDetails()");
        } catch (Exception e) {
            logger.error("Exception in saveEmailDetails()", e);
        }
        return total;
    }
}
