package com.VMcom.VMcom.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import com.VMcom.VMcom.model.Response;
import com.VMcom.VMcom.model.ShopCartLineDAO;
import com.VMcom.VMcom.services.ShopCartService;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
public class ShopCartController {

    private final ShopCartService shopCartService;


    @GetMapping("/shop-cart")
    public ResponseEntity<Response> getShopCard(){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", shopCartService.getShopCart()))
                            .message("Shop card returned successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Shop card were not returned successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }

    @PostMapping("/shopCartLine")
    public ResponseEntity<Response> getShopCard(@RequestBody ShopCartLineDAO shopCartLineDAO){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", shopCartService.addShopCartLine(shopCartLineDAO)))
                            .message("Shop card returned successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Shop card were not returned successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }

}