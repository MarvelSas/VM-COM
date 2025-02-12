package com.VMcom.VMcom.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ProductSpecificationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String value;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductSpecificationLine(String title, String value, Product product) {
        this.title = title;
        this.value = value;
        this.product = product;
    }
}
