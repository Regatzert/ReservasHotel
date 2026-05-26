package com.booking.app_booking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.booking.app_booking.dto.Response;
import com.booking.app_booking.dto.RoomDTO;
import com.booking.app_booking.enums.RoomType;

public interface RoomService {

    Response addRoom(RoomDTO roomDTO, MultipartFile imageFile);
    Response updateRoom(RoomDTO roomDTO, MultipartFile imageFile);
    Response getAllRooms();
    Response getRoomById(Long roomId);
    Response deleteRoom(Long roomId);
    Response getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType);
    List<RoomType> getAllRoomTypes();
    Response searchRooms(String input);

}
