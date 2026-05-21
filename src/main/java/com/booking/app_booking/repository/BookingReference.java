package com.booking.app_booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingReference extends JpaRepository<BookingReference, Long> {

    Optional<BookingReference> findByReferenceNo(String referenceNo);

}
