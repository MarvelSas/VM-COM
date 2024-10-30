package com.VMcom.VMcom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopCartLineDAO {
    
    private Product product;
    private int quantity;
}
