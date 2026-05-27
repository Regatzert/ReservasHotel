package com.booking.app_booking.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.booking.app_booking.dto.Response;
import com.booking.app_booking.dto.RoomDTO;
import com.booking.app_booking.entity.Room;
import com.booking.app_booking.enums.RoomType;
import com.booking.app_booking.exception.InvalidBookingStateAndDateException;
import com.booking.app_booking.exception.NotFoundException;
import com.booking.app_booking.repository.RoomRepository;
import com.booking.app_booking.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    private final static String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";
    
    @Override
    public Response addRoom(RoomDTO roomDTO, MultipartFile imageFile) {
        Room roomToSave = modelMapper.map(roomDTO, Room.class);      
        
        if (imageFile != null) {
            String imagePath = saveImage(imageFile);
            roomToSave.setImageUrl(imagePath);
        }
        roomRepository.save(roomToSave);

        return Response.builder()
                    .status(200)
                    .message("Room succefully added")                    
                    .build();
    }

    @Override
    public Response updateRoom(RoomDTO roomDTO, MultipartFile imageFile) {
        Room existingRoom = roomRepository.findById(roomDTO.getId())
                            .orElseThrow(() -> new NotFoundException("Room not found"));

        if(imageFile != null && !imageFile.isEmpty()){
            String imagePath = saveImage(imageFile);
            existingRoom.setImageUrl(imagePath);
        }

        if (roomDTO.getRoomNumber() != null && roomDTO.getRoomNumber() >= 0) {
            existingRoom.setRoomNumber(roomDTO.getRoomNumber());
        }

        if (roomDTO.getPricePerNight() != null && roomDTO.getPricePerNight().compareTo(BigDecimal.ZERO) >= 0) {
            existingRoom.setPricePerNight(roomDTO.getPricePerNight());
        }

        if (roomDTO.getCapacity() != null && roomDTO.getCapacity() > 0) {
            existingRoom.setCapacity(roomDTO.getCapacity());
        }

        if(roomDTO.getType() != null) existingRoom.setType(roomDTO.getType());

        if(roomDTO.getDescription() != null) existingRoom.setDescription(roomDTO.getDescription());

        return Response.builder()
                .status(200)
                .message("Room updated succefully")
                .build();
    }

    @Override
    public Response getAllRooms() {
        List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<RoomDTO> roomDTOList = modelMapper.map(roomList, new TypeToken<List<RoomDTO>> () {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .rooms(roomDTOList)
                .build();
    }

    @Override
    public Response getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new NotFoundException("Room not found"));
        
        RoomDTO roomDTO = modelMapper.map(room, RoomDTO.class);

        return Response.builder()
                .status(200)
                .message("success")
                .room(roomDTO)
                .build();
    }

    @Override
    public Response deleteRoom(Long roomId) {
        if(!roomRepository.existsById(roomId))
        {
            throw new NotFoundException("Room not found");
        }
        
        roomRepository.deleteById(roomId);

        return Response.builder()
                .status(200)
                .message("Room deleted succefully")
                .build();
    }

    @Override
    public Response getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        if(checkInDate.isBefore(LocalDate.now())){
            throw new InvalidBookingStateAndDateException("check in date cannot be before today");
        }

        if(checkOutDate.isBefore(checkInDate)){
            throw new InvalidBookingStateAndDateException("check out date cannot be before check in date");
        }

        if(checkInDate.isEqual(checkOutDate)){
            throw new InvalidBookingStateAndDateException("check in date cannot be equal to check out date");
        }

        List<Room> roomList = roomRepository.findAvailableRooms(checkInDate, checkOutDate, roomType);

        List<RoomDTO> roomDTOList = modelMapper.map(roomList, new TypeToken<List<RoomDTO>>() {}.getType());

        return Response.builder()
                        .status(200)
                        .message("success")
                        .rooms(roomDTOList)
                        .build();
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return Arrays.asList(RoomType.values());
    }

    @Override
    public Response searchRoom(String input) {
        List<Room> roomList = roomRepository.searchRooms(input);

        List<RoomDTO> roomDTOList = modelMapper.map(roomList, new TypeToken<List<RoomDTO>>() {}.getType());

        return Response.builder()
                        .status(200)
                        .message("success")
                        .rooms(roomDTOList)
                        .build();
    }



    private String saveImage(MultipartFile imageFile) {
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Image file is required");        
        }
        
        File directory = new File(IMAGE_DIRECTORY);

        if(!directory.exists()){
            directory.mkdir();
        }

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());   
        }
        return imagePath;
    }

}
