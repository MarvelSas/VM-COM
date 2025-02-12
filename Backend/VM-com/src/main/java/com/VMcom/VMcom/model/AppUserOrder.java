package com.VMcom.VMcom.model;


import com.VMcom.VMcom.enums.OrderStatus;
import com.VMcom.VMcom.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "appUserOrder")
    private List<AppUserOrderLine> appUserOrderLines;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "appUser_id")
    private AppUser appUser;
    private double totalPrice;
    private LocalDate createDate;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;


}
