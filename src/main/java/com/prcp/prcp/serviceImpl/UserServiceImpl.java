package com.prcp.prcp.serviceImpl;


import com.prcp.prcp.dto.JwtResponseDto;
import com.prcp.prcp.dto.LoginRequestDto;
import com.prcp.prcp.dto.SignupRequestDto;
import com.prcp.prcp.dto.UserResponseDto;
import com.prcp.prcp.entity.User;
import com.prcp.prcp.enums.Roles;
import com.prcp.prcp.helper.CustomUserDetails;
import com.prcp.prcp.helper.JwtService;
import com.prcp.prcp.helper.UserDetailsServiceImpl;
import com.prcp.prcp.repository.UserRepository;
import com.prcp.prcp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserResponseDto signUp(SignupRequestDto userSignUpRequest) {

        Optional<User> checkIfUserAlreadyExists = Optional.ofNullable(userRepository.findByUsernameOrEmail(userSignUpRequest.getUsername()));

        if (checkIfUserAlreadyExists.isPresent()){
            throw new UsernameNotFoundException("USER WITH SAME USERNAME/MAIL ALREADY EXISTS");
        }

        if (!userSignUpRequest.getPassword().equals(userSignUpRequest.getConfirmPassword()))
            throw new RuntimeException("Password confirmation does not match");

        if (userSignUpRequest.getRole() == null || userSignUpRequest.getRole().isBlank()) {
            throw new RuntimeException("Role is required");
        }

        User user = new User();
        user.setId(user.getId());
        user.setUsername(userSignUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));
        user.setFirstName(userSignUpRequest.getFirstName());
        user.setLastName(userSignUpRequest.getLastName());
        user.setEmail(userSignUpRequest.getEmail());
        user.setState(userSignUpRequest.getState());
        user.setCountryCode(userSignUpRequest.getCountryCode());

        try {
            Roles roles = Roles.valueOf(userSignUpRequest.getRole().toUpperCase());
            user.setRoles(roles);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided. Allowed values: INITIATOR, HOC, OPERATIONAL_OFFICER, COO");
        }

        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setRole(user.getRoles().toString());
        BeanUtils.copyProperties(user, userResponseDto);
        return userResponseDto;
    }

    @Override
    public JwtResponseDto login(LoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequestDto.getUsernameOrMail(),
                        userLoginRequestDto.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {

         //   UserDetails userDetails = (UserDetails) authentication.getPrincipal();
           CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateToken(userDetails);

            JwtResponseDto jwtResponseDto = new JwtResponseDto();
            jwtResponseDto.setAccessToken(accessToken);
            jwtResponseDto.setExpiresIn(jwtService.getExpirationTime());

            return jwtResponseDto;
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }


//    @Override
//    public JwtResponseDto refreshToken(String oldToken) {
//
//        String username = jwtService.extractUsername(oldToken.substring(7));
//        String newToken = jwtService.generateToken(username);
//
//        JwtResponseDto jwtResponseDto = new JwtResponseDto();
//        jwtResponseDto.setAccessToken(newToken);
//        jwtResponseDto.setExpiresIn(jwtService.getExpirationTime());
//
//        return jwtResponseDto;
//    }

    @Override
    public JwtResponseDto refreshToken(String oldToken) {
        String username = jwtService.extractUsername(oldToken.substring(7));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newToken = jwtService.generateToken(userDetails);

        JwtResponseDto jwtResponseDto = new JwtResponseDto();
        jwtResponseDto.setAccessToken(newToken);
        jwtResponseDto.setExpiresIn(jwtService.getExpirationTime());

        return jwtResponseDto;
    }


//    @Override
//    public UserResponseDto getCurrentUser() {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        User user = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        UserResponseDto responseDto = new UserResponseDto();
//        user.setRoles(user.getRoles());
//        BeanUtils.copyProperties(user, responseDto);
//
//        return responseDto;
//    }

    @Override
    public UserResponseDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userDetails.getId());
        responseDto.setUsername(userDetails.getUsername());
        responseDto.setCountryCode(userDetails.getCountryCode());
        responseDto.setState(userDetails.getState());
        responseDto.setEmail(userDetails.getEmail());
        responseDto.setFirstName(userDetails.getFirstName());
        responseDto.setLastName(userDetails.getLastName());
        responseDto.setPassword(userDetails.getPassword());
        responseDto.setRole(userDetails.getAuthorities().stream().findFirst().get().getAuthority());

        return responseDto;
    }


}
