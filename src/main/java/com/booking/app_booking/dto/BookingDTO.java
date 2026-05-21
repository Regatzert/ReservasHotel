package com.booking.app_booking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.booking.app_booking.enums.BookingStatus;
import com.booking.app_booking.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDTO {
    private Long id;
    private UserDTO user;
    private RoomDTO room;
    private Long roomId;
    private PaymentStatus paymentStatus;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BigDecimal totalPrice;
    private String bookingReference;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private BookingStatus bookingStatus;
}
