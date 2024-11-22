package com.VMcom.VMcom.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor

public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "description", length = 8192)
    private String description;
    private Double price;
    private List<String> photos;
    private int mainPhotoId;
    private Long amount;
    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ShopCartLine> shopCardLines;
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<AppUserOrderLine> appUserOrderLines;

    public Product(String name, String description, Double price, List<String> photos, int mainPhotoId, Long amount, ProductCategory productCategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.photos = photos;
        this.mainPhotoId = mainPhotoId;
        this.amount = amount;
        this.productCategory = productCategory;
    }


}
