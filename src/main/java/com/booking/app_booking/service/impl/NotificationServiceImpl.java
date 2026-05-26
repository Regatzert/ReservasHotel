package com.booking.app_booking.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.booking.app_booking.dto.NotificationDTO;
import com.booking.app_booking.entity.Notification;
import com.booking.app_booking.enums.NotificationType;
import com.booking.app_booking.repository.NotificationRepository;
import com.booking.app_booking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailService;
    private final NotificationRepository notificationRepository;

    @Override
    @Async
    public void sendEmail(NotificationDTO notificationDTO) {

        log.info("Sending email to: {}", notificationDTO.getRecipient());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(notificationDTO.getRecipient());
        simpleMailMessage.setSubject(notificationDTO.getSubject());
        simpleMailMessage.setText(notificationDTO.getMessage());

        javaMailService.send(simpleMailMessage);

        Notification notificationToSave = Notification.builder()
                .recipient(notificationDTO.getRecipient())
                .subject(notificationDTO.getSubject())
                .body(notificationDTO.getMessage())
                .bookingReference(notificationDTO.getBookingReference())
                .type(NotificationType.EMAIL)
                .build();
        notificationRepository.save(notificationToSave);
    }

    @Override
    public void sendSMS() {
        log.info("Sending SMS");
        // Implement SMS sending logic here
    }

    @Override
    public void sendWhatsapp() {
        log.info("Sending WhatsApp message");
        // Implement WhatsApp sending logic here
    }

}
