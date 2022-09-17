package com.example.basicapiproducts.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "products")
@DynamicUpdate
@RequiredArgsConstructor
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NonNull
    private String name;

    @Column
    @NonNull
    private String brand;

    @Column
    @NonNull
    private Float price;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="product_id")
    private Set<ProductImg> images;
    public void fromOther(Product other){
        if (other.getName() != null) this.setName(other.getName());
        if (other.getBrand() != null) this.setBrand(other.getBrand());
        if (other.getPrice() != null) this.setPrice(other.getPrice());
    }
}
