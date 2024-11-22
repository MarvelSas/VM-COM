package com.VMcom.VMcom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue()
    private Long id;

    @NotBlank(message = "Street is required")
    private String street;
    @NotBlank(message = "Zip code is required")
    private String zipCode;
    @NotBlank(message = "City is required")
    private String city;
    @JsonIgnore
    @ManyToOne      
    @JoinColumn(name = "appUser_id")
    private AppUser appUser;
    @JsonIgnore
    @OneToMany(mappedBy = "address")
    private List<AppUserOrder> appUserOrders;
}
