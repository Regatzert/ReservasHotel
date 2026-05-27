package com.booking.app_booking.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.booking.app_booking.repository.BookingReferenceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingCodeGenerator {
    private final BookingReferenceRepository bookingReferenceRepository;

    public String generateBookingReference(){
        String bookingReference;

        do {
            bookingReference = generateRandomAlphaNumericCode(10);
        } while (isBookingReferenceExist(bookingReference));

        return bookingReference;
    }

    private String generateRandomAlphaNumericCode(int length){
        String characters = "ABCDEFGHI";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for(int i = 0; i < length; i++){
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }

    private boolean isBookingReferenceExist(String bookingReference){
        return bookingReferenceRepository.findByReferenceNo(bookingReference).isPresent();
    }
}
