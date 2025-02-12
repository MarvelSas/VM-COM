package com.VMcom.VMcom.model;


import com.VMcom.VMcom.enums.PaymentMethod;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppUserOrderDetails {


    private PaymentMethod paymentMethod;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;


}
