package com.booking.app_booking.service;

import com.booking.app_booking.dto.NotificationDTO;

public interface NotificationService {

    void sendEmail(NotificationDTO notificationDTO);

    void sendSMS();

    void sendWhatsapp();
}
