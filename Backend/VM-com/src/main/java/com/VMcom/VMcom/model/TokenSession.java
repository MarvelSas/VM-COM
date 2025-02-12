package com.VMcom.VMcom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TokenSession {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "access_token_id")
    private Token accessToken;
    @ManyToOne
    @JoinColumn(name = "refresh_token_id")
    private Token refreshToken;
}
