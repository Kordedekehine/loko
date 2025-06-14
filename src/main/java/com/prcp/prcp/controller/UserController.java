package com.prcp.prcp.controller;

import com.prcp.prcp.dto.LoginRequestDto;
import com.prcp.prcp.dto.SignupRequestDto;
import com.prcp.prcp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

  private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignupRequestDto signupRequestDto){

        return new ResponseEntity<>(userService.signUp(signupRequestDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginRequestDto userLoginRequestDto, HttpServletResponse response) {

        return new ResponseEntity<>(userService.login(userLoginRequestDto,response), HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String oldToken) {

        return new ResponseEntity<>(userService.refreshToken(oldToken), HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser() {

        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }
}
