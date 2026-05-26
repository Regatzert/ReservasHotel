package com.booking.app_booking.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.booking.app_booking.dto.Response;
import com.booking.app_booking.dto.RoomDTO;
import com.booking.app_booking.enums.RoomType;
import com.booking.app_booking.service.RoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/add")   
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addRoom(
        @RequestParam Integer roomNumber,
        @RequestParam RoomType type,
        @RequestParam BigDecimal pricePerNight,
        @RequestParam Integer capacity,
        @RequestParam String description,
        @RequestParam MultipartFile imageFile
    ){
        RoomDTO roomDTO = RoomDTO.builder()
                .roomNumber(roomNumber)
                .type(type)
                .pricePerNight(pricePerNight)
                .capacity(capacity)
                .description(description)
                .build();

        return ResponseEntity.ok(roomService.addRoom(roomDTO, imageFile));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(
        @RequestParam (value = "roomNumber", required = false) Integer roomNumber,
        @RequestParam (value = "type", required = false) RoomType type,
        @RequestParam (value = "pricePerNight", required = false) BigDecimal pricePerNight,
        @RequestParam (value = "capacity", required = false) Integer capacity,
        @RequestParam (value = "description", required = false) String description,
        @RequestParam (value = "imageFile", required = false) MultipartFile imageFile,
        @RequestParam (value = "id", required = true) Long id
    ){
        RoomDTO roomDTO = RoomDTO.builder()
                .id(id)
                .roomNumber(roomNumber)
                .type(type)
                .pricePerNight(pricePerNight)
                .capacity(capacity)
                .description(description)
                .build();

        return ResponseEntity.ok(roomService.updateRoom(roomDTO, imageFile));
    }

    @GetMapping("/all")  
    public ResponseEntity<Response> getAllRooms(){
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")  
    public ResponseEntity<Response> getRoomById(@PathVariable Long id){
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @DeleteMapping("/delete/{id}")  
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long id){
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }

}
