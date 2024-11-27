package com.VMcom.VMcom.controller;

import com.VMcom.VMcom.enums.OrderStatus;
import com.VMcom.VMcom.model.AppUserOrderDetails;
import com.VMcom.VMcom.model.Response;
import com.VMcom.VMcom.services.AppUserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppUserOrderController {

    private final AppUserOrderService appUserOrderService;

    @PostMapping("/appUserOrder")
    public ResponseEntity<Response> addAddress(@RequestBody AppUserOrderDetails appUserOrderDetails){





        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", appUserOrderService.addAppUserOrder(appUserOrderDetails)))
                            .message("Order added successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Order were not added successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }

    @GetMapping("/appUserOrders")
    public ResponseEntity<Response> getAppUserOrders(){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", appUserOrderService.getAllUserOrders()))
                            .message("Orders returned successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Orders were not returned successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }

    @GetMapping("/appUserOrder/{appUserOrderId}")
    public ResponseEntity<Response> getAppUserOrder(@PathVariable("appUserOrderId")Long appUserOrderId){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", appUserOrderService.getAppUserOrder(appUserOrderId)))
                            .message("Order returned successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Order were not returned successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }


    @PatchMapping("/appUserOrder/{appUserOrderId}")
    public ResponseEntity<Response> updateAppUserOrderStatus(@PathVariable("appUserOrderId")Long appUserOrderId, @RequestBody OrderStatus orderStatus){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("data", appUserOrderService.updateAppUserOrderStatus(appUserOrderId,orderStatus)))
                            .message("Order status updated successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Order status were not updated successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }


    }

}
