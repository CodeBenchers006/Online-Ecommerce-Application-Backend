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

    @OneToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id",nullable = true)
    private Product product;

    private int totalItems;
}
