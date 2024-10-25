package com.VMcom.VMcom.model;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShopCardLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private List<Product> products;
    private Long amount;
    @ManyToOne
    @JoinColumn(name = "shop_card_id")
    private ShopCard shopCard;

}
