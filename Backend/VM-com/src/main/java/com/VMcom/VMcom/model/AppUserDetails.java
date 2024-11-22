package com.VMcom.VMcom.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserDetails {

    private String firstName;
    private String lastName;

}
