package com.booking.app_booking.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.app_booking.dto.BookingDTO;
import com.booking.app_booking.dto.LoginRequest;
import com.booking.app_booking.dto.RegistrationRequest;
import com.booking.app_booking.dto.Response;
import com.booking.app_booking.dto.UserDTO;
import com.booking.app_booking.entity.Booking;
import com.booking.app_booking.entity.User;
import com.booking.app_booking.enums.UserRole;
import com.booking.app_booking.exception.NotFoundException;
import com.booking.app_booking.repository.BookingRepository;
import com.booking.app_booking.repository.UserRepository;
import com.booking.app_booking.security.JwtUtils;
import com.booking.app_booking.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;

    @Override
    public Response registerUser(RegistrationRequest registrationRequest) {
        UserRole role = UserRole.CLIENTE;

        if(registrationRequest.getRole() != null) {
            role = registrationRequest.getRole();            
        }

        User userToSave = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .isActive(Boolean.TRUE)
                .build();
        userRepository.save(userToSave);
        return Response.builder()
                        .status(200)
                        .message("User registered successfully")
                        .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email no encontrado: "));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new NotFoundException("Contraseña incorrecta");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                        .status(200)
                        .message("User logged in successfully")
                        .role(user.getRole())
                        .token(token)
                        .isActive(user.getIsActive())
                        .expirationTime("6 meses")
                        .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List <UserDTO> UserDTOList = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());

        return Response.builder()
                        .status(200)
                        .message("Users retrieved successfully")
                        .users(UserDTOList)
                        .build();
    }

    @Override
    public Response getOwnAccountDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + email));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.builder()
                        .status(200)
                        .message("User details retrieved successfully")
                        .user(userDTO)
                        .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + email));
    }

    @Override
    public Response updateOwnAccount(UserDTO userDTO) {
        User existingUser = getCurrentLoggedInUser();

        if(userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if(userDTO.getFirstName() != null) existingUser.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null) existingUser.setLastName(userDTO.getLastName());
        if(userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        if(userDTO.getPassword() != null  && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(existingUser);

        return Response.builder()
                        .status(200)
                        .message("User details updated successfully")
                        .build();
    }

    @Override
    public Response deleteOwnAccount() {
        User user = getCurrentLoggedInUser();
        userRepository.delete(user);

        return Response.builder()
                        .status(200)
                        .message("User account deleted successfully")
                        .build();
    }

    @Override
    public Response getMyBookingHistory() {
        User user = getCurrentLoggedInUser();

        List<Booking> bookingList = bookingRepository.findByUserId(user.getId());

        List<BookingDTO> bookingDTOList = modelMapper.map(bookingList, new TypeToken<List<BookingDTO>>() {}.getType());

        return Response.builder()
                        .status(200)
                        .message("Booking history retrieved successfully")
                        .bookings(bookingDTOList)
                        .build();
    }

}
