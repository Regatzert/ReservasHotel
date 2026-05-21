package com.booking.app_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.app_booking.entity.Payment;

public interface PaymentRepository  extends JpaRepository<Payment, Long> {

}
