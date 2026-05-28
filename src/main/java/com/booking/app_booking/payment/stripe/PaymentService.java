package com.booking.app_booking.payment.stripe;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.booking.app_booking.dto.NotificationDTO;
import com.booking.app_booking.entity.Booking;
import com.booking.app_booking.entity.Payment;
import com.booking.app_booking.enums.NotificationType;
import com.booking.app_booking.enums.PaymentGateway;
import com.booking.app_booking.enums.PaymentStatus;
import com.booking.app_booking.exception.NotFoundException;
import com.booking.app_booking.payment.stripe.dto.PaymentRequest;
import com.booking.app_booking.repository.BookingRepository;
import com.booking.app_booking.repository.PaymentRepository;
import com.booking.app_booking.service.NotificationService;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    @Value("${stripe.api.secret.key}")
    private String secretKey;

    public String createPaymentIntent(PaymentRequest paymentRequest){
        Stripe.apiKey = secretKey;
        String bookingReference = paymentRequest.getBookingReference();

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                    .orElseThrow(() -> new NotFoundException("Booking not found"));

        if(booking.getPaymentStatus() == PaymentStatus.COMPLETADO){
            throw new  NotFoundException("Payment already made for this booking");
        }

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                                            .setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                            .setCurrency("usd")
                                            .putMetadata("bookingReference", bookingReference)
                                            .build();
            PaymentIntent intent = PaymentIntent.create(params);
            return intent.getClientSecret();

        } catch (Exception e) {
            throw new NotFoundException("Error creating payment intent");
        }
    }

    public void updatePaymentBooking(PaymentRequest paymentRequest){
        String bookingReference = paymentRequest.getBookingReference();

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new NotFoundException("Booking reference not found"));

        Payment payment = new Payment();
        payment.setPaymentGateway(PaymentGateway.STRIPE);
        payment.setAmount(paymentRequest.getAmount());
        payment.setTransactionId(paymentRequest.getTransactionId());
        payment.setPaymentStatus(paymentRequest.isSuccess() ? PaymentStatus.COMPLETADO : PaymentStatus.FALLIDO);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setBookingReference(bookingReference);
        payment.setUser(booking.getUser());

        if (!paymentRequest.isSuccess()) {
            payment.setFailureReason(paymentRequest.getFailureReason());
        }

        paymentRepository.save(payment);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                    .recipient(booking.getUser().getEmail())
                    .type(NotificationType.EMAIL)
                    .bookingReference(bookingReference)
                    .build();

        if (paymentRequest.isSuccess()) {
            booking.setPaymentStatus(PaymentStatus.COMPLETADO);
            bookingRepository.save(booking);

            notificationDTO.setSubject("Booking Payment Succefully");
            notificationDTO.setMessage("Congratulation!! Your payment for booking with reference " + bookingReference + " is succefully");
            notificationService.sendEmail(notificationDTO);
        }else{
            booking.setPaymentStatus(PaymentStatus.FALLIDO);
            bookingRepository.save(booking);

            notificationDTO.setSubject("Booking Payment Failed");
            notificationDTO.setMessage("Your payment for booking with reference " + bookingReference + " failed with reason " + paymentRequest.getFailureReason());
            notificationService.sendEmail(notificationDTO);
        }
    }



}
