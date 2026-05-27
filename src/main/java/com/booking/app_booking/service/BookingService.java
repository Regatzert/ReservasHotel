package com.booking.app_booking.service;

import com.booking.app_booking.dto.BookingDTO;
import com.booking.app_booking.dto.Response;

public interface BookingService {
    Response getAllBooking();
    Response createBooking(BookingDTO bookingDTO);
    Response findBookingByReferenceNo(String BookingReference);
    Response updateBooking(BookingDTO bookingDTO);
}
