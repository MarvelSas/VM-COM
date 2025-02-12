package com.VMcom.VMcom.controller;

import com.VMcom.VMcom.model.AuthenciationRequest;
import com.VMcom.VMcom.model.RegisterRequest;
import com.VMcom.VMcom.model.Response;
import com.VMcom.VMcom.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthencicationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request){

        try {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("token", authenticationService.register(request)))
                            .message("User was created successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("token", ""))
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
    }


    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenciationRequest request){

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("token", authenticationService.authenticate(request)))
                        .message("successfully logon")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Response> refreshToken(HttpServletRequest request){

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("token", authenticationService.refresthToken(request)))
                        .message("successfully logon")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );

    }


    @PostMapping("/change-password")
    public ResponseEntity<Response> changePassword(@RequestBody Map<String,String> request){

        try {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("passwordChange", authenticationService.changePassword(request)))
                            .message("password changed successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("password was not changed successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }
    }



}
