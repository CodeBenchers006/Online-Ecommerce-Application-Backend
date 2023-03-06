package codebenchers006.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int inventory_id;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    private int totalItems;
}