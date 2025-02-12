package com.VMcom.VMcom.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenciationResponse {
    private String accessToken;
    private String refreshToken;
}
