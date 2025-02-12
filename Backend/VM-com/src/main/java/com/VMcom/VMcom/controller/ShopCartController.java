package com.VMcom.VMcom.controller;


import com.VMcom.VMcom.model.Response;
import com.VMcom.VMcom.model.ShopCartLineDAO;
import com.VMcom.VMcom.services.ShopCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
public class ShopCartController {

    private final ShopCartService shopCartService;


    @GetMapping("/shopCart")
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
    public ResponseEntity<Response> addShopCartLine(@RequestBody ShopCartLineDAO shopCartLineDAO){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", shopCartService.addShopCartLine(shopCartLineDAO)))
                            .message("Shop card line added or updated successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Shop card line was not added or updated successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }


    @DeleteMapping("/shopCartLine/{shopCartLineId}")
    public ResponseEntity<Response> deleteShopCartLine(@PathVariable("shopCartLineId") Long shopCartLineId){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", shopCartService.deleteShopCartLine(shopCartLineId)))
                            .message("Shop card deleted or updated successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Shop card was not deleted successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }

    @DeleteMapping("/shopCart")
    public ResponseEntity<Response> clearShopCart(){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", shopCartService.clearShopCart()))
                            .message("Shop card cleared successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Shop card was not cleared successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }

    @PatchMapping("/shopCartLine/{shopCartLineId}")
    public ResponseEntity<Response> updateShopCartLineQuantity(@RequestBody int shopCartLineQuantity, @PathVariable("shopCartLineId") Long shopCartLineId) {

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", shopCartService.updateShopCartLineQuantity(shopCartLineId, shopCartLineQuantity)))
                            .message("Shop card  updated successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Shop card was not  updated successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }

    }


}