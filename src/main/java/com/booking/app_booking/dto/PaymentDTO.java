package com.booking.app_booking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.booking.app_booking.enums.PaymentGateway;
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
public class PaymentDTO {
    private Long id;
    private BookingDTO booking;
    private String transactionId;
    private BigDecimal amount;
    private PaymentGateway paymentMethod;
    private LocalDateTime paymentDate;
    private PaymentStatus status;
    private String bookingReference;
    private String failureReason;
    private String approvalLink;
}
