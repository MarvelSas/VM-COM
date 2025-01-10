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


    @NotBlank(message = "first name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    @NotBlank(message = "phone number is required")
    private String phoneNumber;
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

    public Address(String firstName, String lastName, String phoneNumber, String street, String zipCode, String city, AppUser appUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.appUser = appUser;
    }
}
