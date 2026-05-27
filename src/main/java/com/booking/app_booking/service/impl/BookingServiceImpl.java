package com.booking.app_booking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.booking.app_booking.dto.BookingDTO;
import com.booking.app_booking.dto.NotificationDTO;
import com.booking.app_booking.dto.Response;
import com.booking.app_booking.entity.Booking;
import com.booking.app_booking.entity.Room;
import com.booking.app_booking.entity.User;
import com.booking.app_booking.enums.BookingStatus;
import com.booking.app_booking.enums.PaymentStatus;
import com.booking.app_booking.exception.InvalidBookingStateAndDateException;
import com.booking.app_booking.exception.NotFoundException;
import com.booking.app_booking.repository.BookingRepository;
import com.booking.app_booking.repository.RoomRepository;
import com.booking.app_booking.service.BookingCodeGenerator;
import com.booking.app_booking.service.BookingService;
import com.booking.app_booking.service.NotificationService;
import com.booking.app_booking.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final BookingCodeGenerator bookingCodeGenerator;
    
    @Override
    public Response createBooking(BookingDTO bookingDTO) {
        User currentUser = userService.getCurrentLoggedInUser();

        Room room = roomRepository.findById(bookingDTO.getRoomId())
                    .orElseThrow(() -> new NotFoundException("Room not found"));

        if(bookingDTO.getCheckInDate().isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDateException("check in date cannot be before today");
        }

        if(bookingDTO.getCheckInDate().isBefore(bookingDTO.getCheckInDate())){
            throw new InvalidBookingStateAndDateException("check out date cannot be before check in date");
        }

        if(bookingDTO.getCheckInDate().isEqual(bookingDTO.getCheckOutDate())){
            throw new InvalidBookingStateAndDateException("check in date cannot be equal to check out date");
        }

        boolean isAvailable = bookingRepository.isRoomAvailable(room.getId(), bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());
        if(!isAvailable){
            throw new InvalidBookingStateAndDateException("Room is not available for the selected date ranges");
        }

        BigDecimal totalPrice = calculateTotalPrice(room, bookingDTO);
        String  bookingReference = bookingCodeGenerator.generateBookingReference();

        Booking booking = new Booking();
        booking.setUser(currentUser);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingReference(bookingReference);
        booking.setBookingStatus(BookingStatus.RESERVADO);
        booking.setPaymentStatus(PaymentStatus.PENDIENTE);

        bookingRepository.save(booking);

        String paymentUrl = "http://localhost:3000/payment/" + bookingReference + "/" + totalPrice;

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(currentUser.getEmail())
                .subject("Confirmación de Reserva")
                .message(String.format( "Tu reserva ha sido creado exitosamente. Por favor proceda con el pago usando el enlace a continuacion",
                         paymentUrl))
                .bookingReference(bookingReference)
                .build();

        notificationService.sendEmail(notificationDTO);

        return Response.builder()
                .status(200)
                .message("Reserva exitosa")
                .booking(bookingDTO)
                .build();
    }

    @Override
    public Response findBookingByReferenceNo(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                            .orElseThrow(() -> new NotFoundException( "Booking with Reference No: " + bookingReference + "Not found"));

        return null;

    }

    @Override
    public Response updateBooking(BookingDTO bookingDTO) {
        if(bookingDTO.getId() == null) throw new NotFoundException("Id de reserva requerida");

        Booking existBooking = bookingRepository.findById(bookingDTO.getId())
                                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));

        if(bookingDTO.getBookingStatus() != null){
            existBooking.setBookingStatus(bookingDTO.getBookingStatus());
        }
        if (bookingDTO.getPaymentStatus() !=null) {
            existBooking.setPaymentStatus(bookingDTO.getPaymentStatus());
        }

        bookingRepository.save(existBooking);

        return Response.builder()
            .status(200)
            .message("Actualización de reserva exitosa")
            .build();
    }

    private BigDecimal calculateTotalPrice(Room room, BookingDTO bookingDTO){
        BigDecimal pricePerNight = room.getPricePerNight();
        long days = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());
        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }

    @Override
    public Response getAllBooking() {
        List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BookingDTO> bookingDTOList = modelMapper.map(bookingList, new TypeToken<List<BookingDTO>> () {}.getType());

        for(BookingDTO bookingDTO: bookingDTOList){
            bookingDTO.setUser(null);
            bookingDTO.setRoom(null);
        }
        return Response.builder()
                .status(200)
                .message("succefully")
                .bookings(bookingDTOList)
                .build();
    }
}
