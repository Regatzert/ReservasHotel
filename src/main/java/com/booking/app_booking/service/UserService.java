package com.booking.app_booking.service;

import com.booking.app_booking.dto.LoginRequest;
import com.booking.app_booking.dto.RegistrationRequest;
import com.booking.app_booking.dto.Response;
import com.booking.app_booking.dto.UserDTO;
import com.booking.app_booking.entity.User;

public interface UserService {

    Response registerUser(RegistrationRequest registrationRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    Response getOwnAccountDetails();

    User getCurrentLoggedInUser();

    Response updateOwnAccount(UserDTO userDTO);

    Response deleteOwnAccount();

    Response getMyBookingHistory();

}
