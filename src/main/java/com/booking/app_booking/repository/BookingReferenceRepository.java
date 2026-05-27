package com.booking.app_booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingReferenceRepository extends JpaRepository<BookingReferenceRepository, Long> {

    Optional<BookingReferenceRepository> findByReferenceNo(String referenceNo);

}
