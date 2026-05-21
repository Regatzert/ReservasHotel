package com.booking.app_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.app_booking.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
