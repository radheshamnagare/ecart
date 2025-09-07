package com.radhesham.ecart.process;

import com.radhesham.ecart.bean.SendEmailBean;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.SendEmailEntity;
import com.radhesham.ecart.repository.SendEmailRepository;
import com.radhesham.ecart.service.SendEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EmailSender implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private final SendEmailRepository sendEmailRepository;
    private final SendEmailService sendEmailService;

    public EmailSender(SendEmailRepository sendEmailRepository,SendEmailService sendEmailService) {
        this.sendEmailRepository = sendEmailRepository;
        this.sendEmailService=sendEmailService;
    }

    @Override
    public void run() {
        try {
            logger.info("Entry in EmailSender()");
            while (isServiceInjected()) {
                List<SendEmailEntity> emailDetails = sendEmailRepository.findByStatus(ConstantsPool.STATUS_PENDING);
                if (!emailDetails.isEmpty()) {
                    for (SendEmailEntity sendEmailEntity : emailDetails) {
                        SendEmailBean sendEmailBean = new SendEmailBean();
                        sendEmailBean.setSubject(sendEmailEntity.getSubject());
                        sendEmailBean.setBody(sendEmailEntity.getBody());
                        sendEmailBean.setRemark(sendEmailEntity.getRemark());
                        String[] toAdr = sendEmailEntity.getToEmail().split(",");
                        sendEmailBean.setToEmail(Arrays.asList(toAdr));

                       boolean isMailSent= sendEmailService.sendEmail(sendEmailBean);
                       if(isMailSent)
                           sendEmailEntity.setStatus(ConstantsPool.STATUS_SUCCESS);
                       else
                           sendEmailEntity.setStatus(ConstantsPool.STATUS_FAILED);

                       sendEmailRepository.save(sendEmailEntity);

                    }
                }else{
                    Thread.sleep(1000L);
                    logger.info("EmailSender() no email record found for send");
                }
            }
            logger.info("Exit from EmailSender()");
        } catch (Exception e) {
            logger.error("Exception in EmailSender()", e);
        }
    }

    private boolean isServiceInjected() {
        return sendEmailRepository != null;
    }
}
