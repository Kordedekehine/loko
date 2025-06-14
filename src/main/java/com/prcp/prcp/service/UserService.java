package com.prcp.prcp.service;

import com.prcp.prcp.dto.JwtResponseDto;
import com.prcp.prcp.dto.LoginRequestDto;
import com.prcp.prcp.dto.SignupRequestDto;
import com.prcp.prcp.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    UserResponseDto signUp (SignupRequestDto userSignUpRequest);

    JwtResponseDto login (LoginRequestDto userLoginRequestDto, HttpServletResponse response);

    JwtResponseDto refreshToken (String oldToken);

    UserResponseDto getCurrentUser();
}
